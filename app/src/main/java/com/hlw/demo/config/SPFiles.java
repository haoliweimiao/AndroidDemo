package com.hlw.demo.config;

import android.support.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * share preference file names
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@StringDef
public @interface SPFiles {
    /**
     * 公共存储文件夹
     */
    String FILE_COMMON = "file_common";
}
