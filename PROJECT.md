# RigPlus Project

## Overview
RigPlus is an Android vector rigging and animation tool inspired by Moho. The architecture follows the "Holy Trinity":
- **Canvas/Renderer:** ThorVG (C++) for OpenGL ES abstraction and path rendering
- **Heavy Lifting:** C++ for vector math, bone matrices, and event-driven calculations
- **UI:** Kotlin & Jetpack Compose

## Phased Execution Plan

- [x] **Phase 0: GitHub Actions & Base Build Setup** — COMPLETE
- [x] **Phase 1: Canvas, UI Shell & ThorVG Setup** — COMPLETE
- [ ] Phase 2: Vector Path Creation
- [ ] Phase 3: Path Editing
- [ ] Phase 4: Skeletal System
- [ ] Phase 5: IK Toggle & Constraints
- [ ] Phase 6: Smart Bones & Switch Layers
- [ ] Phase 7: Layer Management & Reference Images
- [ ] Phase 8: Timeline & Lip Sync
- [ ] Phase 9: Video Export & Library

## Phase 0 - Completed Files

| File | Description |
|------|-------------|
| `.github/workflows/build.yml` | GitHub Actions workflow: JDK 17, Android NDK 25.1.8937393, debug APK build |
| `build.gradle.kts` | Root Gradle build with AGP 8.2.2, Kotlin 1.9.20 plugins |
| `settings.gradle.kts` | Root settings with Google/Maven Central repos, includes `:app` |
| `app/build.gradle.kts` | App build: Compose, NDK, package `com.rigplus.app`, SDK 34 |
| `app/src/main/AndroidManifest.xml` | Minimal manifest with MainActivity launcher |
| `app/src/main/java/com/rigplus/app/MainActivity.kt` | Compose activity showing black screen with "RigPlus Phase 0" |
| `app/src/main/cpp/CMakeLists.txt` | Placeholder CMake config for native-lib.cpp |
| `app/src/main/cpp/native-lib.cpp` | Empty JNI placeholder for NDK support |

## Phase 1 - Completed Files

| File | Description |
|------|-------------|
| `app/src/main/cpp/CMakeLists.txt` | Updated with ThorVG FetchContent (v0.10.0), EGL/GLESv2 linking |
| `app/src/main/cpp/thorvg_renderer.hpp` | C++ header for ThorVG renderer JNI bridge |
| `app/src/main/cpp/thorvg_renderer.cpp` | ThorVG SwCanvas renderer with EGL surface, draws test rounded rect |
| `app/src/main/cpp/native-lib.cpp` | Simplified JNI entry point |
| `app/src/main/java/com/rigplus/app/CanvasRenderer.kt` | Kotlin JNI bridge class for native renderer |
| `app/src/main/java/com/rigplus/app/CanvasView.kt` | Compose CanvasView with SurfaceView/TextureView, 60fps render loop |
| `app/src/main/java/com/rigplus/app/MainActivity.kt` | Full Moho-style UI shell: TopBar, LeftPanel, Canvas, RightPanel, BottomPanel |

## Current Status
Phase 1 complete. The app now:
- Fetches and builds ThorVG v0.10.0 via CMake FetchContent
- Initializes EGL + ThorVG SwCanvas on SurfaceView surface
- Renders a green rounded rectangle test shape at 60fps
- Implements the full Moho-style responsive layout:
  - TopBar: Auto-weld/fill/stroke toggles (green when active), center canvas, undo/redo
  - LeftPanel: Docked tools (Draw, Fill, Layer, Camera, Workspace) with green active state
  - Center: ThorVG CanvasView filling available space
  - RightPanel: Slide-out panels for Style/Swatches/Layers
  - BottomPanel: Timeline with play, add keyframe, hide controls

Waiting for user confirmation to proceed to Phase 2.

## Notes
- Do NOT move to Phase 2 until user says "Phase complete, move to next Phase."
- User compiles via GitHub Actions on `ubuntu-latest`.
- Backup command at end of each phase: `rsync -av --exclude 'build' --exclude '.git' ~/RigPlus/ /sdcard/RigPlus_Source_Backup/`