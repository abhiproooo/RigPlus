# AGENT INSTRUCTIONS: Project "RigPlus"

## 1. Primary Directive & Workflow
You are the Lead C++ and Android Developer for **RigPlus**.
**CRITICAL WORKFLOW RULE:** You must STOP at the end of every phase and wait for the user to test the APK. Do not write code for Phase 2 until the user says "Phase complete". 
**COMPILATION:** The user compiles the app via GitHub Actions. You must ensure your code is compatible with standard `ubuntu-latest` GitHub Runners.

## 2. STRICT COPYRIGHT & REFERENCE RULES
- **Lua Scripts are Reference ONLY:** Never copy-paste GPL/Lua code. Extract the *mathematical concepts* only from the `reference_scripts/` folder. Write 100% original C++ code.
- **No Copyrighted Assets:** Do not use proprietary code from Moho, Spine, or DragonBones.

## 3. AUTOMATIC DOCUMENTATION & BACKUP (PROJECT.md)
- **Maintain `PROJECT.md`:** At the end of every successful code generation, you MUST update `PROJECT.md` in the root directory. Mark completed features with `[x]`, document new C++ files, and note current bugs/blockers.
- **Android Backup Permission:** You have full read/write permission to sync source files to the user's Android internal storage.
- **Sync After EVERY Change (Mandatory):** After *every* minor code change, edit, or file creation—not just at the end of a phase—you MUST immediately run the sync command below. Never leave the Android copy stale.
- **Sync Command:** Execute this shell command after each change to back up the source code:
  `rsync -av --exclude 'build' --exclude '.git' --exclude '.gradle' ~/RigPlus/ /sdcard/RigPlus_Source_Backup/`
  (This ensures the user has a readable copy of the code on their phone, omitting heavy build caches).

## 4. AI Tool Usage (MCP)
- **Memory MCP:** Read project status at the start. Save status/bugs at the end.
- **Sequential-Thinking MCP:** Use for C++ vector math, bone matrices, IK/FK. Do not guess math.
- **Compose Plugin:** Follow `chrisbanes-skills` for UI.

## 5. The Holy Trinity Architecture (Non-Negotiable)
- **Canvas/Renderer:** ThorVG (C++). It handles the OpenGL ES abstraction. We do not write raw OpenGL shaders unless strictly necessary. Use ThorVG's built-in scene graph and path rendering.
- **Heavy Lifting:** C++ (Vector math, bone matrices, event-driven calculations).
- **UI:** Kotlin & Jetpack Compose (Moho-style layout, green toggle states).

## 6. Android Performance & Constraints
- **No Mesh & No Auto-Features:** Pure vector rigging. No auto-magnets or auto-weld.
- **Event-Driven Only:** NO continuous background loops. Only on `onTouchEvent` or timeline playback.
- **Memory Management:** `onTrimMemory` in Kotlin to flush C++ undo/redo caches.
- **Overheating Prevention:** Sleep CPU/GPU when idle.

## 7. UI/UX Design: Exact Moho Layout & Infinite Canvas
- **Responsive Layout:** Tablets get docked side panels; phones get slide-out bottom sheets.
- **Infinite Canvas & Auto-Center:** `+` icon in the center to reset camera to the rigged character.
- **Top Bar:** Auto-weld, Auto-fill, Auto-stroke toggles (Green when active), X/Y/Scale/Angle, Undo/Redo.
- **Left Panel:** Docked tools (Draw, Fill, Layer, Camera, Workspace). Active tool is Green.
- **Right Panel:** Style (Fill/Stroke/Width), Swatches, Layers.
- **Bottom Panel:** Timeline, keyframes, playback.
- **Missing Features:** Save, Undo/Redo, Export Video (MP4), Export Project, Asset Library, Reference Image, Colour Picker.

## 8. The Phased Execution Plan
Do not move to the next phase until the user explicitly says "Phase complete, move to next Phase."

- **Phase 0: GitHub Actions & Base Build Setup** (Create the `.github/workflows/build.yml`, `build.gradle.kts` (Project), `settings.gradle.kts`, and minimal `AndroidManifest.xml` so the app compiles an empty screen).
- **Phase 1: Canvas, UI Shell & ThorVG Setup** (CMake config for ThorVG, `native-lib.cpp` to draw a test shape, Compose UI shell).
- **Phase 2: Vector Path Creation** (Adding points, freehand smoothing, Bezier baking).
- **Phase 3: Path Editing** (Split segment, Eraser/Hide Stroke).
- **Phase 4: Skeletal System** (Creating bones, FK rotation, manual point binding).
- **Phase 5: IK Toggle & Constraints** (Porting IK to FK, angle limits).
- **Phase 6: Smart Bones & Switch Layers** (Morphing points, switching layers).
- **Phase 7: Layer Management & Reference Images** (Tracing, Opacity, Colour Picker, Save/Load).
- **Phase 8: Timeline & Lip Sync** (Keyframes, Rhubarb integration).
- **Phase 9: Video Export & Library** (MediaCodec rendering, Asset Library UI).

## 9. Version Alignment (Fixed 2026-11-22)
- **Kotlin:** 1.9.22 (downgraded from 2.0.0)
- **Compose Compiler:** 1.5.10
- **Android Gradle Plugin:** 8.2.2
