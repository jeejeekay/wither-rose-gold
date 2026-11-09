package net.igobyjake.witherrosegold.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.igobyjake.witherrosegold.witherrosegold;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    public static final CreativeModeTab WITHER_ROSE_GOLD_GROUP = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(witherrosegold.MOD_ID, "wither_rose_gold_group"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.WITHER_ROSE_GOLD_INGOT))
                    .title(Component.translatable("itemgroup.witherrosegold.wither_rose_gold_group"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.WITHER_ROSE_GOLD_INGOT);
                        output.accept(ModItems.WITHER_ROSE_SMITHING_TEMPLATE);
                        output.accept(ModItems.PIEZOSHARD);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_SWORD);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_PICKAXE);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_SHOVEL);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_AXE);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_HOE);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_HELMET);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_CHESTPLATE);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_LEGGINGS);
                        output.accept(ModItems.WITHER_ROSE_GOLDEN_BOOTS);
                    }).build());

    public static void registerItemGroups() {
        witherrosegold.LOGGER.info("Registering item groups for " + witherrosegold.MOD_ID);
    }
}
