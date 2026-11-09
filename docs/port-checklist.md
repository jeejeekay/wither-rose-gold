# Porting checklist (what changed between versions)

| From -> to | Changes in WRG |
|---|---|
| 1.21.11 -> 26.1 | Java 25; Fabric Loom without remapping (`fabric-loom`, `implementation`, `runtimeOnly` for the RGE jar); no Parchment; mods.toml loses `modLoader`/`loaderVersion`; template's damage resistance via `delayedComponent` (tags load later); `FabricItemGroup` -> `FabricCreativeModeTab`; `humanoid_baby` armor layer |
| 26.1 -> 26.2 | Version numbers only |
| 26.2 -> 26.3 | `AxeItem`/`ShovelItem`/`HoeItem` removed -> every tool is a plain `BlastToolItem` made with `properties.axe()` etc. (strip/path/till come from the `block_transformer` component); trim item models regenerated from RGE (palette trims) |

For each new version: copy RGE's Gradle wrapper + version numbers for the same branch, regenerate the shared
resources from RGE (`tools/gen_resources.py`), fix compile errors, push, and wait for CI to go green.
