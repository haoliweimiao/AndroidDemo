//
// Created by Administrator on 2020/5/18.
//

#ifndef ANDROIDDEMO_LINKUTIL_H
#define ANDROIDDEMO_LINKUTIL_H

#ifdef __cplusplus
extern "C" {
#endif

#include "esUtil.h"
#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>

char *getAssetsFile(AAssetManager *mgr, const char *filename);

GLuint loadShader(GLenum type, const char *shaderSrc);

GLuint linkProgram(GLuint *vertexShader, GLuint *fragmentShader, GLint *linked);

GLuint loadTexture(void *ioContext, char *fileName);

GLuint loadTextureByMgr(AAssetManager *mgr, const char *filename);

#ifdef __cplusplus
}
#endif
#endif //ANDROIDDEMO_LINKUTIL_H

