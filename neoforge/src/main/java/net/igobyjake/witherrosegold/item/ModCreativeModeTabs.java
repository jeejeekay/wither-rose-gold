package net.igobyjake.witherrosegold.item;

import net.igobyjake.witherrosegold.witherrosegold;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, witherrosegold.MOD_ID);

    public static final Supplier<CreativeModeTab> WITHER_ROSE_GOLD_TAB = CREATIVE_MODE_TAB.register("wither_rose_gold_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.WITHER_ROSE_GOLD_INGOT.get()))
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

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
