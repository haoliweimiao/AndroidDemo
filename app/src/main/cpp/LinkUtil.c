//
// Created by Administrator on 2020/5/18.
//

#include "LinkUtil.h"

#define STB_IMAGE_IMPLEMENTATION

#include "stb_image.h"

char *getAssetsFile(AAssetManager *mgr, const char *filename) {
    //open file
    AAsset *assetFile = AAssetManager_open(mgr, filename, AASSET_MODE_BUFFER);
    //get file length
    size_t fileLength = (size_t) AAsset_getLength(assetFile);
    char *fileData = (char *) malloc(fileLength);
    //read file data
    AAsset_read(assetFile, fileData, fileLength);
    //the data has been copied to dataBuffer2, so , close it
    AAsset_close(assetFile);

    // +1 because of '\0' at the end
    char *data = malloc(strlen(fileData) + 1);
    strcpy(data, fileData);
    //free malloc
    free(fileData);

    return data;
}

///
// Create a shader object, load the shader source, and
// compile the shader.
//
GLuint loadShader(GLenum type, const char *shaderSrc) {
    GLuint shader;
    GLint compiled;

    // Create the shader object
    shader = glCreateShader(type);

    if (shader == 0) {
        return 0;
    }

    // Load the shader source
    glShaderSource(shader, 1, &shaderSrc, NULL);

    // Compile the shader
    glCompileShader(shader);

    // Check the compile status
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);

    if (!compiled) {
        GLint infoLen = 0;

        glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);

        if (infoLen > 1) {
            char *infoLog = malloc(sizeof(char) * infoLen);

            glGetShaderInfoLog(shader, infoLen, NULL, infoLog);
            esLogMessage("Error compiling shader:\n%s\n", infoLog);

            free(infoLog);
        }

        glDeleteShader(shader);
        return 0;
    }

    return shader;

}

GLuint linkProgram(GLuint *vertexShader, GLuint *fragmentShader, GLint *linked) {
    // Create the program object
    GLuint programObject = glCreateProgram();

    if (programObject == 0) {
        return 0;
    }

    glAttachShader(programObject, *vertexShader);
    glAttachShader(programObject, *fragmentShader);

    // Link the program
    glLinkProgram(programObject);

    // Check the link status
    glGetProgramiv(programObject, GL_LINK_STATUS, linked);

    if (!linked) {
        GLint infoLen = 0;

        glGetProgramiv(programObject, GL_INFO_LOG_LENGTH, &infoLen);

        if (infoLen > 1) {
            char *infoLog = malloc(sizeof(char) * infoLen);

            glGetProgramInfoLog(programObject, infoLen, NULL, infoLog);
            esLogMessage("Error linking program:\n%s\n", infoLog);

            free(infoLog);
        }

        glDeleteProgram(programObject);
        return FALSE;
    }

    return programObject;
}

///
// Load texture from disk
//
GLuint loadTexture(void *ioContext, char *fileName) {
    int width, height;

    char *buffer = esLoadTGA(ioContext, fileName, &width, &height);
    GLuint texId;

    if (buffer == NULL) {
        esLogMessage("Error loading (%s) image.\n", fileName);
        return 0;
    }


    glGenTextures(1, &texId);
    glBindTexture(GL_TEXTURE_2D, texId);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    free(buffer);

    return texId;
}

/**
 * Load texture from assets file
 */
GLuint loadTextureByMgr(AAssetManager *mgr, const char *filename) {
    int width, height, nrChannels;
    // 打开 Asset 文件夹下的文件
    AAsset *pathAsset = AAssetManager_open(mgr, filename, AASSET_MODE_UNKNOWN);
    // 得到文件的长度
    off_t assetLength = AAsset_getLength(pathAsset);
    // 得到文件对应的 Buffer
    unsigned char *fileData = (unsigned char *) AAsset_getBuffer(pathAsset);
    // stb_image 的方法，从内存中加载图片
    unsigned char *buffer = stbi_load_from_memory(fileData, assetLength, &width, &height,
                                                  &nrChannels, 0);

    GLuint texId;

    if (buffer == NULL) {
        esLogMessage("Error loading (%s) image.\n", filename);
        return 0;
    }

    glGenTextures(1, &texId);
    glBindTexture(GL_TEXTURE_2D, texId);

//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    // set the texture wrapping parameters
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
                    GL_REPEAT);    // set texture wrapping to GL_REPEAT (default wrapping method)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    // set texture filtering parameters
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    if (nrChannels == 3)//rgb 适用于jpg图像
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                     buffer);//后面一个是RGBA
    else if (nrChannels == 4)//rgba 适用于png图像
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
                     buffer);//注意，两个都是RGBA
    else
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);

    glGenerateMipmap(GL_TEXTURE_2D);
//    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
//    glGenerateMipmap(GL_TEXTURE_2D);

    stbi_image_free(buffer);

    return texId;
}