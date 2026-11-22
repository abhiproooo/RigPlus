cmake_minimum_required(VERSION 3.22.1)
project("rigplus")

# ThorVG via FetchContent
include(FetchContent)
FetchContent_Declare(
    thorvg
    GIT_REPOSITORY https://github.com/thorvg/thorvg.git
    GIT_TAG        v0.10.0
    GIT_SHALLOW    TRUE
)
FetchContent_MakeAvailable(thorvg)

add_library(
    native-lib
    SHARED
    native-lib.cpp
    thorvg_renderer.cpp
)

target_include_directories(
    native-lib
    PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${thorvg_SOURCE_DIR}/src
)

find_library(
    log-lib
    log
)

find_library(
    android-lib
    android
)

find_library(
    EGL-lib
    EGL
)

find_library(
    GLESv2-lib
    GLESv2
)

target_link_libraries(
    native-lib
    thorvg
    ${log-lib}
    ${android-lib}
    ${EGL-lib}
    ${GLESv2-lib}
)

target_include_directories(
    native-lib
    PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${thorvg_SOURCE_DIR}/src
)