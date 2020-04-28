package com.hlw.demo.ndk;

public class TestUtils {

    /**
     * 需要加载这个，才能使用里面的函数
     */
    static {
        System.loadLibrary("libtest");
    }

    /**
     * 这个和c++里面的函数名字，有对应关系。到时候调用安卓里面的函数hello，就能调用到C++里面的函数
     */
    public static native String hello();
}
