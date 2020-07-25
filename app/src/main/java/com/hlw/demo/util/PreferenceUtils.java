package com.hlw.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.hlw.demo.DemoApplication;
import com.hlw.demo.config.SPFiles;
import com.hlw.demo.config.SPKeys;


/**
 * 对 SharePreference 的简单封装
 */
public final class PreferenceUtils {

    private PreferenceUtils() {
    }

    public static int getInt(@SPFiles String fileName, @SPKeys String key) {
        return getInt(fileName, key, 0);
    }

    public static int getInt(@SPFiles String fileName, @SPKeys String key, int defaultValue) {
        return get(fileName).getInt(key, defaultValue);
    }

    public static void put(@SPFiles String fileName, @SPKeys String key, int value) {
        getEditor(fileName).putInt(key, value).apply();
    }

    public static String getString(@SPFiles String fileName, @SPKeys String key) {
        return getString(fileName, key, null);
    }

    public static String getString(@SPFiles String fileName, @SPKeys String key, String defaultValue) {
        return get(fileName).getString(key, defaultValue);
    }

    public static long getLong(@SPFiles String fileName, @SPKeys String key) {
        return get(fileName).getLong(key, 0L);
    }

    public static long getLong(@SPFiles String fileName, @SPKeys String key, long defaultValue) {
        return get(fileName).getLong(key, defaultValue);
    }

    public static void put(@SPFiles String fileName, @SPKeys String key, String value) {
        getEditor(fileName).putString(key, value).apply();
    }

    public static void put(@SPFiles String fileName, @SPKeys String key, long value) {
        getEditor(fileName).putLong(key, value).apply();
    }

    public static boolean getBoolean(@SPFiles String fileName, @SPKeys String key) {
        return getBoolean(fileName, key, false);
    }

    public static boolean getBoolean(@SPFiles String fileName, @SPKeys String key, boolean defaultValue) {
        return get(fileName).getBoolean(key, defaultValue);
    }

    public static void put(@SPFiles String fileName, @SPKeys String key, boolean value) {
        getEditor(fileName).putBoolean(key, value).apply();
    }

    @SafeVarargs
    public static void putInt(@SPFiles String fileName, @Nullable Pair<String, Integer>... pairs) {
        if (pairs != null) {
            SharedPreferences.Editor editor = getEditor(fileName);
            for (Pair<String, Integer> pair : pairs) {
                if (pair == null || pair.second == null) {
                    continue;
                }
                editor.putInt(pair.first, pair.second);
            }
            editor.commit();
        }
    }

    @SafeVarargs
    public static void putBool(@SPFiles String fileName, @Nullable Pair<String, Boolean>... pairs) {
        if (pairs != null) {
            SharedPreferences.Editor editor = getEditor(fileName);
            for (Pair<String, Boolean> pair : pairs) {
                if (pair == null || pair.second == null) {
                    continue;
                }
                editor.putBoolean(pair.first, pair.second);
            }
            editor.commit();
        }
    }

    @SafeVarargs
    public static void putString(@SPFiles String fileName, @Nullable Pair<String, String>... pairs) {
        if (pairs != null) {
            SharedPreferences.Editor editor = getEditor(fileName);
            for (Pair<String, String> pair : pairs) {
                if (pair == null || pair.second == null) {
                    continue;
                }
                editor.putString(pair.first, pair.second);
            }
            editor.commit();
        }
    }

    public static void clearCommonCache() {
        SharedPreferences.Editor editor = getEditor(SPFiles.FILE_COMMON);
        editor.clear();
        editor.commit();
    }

    public static SharedPreferences get(@Nullable String name) {
        Context context = DemoApplication.getAppContext();
        if (TextUtils.isEmpty(name)) {
            name = SPFiles.FILE_COMMON;
        }
        if (context == null) {
            throw new NullPointerException("Context is null!");
        }
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(@SPFiles String name) {
        return get(name).edit();
    }
}
