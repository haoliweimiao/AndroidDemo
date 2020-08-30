# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# set targetPlatform, will be passed in from gradle when this sample is completed
# openGL Supportability
# platform         status
#   (0 12)           ES2/ES3 not supported
#   [12, 18)         ES2 only; for ES3, app do dynamic load/detection
#                    this applies to the situations that:
#                        - minimum API is set to less than 18. In this case
#                          there is no ES3 header/lib support inside NDK
#                        - the built APK might be running on newer API phones
#                    with dynamic loading of ES3, the same APK would still be able
#                    to use ES3. Otherwise, app would stuck with ES2 even phone is
#                    is newer than the minimum API level (for example, Android-27 etc).
#
#   [18, 24)         ES2 & ES3
#                    If app is built to only support API-18 or later,
#                    set minimum api level to 18 is good enough, NDK supprts ES3
#                    with the right header and lib files. No need to use ES3 dynamic
#                    detection.
#   [24, infinite)   ES2 & ES3 & Vulkan

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -fno-rtti -fno-exceptions -Wall")

if (${ANDROID_PLATFORM_LEVEL} LESS 12)
    message(FATAL_ERROR "OpenGL 2 is not supported before API level 11 \
                      (currently using ${ANDROID_PLATFORM_LEVEL}).")
    return()
elseif (${ANDROID_PLATFORM_LEVEL} LESS 18)
    add_definitions("-DDYNAMIC_ES3")
    set(OPENGL_LIB GLESv2)
else ()
    set(OPENGL_LIB GLESv3)
endif (${ANDROID_PLATFORM_LEVEL} LESS 12)

include_directories( External/Include )
include_directories( Common/Include )
include_directories( stb_image )
include_directories( glm )

add_library(
        ndk_test
        SHARED
        LinkUtil.c
        OtherUtil.c
        DrawMain.cpp)

find_library(
        log-lib
        log )

find_library( OPENGLES3_LIBRARY GLESv2 "OpenGL ES v3.0 library")
find_library( EGL_LIBRARY EGL "EGL 1.4 library" )

#（1）引入native_app_glue框架头文件所在目录
include_directories(${ANDROID_NDK}/sources/android/native_app_glue
${COMMON_SOURCE_DIR})

#（2）将android_native_app_glue.c封装成app_glue静态库，否则在创建activity时，会crash
add_library(app_glue STATIC
${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c)

#（3）暴露ANativeActivity_onCreate方法，否则在创建activity时，会crash
set(CMAKE_SHARED_LINKER_FLAGS
"${CMAKE_SHARED_LINKER_FLAGS} -u ANativeActivity_onCreate")


#使用命令 ADD_SUBDIRECTORY 指明本项目包含一个子目录 src
ADD_SUBDIRECTORY( ${PROJECT_SOURCE_DIR}/test )
ADD_SUBDIRECTORY( ${PROJECT_SOURCE_DIR}/Common )
ADD_SUBDIRECTORY( ${PROJECT_SOURCE_DIR}/glm )

target_link_libraries( # Specifies the target library.
        ndk_test
        libtest
        Common
        -landroid
        glm
#        camera2ndk
#        mediandk
#        OPENGL_LIB
#        GLESv2
#        GLESv3
        ${jnigraphics}
        ${OPENGL_LIB}
        app_glue
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )