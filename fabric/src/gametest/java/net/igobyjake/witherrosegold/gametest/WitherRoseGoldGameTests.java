package net.igobyjake.witherrosegold.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

// Fabric runs every @GameTest method listed through the "fabric-gametest" entrypoint
// (src/gametest/resources/fabric.mod.json). The checks themselves are in WitherRoseGoldChecks.
public class WitherRoseGoldGameTests {
    // An empty 13x9x13 room (src/gametest/resources/data/witherrosegold/structure/arena.nbt)
    // for the tests that place blocks, spawn mobs or blow things up.
    private static final String ARENA = "witherrosegold:arena";

    @GameTest
    public void recipes(GameTestHelper helper) { WitherRoseGoldChecks.recipes(helper); }

    @GameTest
    public void items(GameTestHelper helper) { WitherRoseGoldChecks.items(helper); }

    @GameTest
    public void armor(GameTestHelper helper) { WitherRoseGoldChecks.armor(helper); }

    @GameTest
    public void tools(GameTestHelper helper) { WitherRoseGoldChecks.tools(helper); }

    @GameTest(structure = ARENA, maxTicks = 100)
    public void blastMining(GameTestHelper helper) { WitherRoseGoldChecks.blastMining(helper); }

    @GameTest(structure = ARENA, maxTicks = 100)
    public void blastProtection(GameTestHelper helper) { WitherRoseGoldChecks.blastProtection(helper); }

    @GameTest(structure = ARENA, maxTicks = 100)
    public void zap(GameTestHelper helper) { WitherRoseGoldChecks.zap(helper); }

    @GameTest(structure = ARENA, maxTicks = 100)
    public void piezoshard(GameTestHelper helper) { WitherRoseGoldChecks.piezoshard(helper); }

    @GameTest(structure = ARENA, maxTicks = 100)
    public void witherDrop(GameTestHelper helper) { WitherRoseGoldChecks.witherDrop(helper); }

    @GameTest
    public void showcaseLoads(GameTestHelper helper) { WitherRoseGoldChecks.showcaseLoads(helper); }
}
