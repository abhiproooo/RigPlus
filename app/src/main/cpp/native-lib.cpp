#include <jni.h>
#include <android/log.h>
#include <thorvg.h>

#include <cstdint>
#include <memory>
#include <vector>

namespace {

constexpr const char* kLogTag = "RigPlus";

std::unique_ptr<tvg::SwCanvas> gCanvas;
std::vector<uint32_t> gBuffer;
int gWidth = 0;
int gHeight = 0;
bool gThorvgInitialized = false;

void ensureThorvgInitialized() {
    if (!gThorvgInitialized) {
        tvg::Initializer::init(0);
        gThorvgInitialized = true;
    }
}

void releaseCanvas() {
    if (gCanvas) {
        gCanvas->clear();
        gCanvas.reset();
    }
}

void createCanvas() {
    releaseCanvas();

    if (gWidth <= 0 || gHeight <= 0) {
        return;
    }

    gBuffer.assign(static_cast<size_t>(gWidth) * static_cast<size_t>(gHeight), 0x00000000u);

    gCanvas = tvg::SwCanvas::gen();
    if (!gCanvas) {
        __android_log_print(ANDROID_LOG_ERROR, kLogTag, "Failed to create ThorVG SwCanvas");
        return;
    }

    if (gCanvas->target(gBuffer.data(),
                        static_cast<uint32_t>(gWidth),
                        static_cast<uint32_t>(gWidth),
                        static_cast<uint32_t>(gHeight),
                        tvg::ColorSpace::ARGB8888) != tvg::Result::Success) {
        __android_log_print(ANDROID_LOG_ERROR, kLogTag, "Failed to set ThorVG canvas target");
        gCanvas.reset();
        return;
    }

    gCanvas->clear();
}

}  // namespace

extern "C" JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeInit(JNIEnv* /* env */,
                                               jobject /* this */,
                                               jobject /* surface */,
                                               jint width,
                                               jint height) {
    ensureThorvgInitialized();

    gWidth = width;
    gHeight = height;
    createCanvas();
}

extern "C" JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeResize(JNIEnv* /* env */,
                                                 jobject /* this */,
                                                 jint width,
                                                 jint height) {
    gWidth = width;
    gHeight = height;
    createCanvas();
}

extern "C" JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeDraw(JNIEnv* /* env */,
                                               jobject /* this */) {
    if (!gCanvas) {
        return;
    }

    gCanvas->clear();

    auto background = tvg::Shape::gen();
    background->appendRect(0.0f, 0.0f,
                           static_cast<float>(gWidth),
                           static_cast<float>(gHeight));
    background->fill(255, 255, 255, 255);

    auto testShape = tvg::Shape::gen();
    testShape->appendRect(static_cast<float>(gWidth) * 0.25f,
                          static_cast<float>(gHeight) * 0.25f,
                          static_cast<float>(gWidth) * 0.5f,
                          static_cast<float>(gHeight) * 0.5f);
    testShape->fill(76, 175, 80, 255);

    gCanvas->push(std::move(background));
    gCanvas->push(std::move(testShape));

    gCanvas->draw();
    gCanvas->sync();
}

extern "C" JNIEXPORT void JNICALL
Java_com_rigplus_app_CanvasRenderer_nativeDestroy(JNIEnv* /* env */,
                                                  jobject /* this */) {
    releaseCanvas();
    gBuffer.clear();

    if (gThorvgInitialized) {
        tvg::Initializer::term();
        gThorvgInitialized = false;
    }

    gWidth = 0;
    gHeight = 0;
}
