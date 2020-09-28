#include "esUtil.h"
#include "LinkUtil.h"
#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "esUtil", __VA_ARGS__))

typedef struct {
    // Handle to a program object
    GLuint programObject;

} UserData;

///
// Initialize the shader and program object
//
int Init(ESContext *esContext) {
    UserData *userData = esContext->userData;
    char *vShaderStr = getAssetsFile(esContext->platformData, "glsl/triangle/vertex_3_0.glsl");
    esLogMessage("load vertex text file in android assets:\n%s\n", vShaderStr);

    char *fShaderStr = getAssetsFile(esContext->platformData, "glsl/triangle/fragment_3_0.glsl");
    esLogMessage("load fragment text file in android assets:\n%s\n", vShaderStr);

    GLuint vertexShader;
    GLuint fragmentShader;
    GLuint programObject;
    GLint linked;

    // Load the vertex/fragment shaders
    vertexShader = LoadShader(GL_VERTEX_SHADER, vShaderStr);
    fragmentShader = LoadShader(GL_FRAGMENT_SHADER, fShaderStr);

    // Create the program object
    programObject = linkProgram(&vertexShader, &fragmentShader, &linked);

    // Store the program object
    userData->programObject = programObject;

    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    return TRUE;
}

///
// Draw a triangle using the shader pair created in Init()
//
void Draw(ESContext *esContext) {
    UserData *userData = esContext->userData;
    GLfloat vVertices[] = {0.0f, 0.5f, 0.0f,
                           -0.5f, -0.5f, 0.0f,
                           0.5f, -0.5f, 0.0f
    };

    // Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);

    // Clear the color buffer
    glClear(GL_COLOR_BUFFER_BIT);

    // Use the program object
    glUseProgram(userData->programObject);

    // Load the vertex data
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, vVertices);
    glEnableVertexAttribArray(0);

    glDrawArrays(GL_TRIANGLES, 0, 3);
}

void Shutdown(ESContext *esContext) {
    UserData *userData = esContext->userData;

    glDeleteProgram(userData->programObject);
}

int esMain(ESContext *esContext) {
    esContext->userData = malloc(sizeof(UserData));

    esCreateWindow(esContext, "Hello Triangle", 320, 240, ES_WINDOW_RGB);

    if (!Init(esContext)) {
        return GL_FALSE;
    }

    esRegisterShutdownFunc(esContext, Shutdown);
    esRegisterDrawFunc(esContext, Draw);

    return GL_TRUE;
}
