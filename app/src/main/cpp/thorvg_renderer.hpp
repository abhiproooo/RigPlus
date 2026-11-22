#ifndef THORVG_RENDERER_HPP
#define THORVG_RENDERER_HPP

#ifdef __cplusplus
extern "C" {
#endif

void nativeInit(void* surface, int width, int height);
void nativeResize(int width, int height);
void nativeDraw();
void nativeDestroy();

#ifdef __cplusplus
}
#endif

#endif // THORVG_RENDERER_HPP