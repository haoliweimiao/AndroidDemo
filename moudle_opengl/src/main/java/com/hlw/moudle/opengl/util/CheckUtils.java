package com.hlw.moudle.opengl.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;

/**
 * @author hlw
 */
public class CheckUtils {

    public static boolean isSupportOpenGl2(Context context) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        final ConfigurationInfo info = manager.getDeviceConfigurationInfo();
        return info.reqGlEsVersion > 0x20000 ||
                (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && Build.FINGERPRINT.startsWith("generic")) ||
                Build.FINGERPRINT.startsWith("unknown") ||
                Build.MODEL.contains("google_sdk") ||
                Build.MODEL.contains("Emulator") ||
                //支持夜神模拟器
                Build.MODEL.contains("SM-G955N") ||
                Build.MODEL.contains("Android SDK built for x86");
    }


    public static boolean isSupportOpenGl3(Context context) {
        final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        ConfigurationInfo info = manager.getDeviceConfigurationInfo();
        return ( info.reqGlEsVersion >= 0x30000 );
    }
}
