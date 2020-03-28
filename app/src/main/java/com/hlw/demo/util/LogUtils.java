package com.hlw.demo.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
    /**
     * 验证流程 tag
     */
    private static final int I = 0;
    private static final int V = 1;
    private static final int D = 2;
    private static final int E = 3;
    private static final int W = 4;

    private static String TAG = "Logger";
    private static boolean mIsShowLog = false;
    private static String SDCarePath = Environment.getExternalStorageDirectory().toString();
    private static String path = SDCarePath + "/cache/hlw";

    public static void initLog(boolean isShowLog) {
        mIsShowLog = isShowLog;
    }

    public static void e(String message) {
        print(E, TAG, message);
    }

    public static void e(String s, Throwable e) {
        print(E, s, e.getMessage());
    }

    public static void e(String tag, String message) {
        print(E, tag, message);
    }

    public static void e(String tag, String format, Object... args) {
        print(E, tag, String.format(format, args));

    }

    public static void d(String message) {
        print(D, TAG, message);
    }

    public static void d(String tag, String message) {
        print(D, tag, message);
    }

    public static void d(String tag, String format, Object... args) {
        print(D, tag, String.format(format, args));
    }

    public static void i(String message) {
        print(I, TAG, message);
    }

    public static void i(String tag, String message) {
        print(I, tag, message);
    }

    public static void i(String tag, String format, Object... args) {
        print(I, tag, String.format(format, args));
    }

    public static void v(String message) {
        print(V, TAG, message);
    }

    public static void v(String tag, String message) {
        print(V, tag, message);
    }

    public static void v(String tag, String format, Object... args) {
        print(V, tag, String.format(format, args));
    }

    public static void w(String message) {
        print(W, TAG, message);
    }

    public static void w(String tag, String message) {
        print(W, tag, message);
    }

    public static void w(String tag, String format, Object... args) {
        print(W, tag, String.format(format, args));
    }

    private static void print(int level, String tag, String message) {
        if (!mIsShowLog) {
            return;
        }
        switch (level) {
            case I:
                Log.i(formatTag(tag), formatValue(message));
                break;
            case V:
                Log.v(formatTag(tag), formatValue(message));
                break;
            case D:
                Log.d(formatTag(tag), formatValue(message));
                break;
            case E:
                Log.e(formatTag(tag), formatValue(message));
                break;
            case W:
                Log.w(formatTag(tag), formatValue(message));
                break;
            default:
                // do nothing
                break;
        }
    }

    /**
     * className-methodName:tag
     *
     * @param tag
     * @return
     */
    private static String formatTag(String tag) {
        return tag;
    }

    /**
     * value (lineNum)
     *
     * @param value
     * @return
     */
    private static String formatValue(String value) {
        return value + getLineNum();
    }

    /**
     * 获取 SimpleClassName
     *
     * @return
     */
    private static String getSimpleClassName() {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[6];
        String className = caller.getClassName();
        return className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * method name
     *
     * @return
     */
    private static String getMethodName() {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[6];
        return caller.getMethodName();
    }

    /**
     * line num
     *
     * @return
     */
    private static String getLineNum() {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stacks[6];
        return ".(" + caller.getFileName() + ":" + caller.getLineNumber() + ")";
    }

    public static void writeLog(boolean isOpenLog, String fileName, String str) {
        synchronized (LogUtils.class) {
            if (!isOpenLog) {
                return;
            }

            File file = new File(path + fileName);
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                long fileLength = file.length();
                if (fileLength > 3 * 1024 * 1024) {
                    file.delete();
                    file.createNewFile();
                }
                Date date = new Date();
                SimpleDateFormat dateFormat_min = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = dateFormat_min.format(date);

                String log = str + " " + time + "    ";

                methodBufferedWriter(file, log + "    \r\n");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeLog(String fileName, String str) {
        writeLog(mIsShowLog, fileName, str);
    }

    private static void methodBufferedWriter(File file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
