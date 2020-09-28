#include "esUtil.h"
#include "LinkUtil.h"
#include "OtherUtil.h"
#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>
#include <glm/ext.hpp>
#include <stb_image.h>

typedef struct {
    // Handle to a program object
    GLuint programObject;

} UserData;

GLuint VBO, VAO, EBO;

GLuint texture1, texture2;

///
// Initialize the shader and program object
//
int Init(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;
    const char *vShaderStr = getAssetsFile((AAssetManager *) esContext->platformData,
                                           "glsl/ndk/vertex_perspective_smile_box.glsl");

    const char *fShaderStr = getAssetsFile((AAssetManager *) esContext->platformData,
                                           "glsl/ndk/fragment_perspective_smile_box.glsl");

    GLuint vertexShader;
    GLuint fragmentShader;
    GLuint programObject;
    GLint linked;

    glEnable(GL_DEPTH_TEST);

    // Load the vertex/fragment shaders
    vertexShader = loadShader(GL_VERTEX_SHADER, vShaderStr);
    fragmentShader = loadShader(GL_FRAGMENT_SHADER, fShaderStr);

    // Create the program object
    programObject = linkProgram(&vertexShader, &fragmentShader, &linked);

    glDeleteShader(fragmentShader);
    glDeleteShader(vertexShader);

    // Store the program object
    userData->programObject = programObject;

    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    float vVertices[] = {
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 0.0f,

            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,

            -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, -0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, -0.5f, 0.5f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f, 0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f, 0.0f, 1.0f,

            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f,
            0.5f, 0.5f, -0.5f, 1.0f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f, 0.0f, 1.0f
    };

    unsigned int indices[] = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    };

    glGenVertexArrays(1, &VAO);
    glGenBuffers(1, &VBO);
    glGenBuffers(1, &EBO);

    glBindVertexArray(VAO);

    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vVertices), vVertices, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

    //positions
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat), (void *) 0);
    glEnableVertexAttribArray(0);

    //colors
    glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat),
                          (void *) (3 * sizeof(GLfloat)));
    glEnableVertexAttribArray(1);

    stbi_set_flip_vertically_on_load(
            true); // tell stb_image.h to flip loaded texture's on the y-axis.
    texture1 = loadTextureByMgr((AAssetManager *) esContext->platformData, "image/awesomeface.png");
    texture2 = loadTextureByMgr((AAssetManager *) esContext->platformData, "image/container2.png");

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    // Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);


    return TRUE;
}


float show = 0.0f;

///
// Draw a triangle using the shader pair created in Init()
//
void Draw(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    // render
    // ------
    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    //bind texture
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texture1);

    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, texture2);

    glUseProgram(userData->programObject);

    glUniform1i(glGetUniformLocation(userData->programObject, "uTexture1"), 0);
    glUniform1i(glGetUniformLocation(userData->programObject, "uTexture2"), 1);

    if (show >= 360.0f) {
        show = 0.0f;
    }

    show += 0.8f;
    esLogMessage("time %f", show);

    // create transformations
    glm::mat4 model = glm::mat4(1.0f); // make sure to initialize matrix to identity matrix first
    glm::mat4 view = glm::mat4(1.0f);
    glm::mat4 projection = glm::mat4(1.0f);
//    model = glm::rotate(model, glm::radians(-55.0f), glm::vec3(1.0f, 0.0f, 0.0f));

    model = glm::rotate(model, glm::radians(show),
                        glm::vec3(0.5f, 0.9f, 0.0f));

    view = glm::translate(view, glm::vec3(0.0f, 0.0f, -4.0f));
    projection = glm::perspective(glm::radians(45.0f),
                                  (float) esContext->width / (float) esContext->height, 0.99f,
                                  100.0f);
    // retrieve the matrix uniform locations
    GLint modelLoc = glGetUniformLocation(userData->programObject, "model");
    GLint viewLoc = glGetUniformLocation(userData->programObject, "view");
    GLint projectLoc = glGetUniformLocation(userData->programObject, "projection");
    // pass them to the shaders (3 different ways)
    // glUniformMatrix4fv(modelLoc, 1, GL_FALSE, &model[0][0]);
    glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));
    // glUniformMatrix4fv(viewLoc, 1, GL_FALSE, &view[0][0]);
    glUniformMatrix4fv(viewLoc, 1, GL_FALSE, glm::value_ptr(view));
    // note: currently we set the projection matrix each frame,
    // but since the projection matrix rarely changes it's often
    // best practice to set it outside the main loop only once.
    //  glUniformMatrix4fv(projectLoc, 1, GL_FALSE, &projection[0][0]);
    glUniformMatrix4fv(projectLoc, 1, GL_FALSE, glm::value_ptr(projection));

    glBindVertexArray(VAO);
    glDrawArrays(GL_TRIANGLES, 0, 36);

    eglSwapBuffers(esContext->eglDisplay, esContext->eglSurface);
}

void Shutdown(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    glDeleteProgram(userData->programObject);
}

int esMain(ESContext *esContext) {
    esContext->userData = malloc(sizeof(UserData));

    // 必须在创建窗口时，使窗口支持 ES_WINDOW_DEPTH，否则 GL_DEPTH_TEST 无效
    esCreateWindow(esContext, "Demo", esContext->width, esContext->height,
                   ES_WINDOW_RGB | ES_WINDOW_DEPTH | ES_WINDOW_STENCIL);

    glEnable(GL_DEPTH_TEST);

    if (!Init(esContext)) {
        return GL_FALSE;
    }

    esRegisterShutdownFunc(esContext, Shutdown);
    esRegisterDrawFunc(esContext, Draw);

    return GL_TRUE;
}
