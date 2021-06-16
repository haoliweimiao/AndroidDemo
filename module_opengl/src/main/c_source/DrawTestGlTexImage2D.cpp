#include "esUtil.h"
#include "LinkUtil.h"
#include <android/log.h>
#include <android_native_app_glue.h>
#include <android/asset_manager_jni.h>
#include <android/asset_manager.h>
#include "OtherUtil.h"
#include <cstring>
#include "stb_image.h"

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "esUtil", __VA_ARGS__))
typedef struct {
    unsigned char *image;
    int width[1];
    int height[1];
    int channel[1];
} TextureImage;


typedef struct {
    // Handle to a program object
    GLuint programObject;

    GLuint uTextureMoveX;

    GLuint uPositionMoveX;

    GLuint textureId;

    GLuint uTexture;

    TextureImage image1;
    TextureImage image2;
} UserData;

GLuint VBO, VAO, EBO;


///
// Initialize the shader and program object
//
int Init(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;
    char *vShaderStr = getAssetsFile((AAssetManager *) esContext->assetManager,
                                     "glsl/vertex_image_move_x.glsl");
    esLogMessage("load vertex text file in android assets:\n%s\n", vShaderStr);

    char *fShaderStr = getAssetsFile((AAssetManager *) esContext->assetManager,
                                     "glsl/fragment_image_move_x.glsl");
    esLogMessage("load fragment text file in android assets:\n%s\n", vShaderStr);

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

    // set up vertex data (and buffer(s)) and configure vertex attributes
    // ------------------------------------------------------------------
    GLfloat vVertices[] = {
            // positions          // colors           // textureId coords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f  // top left
    };
    GLuint indices[] = {
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
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(GLfloat), (void *) 0);
    glEnableVertexAttribArray(0);

    //colors
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(GLfloat),
                          (void *) (3 * sizeof(GLfloat)));
    glEnableVertexAttribArray(1);

    //colors
    glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, 8 * sizeof(GLfloat),
                          (void *) (6 * sizeof(GLfloat)));
    glEnableVertexAttribArray(2);

    userData->textureId = loadTextureByMgr((AAssetManager *) esContext->assetManager,
                                           "image/container2.png");
//    texture2 = loadTextureByMgr((AAssetManager *) esContext->assetManager, "image/awesomeface.png");

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    // Set the viewport
    glViewport(0, 0, esContext->width, esContext->height);

    userData->uTextureMoveX = glGetUniformLocation(programObject, "uTextureMoveX");
    userData->uPositionMoveX = glGetUniformLocation(programObject, "uPositionMoveX");
    userData->uTexture = glGetUniformLocation(userData->programObject, "uTexture");

    userData->image1.image = loadImageData((AAssetManager *) esContext->assetManager,
                                           "image/container2.png",
                                           userData->image1.width, userData->image1.height,
                                           userData->image1.channel);

    userData->image2.image = loadImageData((AAssetManager *) esContext->assetManager,
                                           "image/container2_specular.png", userData->image2.width,
                                           userData->image2.height,
                                           userData->image2.channel);

    return TRUE;
}

GLfloat mMoveX = 0;
long mStartTime;

///
// Draw a triangle using the shader pair created in Init()
//
void Draw(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    // render
    // ------
    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);

    glUseProgram(userData->programObject);

    //bind textureId
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, userData->textureId);
    // Load the texture

//    int nrChannels = userData->image1.channel[0];
//    int width = userData->image1.width[0];
//    int height = userData->image1.height[0];
//    unsigned char *buffer = userData->image1.image;
//    if (nrChannels == 3)//rgb 适用于jpg图像
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
//                     buffer);//后面一个是RGBA
//    else if (nrChannels == 4)//rgba 适用于png图像
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
//                     buffer);//注意，两个都是RGBA
//    else
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, userData->image1.width[0],
                 userData->image1.height[0],
                 0, GL_RGBA, GL_UNSIGNED_BYTE,
                 userData->image1.image);//注意，两个都是RGBA
    esLogMessage("image1 length %d", strlen((const char *) userData->image1.image));
    glUniform1f(userData->uTextureMoveX, 0.0f);
    glUniform1f(userData->uPositionMoveX, 0.0f);
    glUniform1i(userData->uTexture, 0);

    if (mStartTime == 0) {
        mStartTime = currentTimeInMilliseconds();
    }
    long nowTime = currentTimeInMilliseconds();
    float textureMoveX = (float) (nowTime - mStartTime) / 3000.f;
    if (textureMoveX > 1.0f) {
        textureMoveX = textureMoveX - 1.0f;
        mStartTime = currentTimeInMilliseconds();
    } else {
        textureMoveX = textureMoveX * 1.0f;
    }

    glUniform1f(userData->uTextureMoveX, textureMoveX);
    glUniform1f(userData->uPositionMoveX, -textureMoveX);

    // draw our first triangle
    glBindVertexArray(VAO); // seeing as we only have a single VAO there's no need to bind it
    // every time, but we'll do so to keep things a bit more organized
    // glDrawArrays(GL_TRIANGLES, 0, 6);
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

    glUniform1f(userData->uPositionMoveX, 1.0f - textureMoveX);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, userData->image2.width[0],
                 userData->image2.height[0],
                 0, GL_RGBA, GL_UNSIGNED_BYTE,
                 userData->image2.image);
    esLogMessage("image2 length %d", strlen((const char *) userData->image2.image));
    glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
}

void Shutdown(ESContext *esContext) {
    UserData *userData = (UserData *) esContext->userData;

    stbi_image_free(userData->image1.image);
    stbi_image_free(userData->image2.image);

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
