# RigPlus Project

## Overview
RigPlus is an Android vector rigging and animation tool inspired by Moho. The architecture follows the "Holy Trinity":
- **Canvas/Renderer:** ThorVG (C++) for OpenGL ES abstraction and path rendering
- **Heavy Lifting:** C++ for vector math, bone matrices, and event-driven calculations
- **UI:** Kotlin & Jetpack Compose

## Phased Execution Plan

- [x] **Phase 0: GitHub Actions & Base Build Setup** — COMPLETE
- [ ] Phase 1: Canvas, UI Shell & ThorVG Setup
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

## Current Status
Phase 0 complete. The app compiles an empty black screen with "RigPlus Phase 0" text. No ThorVG or Canvas code has been written yet. Waiting for user confirmation to proceed to Phase 1.

## Notes
- Do NOT move to Phase 1 until user says "Phase complete, move to next Phase."
- User compiles via GitHub Actions on `ubuntu-latest`.
- Backup command at end of each phase: `rsync -av --exclude 'build' --exclude '.git' ~/RigPlus/ /sdcard/RigPlus_Source_Backup/`