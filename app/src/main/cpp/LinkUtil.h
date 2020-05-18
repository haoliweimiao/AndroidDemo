//
// Created by Administrator on 2020/5/18.
//

#ifndef ANDROIDDEMO_LINKUTIL_H
#define ANDROIDDEMO_LINKUTIL_H

#include "esUtil.h"
#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>

char *getAssetsFile(AAssetManager *mgr, const char *filename);

GLuint LoadShader(GLenum type, const char *shaderSrc);

GLuint linkProgram(GLuint *vertexShader, GLuint *fragmentShader, GLint *linked);

GLuint LoadTexture(void *ioContext, char *fileName);

#endif //ANDROIDDEMO_LINKUTIL_H
