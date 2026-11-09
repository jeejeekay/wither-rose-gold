package net.igobyjake.witherrosegold.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Right-clicking a block with a wither rose gold tool "blasts" a 3x3 square, flat against the
 * face that was clicked (click a wall -> a 3x3 hole one block deep; click the floor -> a 3x3 pit).
 *
 * It only looks like an explosion: blocks drop as if mined with the tool (Fortune/Silk Touch
 * work), there's one small puff of particles and one quiet sound instead of a sound per block.
 *
 * Vanilla-only (same file on Fabric and NeoForge).
 */
public final class BlastMining {
    public static final int COOLDOWN_TICKS = 10;
    /** Blocks at least this blast-resistant are never broken (obsidian, crying obsidian, netherite...). */
    public static final float MAX_BLAST_RESISTANCE = 1200.0F;

    private BlastMining() {}

    /**
     * @return the result of the blast, or null when the tool should do its normal right-click
     * instead (sneaking, on cooldown, or the clicked block can't be blasted).
     */

    public static InteractionResult tryBlast(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || player.isSecondaryUseActive()) return null;
        Level level = context.getLevel();
        ItemStack tool = context.getItemInHand();
        BlockPos center = context.getClickedPos();
        Direction face = context.getClickedFace();
        if (player.getCooldowns().isOnCooldown(tool)) return null;
        if (!canBlast(level, center, face, player, tool)) return null;
        if (!(level instanceof ServerLevel serverLevel)) return InteractionResult.SUCCESS;   // the server does the work

        int broken = 0;
        for (BlockPos pos : area(center, face)) {
            if (!canBlast(level, pos, face, player, tool)) continue;
            BlockState state = level.getBlockState(pos);
            Block.dropResources(state, level, pos, null, player, tool);   // drops + XP, as if mined with this tool
            // setBlock (not destroyBlock) = no break sound / crack particles per block. Keeps water in waterlogged blocks.
            level.setBlock(pos, state.getFluidState().createLegacyBlock(), Block.UPDATE_ALL);
            broken++;
        }

        Vec3 c = Vec3.atCenterOf(center).relative(face, 0.5);
        serverLevel.sendParticles(ParticleTypes.EXPLOSION, c.x, c.y, c.z, 1, 0.0, 0.0, 0.0, 0.0);
        serverLevel.sendParticles(ParticleTypes.POOF, c.x, c.y, c.z, 10, 0.6, 0.6, 0.6, 0.02);
        level.playSound(null, center, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.35F, 1.6F);

        player.getCooldowns().addCooldown(tool, COOLDOWN_TICKS);
        tool.hurtAndBreak(broken, player, context.getHand());   // 1 durability per block
        return InteractionResult.SUCCESS;
    }

    /** The 9 positions of the 3x3 square through {@code center}, perpendicular to {@code face}. */
    public static List<BlockPos> area(BlockPos center, Direction face) {
        List<BlockPos> positions = new ArrayList<>(9);
        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                positions.add(switch (face.getAxis()) {
                    case X -> center.offset(0, a, b);
                    case Y -> center.offset(a, 0, b);
                    case Z -> center.offset(a, b, 0);
                });
            }
        }
        return positions;
    }

    /**
     * A block can be blasted if it's a normal breakable block (not air, liquid, bedrock or a block
     * entity like a chest), isn't obsidian-tough, would actually drop something with this tool,
     * and the player is allowed to edit it (spawn protection, adventure mode...).
     */
    public static boolean canBlast(Level level, BlockPos pos, Direction face, Player player, ItemStack tool) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir() || state.hasBlockEntity() || state.getBlock() instanceof LiquidBlock) return false;
        if (state.getDestroySpeed(level, pos) < 0) return false;
        if (state.getBlock().getExplosionResistance() >= MAX_BLAST_RESISTANCE) return false;
        if (state.requiresCorrectToolForDrops() && !tool.isCorrectToolForDrops(state)) return false;
        return level.mayInteract(player, pos) && player.mayUseItemAt(pos, face, tool);
    }
}
