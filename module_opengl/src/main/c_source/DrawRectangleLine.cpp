#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>
#include <glm/ext.hpp>
#include <stb_image.h>
#include <malloc.h>
#include <cstdlib>

#include "esUtil.h"
#include "LinkUtil.h"
#include "OtherUtil.h"

typedef struct {
    // Handle to a program object
    GLuint programObject;

    GLuint vaoIds[1];
    GLuint vboIds[1];
    GLuint eboIds[1];

    GLint modelLoc;
    GLint viewLoc;
    GLint projectLoc;
    GLint uMoveXPosLoc;
    GLint uMoveYPosLoc;

} UserData;

typedef struct {
    int top;
    int right;
    int bottom;
    int left;
} Rect;

GLfloat vVertices[] = {0.5f, 0.5f,
                       0.5f, -0.5f,
                       -0.5f, -0.5f,
                       -0.5f, 0.5f,
};

GLuint vIndex[] = {
        0, 1,
        1, 2,
        2, 3,
        3, 0
};

///
// Initialize the shader and program object
//
int Init(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    char vShaderStr[4096] = {0};
    char fShaderStr[4096] = {0};

    readAssetsFile((AAssetManager *) esContext->assetManager,
                   "glsl/vertex_draw_rect.glsl", vShaderStr);

    readAssetsFile((AAssetManager *) esContext->assetManager,
                   "glsl/fragment_draw_rect.glsl", fShaderStr);


    GLuint vertexShader;
    GLuint fragmentShader;
    GLuint programObject;
    GLint linked;

    // Load the vertex/fragment shaders
    vertexShader = loadShader(GL_VERTEX_SHADER, vShaderStr);
    fragmentShader = loadShader(GL_FRAGMENT_SHADER, fShaderStr);

    // Create the program object
    programObject = linkProgram(&vertexShader, &fragmentShader, &linked);

    // Store the program object
    userData->programObject = programObject;

    // retrieve the matrix uniform locations
    userData->modelLoc = glGetUniformLocation(userData->programObject, "model");
    userData->viewLoc = glGetUniformLocation(userData->programObject, "view");
    userData->projectLoc = glGetUniformLocation(userData->programObject, "projection");
    userData->uMoveXPosLoc = glGetUniformLocation(userData->programObject, "uMoveXPos");
    userData->uMoveYPosLoc = glGetUniformLocation(userData->programObject, "uMoveYPos");

    glGenVertexArrays(1, userData->vaoIds);
    glGenBuffers(1, userData->vboIds);
    glGenBuffers(1, userData->eboIds);

    glBindVertexArray(userData->vaoIds[0]);

    glBindBuffer(GL_ARRAY_BUFFER, userData->vboIds[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vVertices), vVertices, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, userData->eboIds[0]);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(vIndex), vIndex, GL_STATIC_DRAW);


    glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(GLfloat),
                          (const void *) 0);
    glEnableVertexAttribArray(0);


    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindVertexArray(0);

    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    return TRUE;
}

///
// Draw a triangle using the shader pair created in Init()
//
void Draw(ESContext *esContext) {
    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    UserData *userData = (UserData *) esContext->userData;

    int screenWidth = esContext->width;
    int screenHeight = esContext->height;

    glUseProgram(userData->programObject);

    glLineWidth(2);

//    Rect rect[3];
//    for (int i = 0; i < 3; i++) {
//        int rectWidth = rand() % 200;
//        int rectHeight = rand() % 200;
//        if (rectWidth < 100) {
//            rectWidth = 100;
//        }
//        if (rectHeight < 100) {
//            rectHeight = 100;
//        }
//        int startX = rand() % screenWidth;
//        int startY = rand() % screenHeight;
//        rect[i].left = startX;
//        rect[i].right = startX + rectWidth;
//        rect[i].top = startY;
//        rect[i].bottom = startX + rectHeight;
//    }


    // create transformations
    glm::mat4 model = glm::mat4(
            1.0f); // make sure to initialize matrix to identity matrix first
    glm::mat4 view = glm::mat4(1.0f);
    glm::mat4 projection = glm::mat4(1.0f);
//    model = glm::rotate(model, glm::radians(-55.0f), glm::vec3(1.0f, 0.0f, 0.0f));


    view = glm::translate(view, glm::vec3(0.0f, 0.0f, -4.0f));
    projection = glm::perspective(glm::radians(45.0f),
                                  (float) screenWidth / (float) screenHeight, 1.f,
                                  100.0f);
    // pass them to the shaders (3 different ways)
    // glUniformMatrix4fv(modelLoc, 1, GL_FALSE, &model[0][0]);
    glUniformMatrix4fv(userData->modelLoc, 1, GL_FALSE, glm::value_ptr(model));
    // glUniformMatrix4fv(viewLoc, 1, GL_FALSE, &view[0][0]);
    glUniformMatrix4fv(userData->viewLoc, 1, GL_FALSE, glm::value_ptr(view));
    // note: currently we set the projection matrix each frame,
    // but since the projection matrix rarely changes it's often
    // best practice to set it outside the main loop only once.
    //  glUniformMatrix4fv(projectLoc, 1, GL_FALSE, &projection[0][0]);
    glUniformMatrix4fv(userData->projectLoc, 1, GL_FALSE, glm::value_ptr(projection));

    glUniform1f(userData->uMoveXPosLoc, 0.0f);
    glBindVertexArray(userData->vaoIds[0]);
    glDrawElements(GL_LINES, 8, GL_UNSIGNED_INT, 0);
}

void Shutdown(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    glDeleteProgram(userData->programObject);
}

int esMain(ESContext *esContext) {
    esContext->userData = malloc(sizeof(UserData));

    esCreateWindow(esContext, "Hello Triangle", 320, 240, ES_WINDOW_RGB);

    // Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);

    if (!Init(esContext)) {
        return GL_FALSE;
    }

    esRegisterShutdownFunc(esContext, Shutdown);
    esRegisterDrawFunc(esContext, Draw);

    return GL_TRUE;
}
