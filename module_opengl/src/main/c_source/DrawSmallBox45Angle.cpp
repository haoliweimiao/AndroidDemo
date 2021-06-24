#include "esUtil.h"
#include "LinkUtil.h"
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
    char *vShaderStr = "#version 300 es\n"
                       "layout (location = 0) in vec3 aPos;\n"
                       "layout (location = 1) in vec2 aTexCoord;\n"
                       "\n"
                       "out vec2 TexCoord;\n"
                       "\n"
                       "uniform mat4 model;\n"
                       "uniform mat4 view;\n"
                       "uniform mat4 projection;\n"
                       "\n"
                       "void main()\n"
                       "{\n"
                       "\tgl_Position = projection * view * model * vec4(aPos, 1.0);\n"
                       "\tTexCoord = vec2(aTexCoord.x, aTexCoord.y);\n"
                       "}";

    char *fShaderStr = "#version 300 es\n"
                       "precision mediump float;\n"
                       "out vec4 FragColor;\n"
                       "\n"
                       "in vec2 TexCoord;\n"
                       "\n"
                       "// texture samplers\n"
                       "uniform sampler2D texture1;\n"
                       "uniform sampler2D texture2;\n"
                       "\n"
                       "void main()\n"
                       "{\n"
                       "\t// linearly interpolate between both textures (80% container, 20% awesomeface)\n"
                       "\tFragColor = mix(texture(texture1, TexCoord), texture(texture2, TexCoord), 0.2);\n"
                       "}";

    GLuint vertexShader;
    GLuint fragmentShader;
    GLuint programObject;
    GLint linked;

    // Load the vertex/fragment shaders
    vertexShader = loadShader(GL_VERTEX_SHADER, vShaderStr);
    fragmentShader = loadShader(GL_FRAGMENT_SHADER, fShaderStr);

    // Create the program object
    programObject = linkProgram(&vertexShader, &fragmentShader, &linked);

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    glDeleteShader(fragmentShader);
    glDeleteShader(vertexShader);

    // Store the program object
    userData->programObject = programObject;

    float vertices[] = {
            // positions          // texture coords
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f  // top left
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
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

    // position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat), (void *) 0);
    glEnableVertexAttribArray(0);
    // texture coord attribute
    glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat),
                          (void *) (3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    texture1 = loadTextureByMgr((AAssetManager *) esContext->assetManager, "image/container2.png");
    texture2 = loadTextureByMgr((AAssetManager *) esContext->assetManager, "image/awesomeface.png");

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    // Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);

    return TRUE;
}

///
// Draw a triangle using the shader pair created in Init()
//
void Draw(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    // render
    // ------
    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    //bind texture
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texture1);

    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, texture2);

    glUniform1i(glGetUniformLocation(userData->programObject, "texture1"), 0);
    glUniform1i(glGetUniformLocation(userData->programObject, "texture2"), 1);


    // draw our first triangle
    glUseProgram(userData->programObject);

    // create transformations
    glm::mat4 model = glm::mat4(1.0f); // make sure to initialize matrix to identity matrix first
    glm::mat4 view = glm::mat4(1.0f);
    glm::mat4 projection = glm::mat4(1.0f);
    model = glm::rotate(model, glm::radians(-45.0f), glm::vec3(1.0f, 0.0f, 0.0f));
    view = glm::translate(view, glm::vec3(0.0f, 0.0f, -3.0f));
    projection = glm::perspective(glm::radians(45.0f),
                                  (float) esContext->width / (float) esContext->height, 0.1f,
                                  100.0f);
    // retrieve the matrix uniform locations
    unsigned int modelLoc = glGetUniformLocation(userData->programObject, "model");
    unsigned int viewLoc = glGetUniformLocation(userData->programObject, "view");
    unsigned int projectionLoc = glGetUniformLocation(userData->programObject, "projection");
    // pass them to the shaders (3 different ways)
    glUniformMatrix4fv(modelLoc, 1, GL_FALSE, glm::value_ptr(model));
    glUniformMatrix4fv(viewLoc, 1, GL_FALSE, &view[0][0]);
    // note: currently we set the projection matrix each frame, but since the projection matrix rarely changes it's often best practice to set it outside the main loop only once.
    glUniformMatrix4fv(projectionLoc, 1, GL_FALSE, glm::value_ptr(projection));

    glBindVertexArray(VAO); // seeing as we only have a single VAO there's no need to bind it
    // every time, but we'll do so to keep things a bit more organized
    // glDrawArrays(GL_TRIANGLES, 0, 6);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
}

void Shutdown(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

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
