package com.hlw.demo.config;

import android.support.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * share preference keys
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@StringDef
public @interface SPKeys {
    /**
     * 应用启动时间
     */
    String COMMON_APP_START_TIME_LONG = "common_app_start_time_long";
}
