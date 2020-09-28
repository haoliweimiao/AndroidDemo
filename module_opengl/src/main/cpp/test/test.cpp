//
// Created by 毫厘微 on 2020/4/28.
//

#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring
JNICALL Java_com_hlw_demo_ndk_TestUtils_hello(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}