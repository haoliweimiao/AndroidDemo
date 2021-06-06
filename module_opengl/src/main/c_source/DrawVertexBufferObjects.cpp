#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>
#include <glm/ext.hpp>
#include <stb_image.h>
#include <malloc.h>

#include "esUtil.h"
#include "LinkUtil.h"
#include "OtherUtil.h"

#define VBO_SIZE 2

typedef struct {
    // Handle to a program object
    GLuint programObject;

    // VertexBufferObject Ids
    GLuint vboIds[VBO_SIZE];

    // x-offset uniform location
    GLuint offsetLoc;

} UserData;

#define VERTEX_POS_SIZE       3 // x, y and z
#define VERTEX_COLOR_SIZE     4 // r, g, b, and a

#define VERTEX_POS_INDX       0
#define VERTEX_COLOR_INDX     1

///
// Initialize the shader and program object
//
int Init(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    char vShaderStr[4096] = {0};
    char fShaderStr[4096] = {0};

    readAssetsFile((AAssetManager *) esContext->assetManager,
                   "glsl/vertex_vertex_buffer_objects.glsl", vShaderStr);

    readAssetsFile((AAssetManager *) esContext->assetManager,
                   "glsl/fragment_vertex_buffer_objects.glsl", fShaderStr);


    GLuint vertexShader;
    GLuint fragmentShader;
    GLuint programObject;
    GLint linked;

    // Load the vertex/fragment shaders
    vertexShader = loadShader(GL_VERTEX_SHADER, vShaderStr);
    fragmentShader = loadShader(GL_FRAGMENT_SHADER, fShaderStr);

    // Create the program object
    programObject = linkProgram(&vertexShader, &fragmentShader, &linked);

    userData->offsetLoc = glGetUniformLocation(programObject, "u_offset");

    if (programObject == 0) {
        return GL_FALSE;
    }

    // Store the program object
    userData->programObject = programObject;
    userData->vboIds[0] = 0;
    userData->vboIds[1] = 0;

    glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    return TRUE;
}

//
// vertices   - pointer to a buffer that contains vertex
//              attribute data
// vtxStride  - stride of attribute data / vertex in bytes
// numIndices - number of indices that make up primitive
//              drawn as triangles
// indices    - pointer to element index buffer.
//
void DrawPrimitiveWithoutVBOs(GLfloat *vertices,
                              GLint vtxStride,
                              GLint numIndices,
                              GLushort *indices) {
    GLfloat *vtxBuf = vertices;

    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    glEnableVertexAttribArray(VERTEX_POS_INDX);
    glEnableVertexAttribArray(VERTEX_COLOR_INDX);

    glVertexAttribPointer(VERTEX_POS_INDX,
                          VERTEX_POS_SIZE,
                          GL_FLOAT,
                          GL_FALSE,
                          vtxStride,
                          vtxBuf);

    vtxBuf += VERTEX_POS_SIZE;
    glVertexAttribPointer(VERTEX_COLOR_INDX,
                          VERTEX_COLOR_SIZE,
                          GL_FLOAT,
                          GL_FALSE,
                          vtxStride,
                          vtxBuf);

    glDrawElements(GL_TRIANGLES,
                   numIndices,
                   GL_UNSIGNED_SHORT,
                   indices);

    glDisableVertexAttribArray(VERTEX_POS_INDX);
    glDisableVertexAttribArray(VERTEX_COLOR_INDX);
}

void DrawPrimitiveWithVBOs(ESContext *esContext,
                           GLint numVertices, GLfloat *vtxBuf,
                           GLint vtxStride, GLint numIndices,
                           GLushort *indices) {
    UserData *userData = (UserData *) esContext->userData;
    GLuint offset = 0;

    // vboIds[0] - used to store vertex attribute data
    // vboIds[1] - used to store element indices
    if (userData->vboIds[0] == 0 && userData->vboIds[1] == 0) {
        // Only allocate on the first draw
        glGenBuffers(VBO_SIZE, userData->vboIds);

        glBindBuffer(GL_ARRAY_BUFFER, userData->vboIds[0]);
        glBufferData(GL_ARRAY_BUFFER, vtxStride * numVertices,
                     vtxBuf, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, userData->vboIds[1]);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(GLfloat) * numIndices,
                     indices, GL_STATIC_DRAW);
    }

    glBindBuffer(GL_ARRAY_BUFFER, userData->vboIds[0]);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, userData->vboIds[1]);

    glEnableVertexAttribArray(VERTEX_POS_INDX);
    glEnableVertexAttribArray(VERTEX_COLOR_INDX);

    glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_POS_SIZE,
                          GL_FLOAT, GL_FALSE, vtxStride,
                          (const void *) offset);

    offset += VERTEX_POS_SIZE * sizeof(GLfloat);
    glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                          GL_FLOAT, GL_FALSE, vtxStride,
                          (const void *) offset);

    glDrawElements(GL_TRIANGLES, numIndices, GL_UNSIGNED_SHORT, 0);

    glDisableVertexAttribArray(VERTEX_POS_INDX);
    glDisableVertexAttribArray(VERTEX_COLOR_INDX);


    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
}

///
// Draw a triangle using the shader pair created in Init()
//
void Draw(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;
    // 3 vertices, with (x,y,z) ,(r, g, b, a) per-vertex
    GLfloat vertices[3 * (VERTEX_POS_SIZE + VERTEX_COLOR_SIZE)] =
            {
                    -0.5f, 0.5f, 0.0f,        // v0
                    1.0f, 0.0f, 0.0f, 1.0f,  // c0
                    -1.0f, -0.5f, 0.0f,        // v1
                    0.0f, 1.0f, 0.0f, 1.0f,  // c1
                    0.0f, -0.5f, 0.0f,        // v2
                    0.0f, 0.0f, 1.0f, 1.0f,  // c2
            };
    // Index buffer data
    GLushort indices[3] = {0, 1, 2};

    // Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);

    // Clear the color buffer
    glClear(GL_COLOR_BUFFER_BIT);

    // Use the program object
    glUseProgram(userData->programObject);

    glUniform1f(userData->offsetLoc, 0.0f);

    DrawPrimitiveWithoutVBOs(vertices,
                             sizeof(GLfloat) * (VERTEX_POS_SIZE + VERTEX_COLOR_SIZE),
                             3,
                             indices);

    // Offset the vertex position so both can be seen
    glUniform1f(userData->offsetLoc, 1.0f);

    DrawPrimitiveWithVBOs(esContext,
                          3,
                          vertices,
                          sizeof(GLfloat) * (VERTEX_POS_SIZE + VERTEX_COLOR_SIZE),
                          3, indices);
}

void Shutdown(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    glDeleteProgram(userData->programObject);
}

int esMain(ESContext *esContext) {
    esContext->userData = malloc(sizeof(UserData));

    esCreateWindow(esContext, "Hello Triangle", 320, 240, ES_WINDOW_RGB);

    if (!Init(esContext)) {
        esLogMessage("error! init failed!");
        return GL_FALSE;
    }

    esRegisterShutdownFunc(esContext, Shutdown);
    esRegisterDrawFunc(esContext, Draw);

    return GL_TRUE;
}
