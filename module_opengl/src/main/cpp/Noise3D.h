// Noise3D.h
//
// Generates a 3D noise
//
#ifndef ANDROIDDEMO_NOISE3D_H
#define ANDROIDDEMO_NOISE3D_H

#ifdef __cplusplus
extern "C" {
#endif
#include "esUtil.h"
unsigned int Create3DNoiseTexture ( int textureSize, float frequency );
#ifdef __cplusplus
}
#endif
#endif //ANDROIDDEMO_NOISE3D_H