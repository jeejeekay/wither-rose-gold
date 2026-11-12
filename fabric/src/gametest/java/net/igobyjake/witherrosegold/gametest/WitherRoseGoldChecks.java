package net.igobyjake.witherrosegold.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.Holder;
import net.igobyjake.witherrosegold.config.WrgConfig;
import net.igobyjake.witherrosegold.effect.FullSetEffects;
import net.igobyjake.witherrosegold.entity.PiezoshardExplosionDamage;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The automated version of docs/TESTING.md.
 *
 * Only uses vanilla classes and looks items up by name, so this exact file works on both Fabric
 * and NeoForge (and needs Rose Gold Equipment loaded). Each public method is one GameTest; the
 * loader-specific files (WitherRoseGoldGameTests) hook them up. Tests that place blocks or spawn
 * things use the "witherrosegold:arena" structure, an empty 13x9x13 room.
 */
public final class WitherRoseGoldChecks {
    private static final String MOD = "witherrosegold";
    private static final String RGE = "rosegoldequipment";
    private static final String[] ARMOR = {"helmet", "chestplate", "leggings", "boots"};
    private static final String[] TOOLS = {"sword", "pickaxe", "shovel", "axe", "hoe"};
    private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    private WitherRoseGoldChecks() {}

    static Item item(String namespace, String name) {
        Item item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(namespace, name));
        if (item == null || item == Items.AIR) throw new IllegalStateException("missing item " + namespace + ":" + name);
        return item;
    }

    static Item item(String name) {
        return item(MOD, name);
    }

    static EntityType<?> entity(String namespace, String name) {
        return BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.fromNamespaceAndPath(namespace, name));
    }

    // ---------------------------------------------------------------- recipes

    /** Template copy, piezoshard, ingot, and all 9 smithing upgrades (rose gold -> wither rose gold). */
    public static void recipes(GameTestHelper h) {
        ItemStack template = new ItemStack(item("wither_rose_smithing_template"));
        ItemStack amethyst = new ItemStack(Items.AMETHYST_SHARD);
        ItemStack quartz = new ItemStack(Items.QUARTZ);
        ItemStack shard = new ItemStack(item("piezoshard"));
        ItemStack roseGold = new ItemStack(item(RGE, "rose_gold_ingot"));

        assertCrafts(h, "wither_rose_smithing_template", List.of(
                amethyst, template, amethyst,
                amethyst, new ItemStack(Items.CRYING_OBSIDIAN), amethyst,
                amethyst, amethyst, amethyst));
        assertCrafts(h, "piezoshard", List.of(
                quartz, amethyst, quartz,
                amethyst, ItemStack.EMPTY, amethyst,
                quartz, amethyst, quartz));
        assertCrafts(h, "wither_rose_gold_ingot", List.of(
                roseGold, shard, roseGold,
                shard, new ItemStack(Items.NETHER_STAR), shard,
                roseGold, shard, roseGold));

        ItemStack ingot = new ItemStack(item("wither_rose_gold_ingot"));
        List<String> pieces = new ArrayList<>(List.of(TOOLS));
        Collections.addAll(pieces, ARMOR);
        for (String piece : pieces) {
            Optional<? extends RecipeHolder<?>> recipe = h.getLevel().recipeAccess().getRecipeFor(RecipeType.SMITHING,
                    new SmithingRecipeInput(template, new ItemStack(item(RGE, "rose_golden_" + piece)), ingot),
                    h.getLevel(), (ResourceKey<Recipe<?>>) null);
            h.assertTrue(recipe.isPresent() && recipe.get().id().equals(recipeKey("wither_rose_golden_" + piece + "_smithing")),
                    "smithing rose_golden_" + piece + " doesn't give wither_rose_golden_" + piece);
        }
        h.succeed();
    }

    private static void assertCrafts(GameTestHelper h, String recipe, List<ItemStack> grid) {
        Optional<? extends RecipeHolder<?>> found = h.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING,
                CraftingInput.of(3, 3, grid), h.getLevel());
        h.assertTrue(found.isPresent() && found.get().id().equals(recipeKey(recipe)),
                "crafting grid for " + recipe + " found " + found.map(r -> r.id().toString()).orElse("nothing"));
    }

    private static ResourceKey<Recipe<?>> recipeKey(String name) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(MOD, name));
    }

    // ---------------------------------------------------------------- items

    /** Fire/explosion proofing, the piezoshard tooltip and stack size. */
    public static void items(GameTestHelper h) {
        ServerLevel level = h.getLevel();
        ItemStack template = new ItemStack(item("wither_rose_smithing_template"));
        h.assertFalse(template.canBeHurtBy(level.damageSources().inFire()), "template burns");
        h.assertFalse(template.canBeHurtBy(level.damageSources().lava()), "template burns in lava");
        h.assertFalse(template.canBeHurtBy(level.damageSources().explosion(null, null)), "template is destroyed by explosions");

        List<String> fireproof = new ArrayList<>(List.of("wither_rose_gold_ingot"));
        for (String t : TOOLS) fireproof.add("wither_rose_golden_" + t);
        for (String a : ARMOR) fireproof.add("wither_rose_golden_" + a);
        for (String name : fireproof) {
            ItemStack stack = new ItemStack(item(name));
            h.assertFalse(stack.canBeHurtBy(level.damageSources().lava()), name + " burns in lava");
            h.assertTrue(stack.canBeHurtBy(level.damageSources().explosion(null, null)), name + " should NOT be explosion proof");
        }

        ItemStack shard = new ItemStack(item("piezoshard"));
        h.assertTrue(shard.has(DataComponents.LORE) && !shard.get(DataComponents.LORE).lines().isEmpty(), "piezoshard has no description");
        h.assertTrue(shard.getMaxStackSize() == 16, "piezoshard should stack to 16");
        h.succeed();
    }

    // ---------------------------------------------------------------- armor and tools

    /** Netherite armor (20) + toughness (12) and no knockback resistance; trimmable; repairs with piezoshards (not the ingot). */
    public static void armor(GameTestHelper h) {
        double armor = 0, toughness = 0, knockback = 0;
        ItemStack ingot = new ItemStack(item("wither_rose_gold_ingot"));
        for (String piece : ARMOR) {
            ItemStack stack = new ItemStack(item("wither_rose_golden_" + piece));
            h.assertTrue(stack.is(ItemTags.TRIMMABLE_ARMOR), "wither_rose_golden_" + piece + " is not trimmable");
            h.assertFalse(stack.isValidRepairItem(ingot), "wither_rose_golden_" + piece + " shouldn't be repairable with the ingot");
            h.assertTrue(stack.isValidRepairItem(new ItemStack(item("piezoshard"))), "wither_rose_golden_" + piece + " can't be repaired with piezoshards");
            h.assertTrue(stack.has(DataComponents.LORE), "wither_rose_golden_" + piece + " doesn't describe the set bonus");
            for (ItemAttributeModifiers.Entry e : stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY).modifiers()) {
                if (e.attribute().is(Attributes.ARMOR)) armor += e.modifier().amount();
                if (e.attribute().is(Attributes.ARMOR_TOUGHNESS)) toughness += e.modifier().amount();
                if (e.attribute().is(Attributes.KNOCKBACK_RESISTANCE)) knockback += e.modifier().amount();
            }
        }
        h.assertTrue(armor == 20, "full set should give 20 armor, gives " + armor);
        h.assertTrue(toughness == 12, "full set should give 12 toughness, gives " + toughness);
        h.assertTrue(knockback == 0, "full set should give no knockback resistance, gives " + knockback);
        h.succeed();
    }

    /** Netherite mining level, 2520 durability, mining speed 11; repairs with piezoshards (not the ingot). */
    public static void tools(GameTestHelper h) {
        ItemStack ingot = new ItemStack(item("wither_rose_gold_ingot"));
        h.assertTrue(new ItemStack(item("wither_rose_golden_pickaxe")).isCorrectToolForDrops(Blocks.ANCIENT_DEBRIS.defaultBlockState()),
                "pickaxe can't harvest ancient debris");
        for (String tool : TOOLS) {
            ItemStack stack = new ItemStack(item("wither_rose_golden_" + tool));
            h.assertFalse(stack.isValidRepairItem(ingot), "wither_rose_golden_" + tool + " shouldn't be repairable with the ingot");
            h.assertTrue(stack.isValidRepairItem(new ItemStack(item("piezoshard"))), "wither_rose_golden_" + tool + " can't be repaired with piezoshards");
            h.assertTrue(stack.getMaxDamage() == 2520, "wither_rose_golden_" + tool + " durability should be 2520, is " + stack.getMaxDamage());
        }
        float speed = new ItemStack(item("wither_rose_golden_pickaxe")).getDestroySpeed(Blocks.STONE.defaultBlockState());
        h.assertTrue(speed == 11.0F, "pickaxe mining speed on stone should be 11, is " + speed);
        h.succeed();
    }

    // ---------------------------------------------------------------- 3x3 blast (arena)

    /**
     * Right-click the middle of a 3x3 stone wall with the pickaxe: 8 blocks go (the obsidian corner
     * stays), 8 cobblestone drop, the block behind the wall is untouched, 8 durability is used.
     * Then sneak + right-click a log with the axe: it gets stripped, not blasted.
     */
    public static void blastMining(GameTestHelper h) {
        for (int x = 5; x <= 7; x++) for (int y = 2; y <= 4; y++) h.setBlock(new BlockPos(x, y, 6), Blocks.STONE);
        h.setBlock(new BlockPos(5, 2, 6), Blocks.OBSIDIAN);
        h.setBlock(new BlockPos(6, 3, 7), Blocks.STONE);

        Player player = h.makeMockPlayer(GameType.SURVIVAL);
        ItemStack pickaxe = new ItemStack(item("wither_rose_golden_pickaxe"));
        player.setItemInHand(InteractionHand.MAIN_HAND, pickaxe);
        click(h, player, new BlockPos(6, 3, 6), Direction.NORTH);

        for (int x = 5; x <= 7; x++) for (int y = 2; y <= 4; y++) {
            BlockPos pos = new BlockPos(x, y, 6);
            if (x == 5 && y == 2) h.assertBlockPresent(Blocks.OBSIDIAN, pos);
            else h.assertBlockPresent(Blocks.AIR, pos);
        }
        h.assertBlockPresent(Blocks.STONE, new BlockPos(6, 3, 7));
        int cobble = 0;
        for (ItemEntity drop : h.getLevel().getEntitiesOfClass(ItemEntity.class, arenaBox(h))) {
            if (drop.getItem().is(Items.COBBLESTONE)) cobble += drop.getItem().getCount();
        }
        h.assertTrue(cobble == 8, "expected 8 cobblestone drops, got " + cobble);
        h.assertTrue(pickaxe.getDamageValue() == 8, "pickaxe should have lost 8 durability, lost " + pickaxe.getDamageValue());
        h.assertTrue(player.getCooldowns().isOnCooldown(pickaxe), "pickaxe should be on cooldown after a blast");

        h.setBlock(new BlockPos(2, 2, 2), Blocks.OAK_LOG);
        h.setBlock(new BlockPos(3, 2, 2), Blocks.OAK_LOG);
        ItemStack axe = new ItemStack(item("wither_rose_golden_axe"));
        player.setItemInHand(InteractionHand.MAIN_HAND, axe);
        player.setShiftKeyDown(true);
        click(h, player, new BlockPos(2, 2, 2), Direction.UP);
        h.assertBlockPresent(Blocks.STRIPPED_OAK_LOG, new BlockPos(2, 2, 2));
        h.assertBlockPresent(Blocks.OAK_LOG, new BlockPos(3, 2, 2));
        h.succeed();
    }

    private static void click(GameTestHelper h, Player player, BlockPos relative, Direction face) {
        BlockPos pos = h.absolutePos(relative);
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(pos).relative(face, 0.5), face, pos, false);
        player.getMainHandItem().useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
    }

    private static AABB arenaBox(GameTestHelper h) {
        BlockPos min = h.absolutePos(BlockPos.ZERO);
        return new AABB(Vec3.atLowerCornerOf(min), Vec3.atLowerCornerOf(min.offset(13, 9, 13)));
    }

    // ---------------------------------------------------------------- full-set effects (arena)

    /** The same explosion hurts a zombie in wither rose gold about half as much as one in netherite. */
    public static void blastProtection(GameTestHelper h) {
        LivingEntity wrg = armoredZombie(h, new BlockPos(3, 1, 3), "witherrosegold", "wither_rose_golden_");
        LivingEntity netherite = armoredZombie(h, new BlockPos(9, 1, 9), "minecraft", "netherite_");
        ServerLevel level = h.getLevel();
        wrg.hurtServer(level, level.damageSources().explosion(null, null), 16.0F);
        netherite.hurtServer(level, level.damageSources().explosion(null, null), 16.0F);
        float lostWrg = wrg.getMaxHealth() - wrg.getHealth();
        float lostNetherite = netherite.getMaxHealth() - netherite.getHealth();
        h.assertTrue(lostWrg > 0 && lostWrg < lostNetherite * 0.75F,
                "explosion: wither rose gold lost " + lostWrg + ", netherite lost " + lostNetherite + " (expected about half)");
        wrg.discard();
        netherite.discard();
        h.succeed();
    }

    /** Hitting a zombie in the full set zaps the attacker: exactly 3 damage, and no hit immunity afterwards. */
    public static void zap(GameTestHelper h) {
        LivingEntity victim = armoredZombie(h, new BlockPos(3, 1, 3), "witherrosegold", "wither_rose_golden_");
        LivingEntity attacker = (LivingEntity) h.spawn(entity("minecraft", "zombie"), new BlockPos(9, 1, 9));
        attacker.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));   // so daylight can't hurt it
        ServerLevel level = h.getLevel();
        for (int i = 0; i < 40 && attacker.getHealth() >= attacker.getMaxHealth(); i++) {
            // Each hit 1 more than the last: during the hurt cooldown only the extra 1 is applied, but it still
            // counts as a hit (no need to reset the cooldown, whose field differs between versions).
            victim.hurtServer(level, level.damageSources().mobAttack(attacker), 1.0F + i);
        }
        h.assertTrue(attacker.getHealth() < attacker.getMaxHealth(), "attacker was never zapped in 40 hits");
        h.assertTrue(attacker.getMaxHealth() - attacker.getHealth() == (float) WrgConfig.zapDamage,
                "a zap should do exactly 3 damage, did " + (attacker.getMaxHealth() - attacker.getHealth()));
        h.assertTrue(attacker.hurtServer(level, level.damageSources().generic(), 1.0F), "the zap left the attacker immune to the next hit");
        victim.discard();
        attacker.discard();
        h.succeed();
    }


    /** The full set gives the "Wither Rosed" effect (shown in the inventory); taking a piece off removes it. */
    public static void setBonus(GameTestHelper h) {
        LivingEntity zombie = armoredZombie(h, new BlockPos(6, 1, 6), "witherrosegold", "wither_rose_golden_");
        Holder<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.get(FullSetEffects.WITHER_ROSED).orElseThrow();
        FullSetEffects.updateSetBonus(zombie);
        h.assertTrue(zombie.hasEffect(effect), "full set should give Wither Rosed");
        zombie.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        FullSetEffects.updateSetBonus(zombie);
        h.assertFalse(zombie.hasEffect(effect), "Wither Rosed should go away without the full set");
        zombie.discard();
        h.succeed();
    }

    private static LivingEntity armoredZombie(GameTestHelper h, BlockPos pos, String namespace, String prefix) {
        LivingEntity zombie = (LivingEntity) h.spawn(entity("minecraft", "zombie"), pos);
        for (int i = 0; i < ARMOR.length; i++) {
            zombie.setItemSlot(SLOTS[i], new ItemStack(item(namespace, prefix + ARMOR[i])));
        }
        return zombie;
    }

    // ---------------------------------------------------------------- piezoshard (arena)

    /** Throwing uses one shard and spawns the grenade; a grenade that lands blows up the block it hits; max 20 damage. */
    public static void piezoshard(GameTestHelper h) {
        h.assertTrue(PiezoshardExplosionDamage.damageAt(0, 1) == 20.0F, "piezoshard centre damage should be 20, is " + PiezoshardExplosionDamage.damageAt(0, 1));
        Player player = h.makeMockPlayer(GameType.SURVIVAL);
        player.setPos(h.absoluteVec(new Vec3(6.5, 2, 6.5)));
        ItemStack shards = new ItemStack(item("piezoshard"), 4);
        player.setItemInHand(InteractionHand.MAIN_HAND, shards);
        shards.getItem().use(h.getLevel(), player, InteractionHand.MAIN_HAND);
        h.assertTrue(shards.getCount() == 3, "throwing should use 1 shard, " + shards.getCount() + " left");
        EntityType<?> type = entity(MOD, "piezoshard");
        List<Entity> thrown = h.getLevel().getEntities((Entity) null, arenaBox(h), e -> e.getType() == type);
        h.assertTrue(thrown.size() == 1, "expected 1 thrown piezoshard, found " + thrown.size());
        thrown.forEach(Entity::discard);   // don't let it fly out of the arena

        h.setBlock(new BlockPos(6, 1, 6), Blocks.STONE);
        h.spawn(type, new Vec3(6.5, 4, 6.5));   // falls onto the stone
        h.succeedWhen(() -> h.assertBlockPresent(Blocks.AIR, new BlockPos(6, 1, 6)));
    }

    // ---------------------------------------------------------------- wither drop (arena)

    /** A killed wither drops the template (and still its nether star). */
    public static void witherDrop(GameTestHelper h) {
        Entity wither = h.spawn(entity("minecraft", "wither"), new BlockPos(6, 2, 6));
        wither.kill(h.getLevel());
        boolean template = false, star = false;
        for (ItemEntity drop : h.getLevel().getEntitiesOfClass(ItemEntity.class, arenaBox(h))) {
            template |= drop.getItem().is(item("wither_rose_smithing_template"));
            star |= drop.getItem().is(Items.NETHER_STAR);
            drop.discard();
        }
        h.assertTrue(template, "the wither didn't drop a wither rose smithing template");
        h.assertTrue(star, "the wither didn't drop its nether star");
        h.succeed();
    }

    // ---------------------------------------------------------------- showcase

    /** /function witherrosegold:showcase exists (it parsed, so every command in it is valid). */
    public static void showcaseLoads(GameTestHelper h) {
        h.assertTrue(h.getLevel().getServer().getFunctions().get(Identifier.fromNamespaceAndPath(MOD, "showcase")).isPresent(),
                "function witherrosegold:showcase didn't load (a command in it is invalid)");
        h.succeed();
    }
}
