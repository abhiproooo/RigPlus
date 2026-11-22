#include "thorvg_renderer.hpp"
#include <thorvg.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <EGL/egl.h>
#include <GLES2/gl2.h>
#include <mutex>

static std::mutex rendererMutex;
static tvg::Canvas* canvas = nullptr;
static tvg::SwCanvas* swCanvas = nullptr;
static EGLDisplay eglDisplay = EGL_NO_DISPLAY;
static EGLSurface eglSurface = EGL_NO_SURFACE;
static EGLContext eglContext = EGL_NO_CONTEXT;
static ANativeWindow* nativeWindow = nullptr;
static int surfaceWidth = 0;
static int surfaceHeight = 0;
static bool initialized = false;

bool initEGL(ANativeWindow* window) {
    if (eglDisplay != EGL_NO_DISPLAY) return true;

    eglDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (eglDisplay == EGL_NO_DISPLAY) return false;

    EGLint major, minor;
    if (!eglInitialize(eglDisplay, &major, &minor)) return false;

    const EGLint configAttribs[] = {
        EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
        EGL_RED_SIZE, 8,
        EGL_GREEN_SIZE, 8,
        EGL_BLUE_SIZE, 8,
        EGL_ALPHA_SIZE, 8,
        EGL_DEPTH_SIZE, 16,
        EGL_STENCIL_SIZE, 8,
        EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
        EGL_NONE
    };

    EGLConfig config;
    EGLint numConfigs;
    if (!eglChooseConfig(eglDisplay, configAttribs, &config, 1, &numConfigs) || numConfigs == 0) {
        return false;
    }

    const EGLint contextAttribs[] = {
        EGL_CONTEXT_CLIENT_VERSION, 2,
        EGL_NONE
    };

    eglContext = eglCreateContext(eglDisplay, config, EGL_NO_CONTEXT, contextAttribs);
    if (eglContext == EGL_NO_CONTEXT) return false;

    nativeWindow = window;
    eglSurface = eglCreateWindowSurface(eglDisplay, config, nativeWindow, nullptr);
    if (eglSurface == EGL_NO_SURFACE) return false;

    if (!eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) return false;

    return true;
}

void destroyEGL() {
    if (eglDisplay != EGL_NO_DISPLAY) {
        eglMakeCurrent(eglDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
        if (eglSurface != EGL_NO_SURFACE) {
            eglDestroySurface(eglDisplay, eglSurface);
            eglSurface = EGL_NO_SURFACE;
        }
        if (eglContext != EGL_NO_CONTEXT) {
            eglDestroyContext(eglDisplay, eglContext);
            eglContext = EGL_NO_CONTEXT;
        }
        eglTerminate(eglDisplay);
        eglDisplay = EGL_NO_DISPLAY;
    }
    nativeWindow = nullptr;
}

bool initThorVG(int width, int height) {
    std::lock_guard<std::mutex> lock(rendererMutex);

    if (initialized && canvas) return true;

    if (!tvg::Initializer::init(tvg::CanvasEngine::Sw, 0)) {
        return false;
    }

    swCanvas = tvg::SwCanvas::gen();
    if (!swCanvas) return false;

    if (!swCanvas->target(nullptr, width, height, tvg::SwCanvas::ARGB8888)) {
        return false;
    }

    canvas = tvg::Canvas::gen();
    if (!canvas) return false;

    if (!canvas->push(swCanvas)) {
        return false;
    }

    surfaceWidth = width;
    surfaceHeight = height;
    initialized = true;
    return true;
}

void destroyThorVG() {
    std::lock_guard<std::mutex> lock(rendererMutex);

    if (canvas) {
        canvas->clear(false);
        delete canvas;
        canvas = nullptr;
    }
    if (swCanvas) {
        delete swCanvas;
        swCanvas = nullptr;
    }
    tvg::Initializer::term(tvg::CanvasEngine::Sw);
    initialized = false;
}

void drawTestFrame() {
    std::lock_guard<std::mutex> lock(rendererMutex);

    if (!canvas || !swCanvas) return;

    canvas->clear(false);

    auto shape = tvg::Shape::gen();
    if (!shape) return;

    float cx = surfaceWidth / 2.0f;
    float cy = surfaceHeight / 2.0f;
    float size = std::min(surfaceWidth, surfaceHeight) * 0.3f;

    shape->appendRect(cx - size/2, cy - size/2, size, size, 20, 20);
    shape->fill(100, 200, 100, 255);
    shape->stroke(255, 255, 255, 255);
    shape->strokeWidth(4);

    canvas->push(std::move(shape));

    canvas->draw();
    swCanvas->sync();
}

void renderToWindow() {
    std::lock_guard<std::mutex> lock(rendererMutex);

    if (!swCanvas || !nativeWindow) return;

    ANativeWindow_Buffer buffer;
    if (ANativeWindow_lock(nativeWindow, &buffer, nullptr) != 0) return;

    uint32_t* dst = static_cast<uint32_t*>(buffer.bits);
    uint32_t* src = static_cast<uint32_t*>(swCanvas->target());

    int copyWidth = std::min(surfaceWidth, buffer.width);
    int copyHeight = std::min(surfaceHeight, buffer.height);

    for (int y = 0; y < copyHeight; ++y) {
        memcpy(dst + y * buffer.stride, src + y * surfaceWidth, copyWidth * sizeof(uint32_t));
    }

    ANativeWindow_unlockAndPost(nativeWindow);
}

extern "C" {

JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeInit(JNIEnv* env, jobject thiz, jobject surface, jint width, jint height) {
    ANativeWindow* window = ANativeWindow_fromSurface(env, surface);
    if (!window) return;

    std::lock_guard<std::mutex> lock(rendererMutex);

    if (!initEGL(window)) {
        ANativeWindow_release(window);
        return;
    }

    if (!initThorVG(width, height)) {
        destroyEGL();
        ANativeWindow_release(window);
        return;
    }

    surfaceWidth = width;
    surfaceHeight = height;
}

JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeResize(JNIEnv* env, jobject thiz, jint width, jint height) {
    std::lock_guard<std::mutex> lock(rendererMutex);

    surfaceWidth = width;
    surfaceHeight = height;

    if (swCanvas) {
        swCanvas->target(nullptr, width, height, tvg::SwCanvas::ARGB8888);
    }

    if (nativeWindow) {
        ANativeWindow_setBuffersGeometry(nativeWindow, width, height, WINDOW_FORMAT_RGBA_8888);
    }
}

JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeDraw(JNIEnv* env, jobject thiz) {
    drawTestFrame();
    renderToWindow();
}

JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeDestroy(JNIEnv* env, jobject thiz) {
    std::lock_guard<std::mutex> lock(rendererMutex);

    destroyThorVG();
    destroyEGL();

    if (nativeWindow) {
        ANativeWindow_release(nativeWindow);
        nativeWindow = nullptr;
    }
}

}