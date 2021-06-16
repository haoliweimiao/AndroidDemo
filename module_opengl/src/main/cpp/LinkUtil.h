//
// Created by Administrator on 2020/5/18.
//

#ifndef ANDROIDDEMO_LINKUTIL_H
#define ANDROIDDEMO_LINKUTIL_H

#ifdef __cplusplus
extern "C" {
#endif

#include "esUtil.h"


GLuint loadShader(GLenum type, const char *shaderSrc);

GLuint linkProgram(const GLuint *vertexShader, const GLuint *fragmentShader, GLint *linked);

GLuint loadTexture(void *ioContext, char *fileName);

#ifdef ANDROID

#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>

char *getAssetsFile(AAssetManager *mgr, const char *filename);

void readAssetsFile(AAssetManager *mgr, const char *filename, char *ret);

GLuint loadTextureByMgr(AAssetManager *mgr, const char *filename);

unsigned char *
loadImageData(AAssetManager *mgr, const char *filename, int width[],
              int height[], int nrChannels[]);

#endif // ANDROID


#ifdef __cplusplus
}
#endif
#endif //ANDROIDDEMO_LINKUTIL_H

