#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_rigplus_app_MainActivity_stringFromJNI(
    JNIEnv* env,
    jobject /* this */) {
    return env->NewStringUTF("RigPlus Phase 0 NDK");
}