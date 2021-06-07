package com.hlw.moudle.opengl.activity;

import android.app.NativeActivity;

public class OpenGLNativeActivity extends NativeActivity {

    static {
        System.loadLibrary("lib_ndk_open_demo");
    }
}
