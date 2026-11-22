#include <jni.h>
#include "thorvg_renderer.hpp"

extern "C" JNIEXPORT jstring JNICALL
Java_com_rigplus_app_MainActivity_stringFromJNI(
    JNIEnv* env,
    jobject /* this */) {
    return env->NewStringUTF("RigPlus Phase 1 NDK + ThorVG");
}