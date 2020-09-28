//
// Created by Administrator on 2020/5/19.
//

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

unsigned int VBO, VAO, EBO;

///
// Initialize the shader and program object
//
int Init(ESContext *esContext) {
    UserData *userData = esContext->userData;
    char *vShaderStr = getAssetsFile(esContext->platformData, "glsl/rectangle/vertex.glsl");
    esLogMessage("load vertex text file in android assets:\n%s\n", vShaderStr);

    char *fShaderStr = getAssetsFile(esContext->platformData, "glsl/rectangle/fragment.glsl");
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

    glDeleteShader(fragmentShader);
    glDeleteShader(vertexShader);

    // Store the program object
    userData->programObject = programObject;

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    GLfloat vVertices[] = {
            0.5f, 0.5f, 0.0f, // top right
            0.5f, -0.5f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f  // top left
    };
    GLint indices[] = {
            // note that we start from 0!
            0, 1, 3, // first Triangle
            1, 2, 3  // second Triangle
    };

    // Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);

    glGenVertexArrays(1, &VAO);
    glGenBuffers(1, &VBO);
    glGenBuffers(1, &EBO);

    glBindVertexArray(VAO);

    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vVertices), vVertices, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void *) 0);
    glEnableVertexAttribArray(0);

    // note that this is allowed, the call to glVertexAttribPointer registered VBO
    // as the vertex attribute's bound vertex buffer object so afterwards we can
    // safely unbind
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    // remember: do NOT unbind the EBO while a VAO is active as the bound element
    // buffer object IS stored in the VAO; keep the EBO bound.
    // glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    // You can unbind the VAO afterwards so other VAO calls won't accidentally
    // modify this VAO, but this rarely happens. Modifying other VAOs requires a
    // call to glBindVertexArray anyways so we generally don't unbind VAOs (nor
    // VBOs) when it's not directly necessary.
    glBindVertexArray(0);

    return TRUE;
}

///
// Draw a triangle using the shader pair created in Init()
//
void Draw(ESContext *esContext) {
    UserData *userData = esContext->userData;

    // render
    // ------
    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    // draw our first triangle
    glUseProgram(userData->programObject);
    glBindVertexArray(
            VAO); // seeing as we only have a single VAO there's no need to bind it
    // every time, but we'll do so to keep things a bit more organized
    // glDrawArrays(GL_TRIANGLES, 0, 6);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
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
