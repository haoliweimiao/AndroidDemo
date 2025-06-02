package com.hlw.demo.ui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Demon
 * @version 1.0
 * @date 2016年2月24日 下午2:24:23
 * 活动(Activity)管理类
 */
public class ActivityCollector {

    /**
     * Application activities in stack
     */
    private static List<Activity> activities = new ArrayList<>();

    /**
     * get application activities in stack
     * @return activities
     */
    public static List<Activity> getActivities() {
        return activities;
    }

    /**
     * 添加Activity
     *
     * @param activity activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 删除Activity
     *
     * @param activity activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有Activity
     */
    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭所有Activity 除当前的Activity
     */
    public static void finishAllNoThis(Activity thisActivity) {
        for (Activity activity : activities) {
            if (activity != thisActivity) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 关闭当前的Activity
     */
    public static void finishThisActivity(Class thisActivity) {
        for (Activity activity : activities) {
            if (activity.getComponentName().toString().contains(thisActivity.getSimpleName())) {
                activity.finish();
                break;
            }
        }
    }
}
