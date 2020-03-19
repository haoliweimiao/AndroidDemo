package com.hlw.demo.base;

public interface ZKBaseFragmentDialogClickLisenter {
    /**
     * 点击取消按钮/X按钮
     */
    void dialogCancle(String dialogName);

    /**
     * 点击确定按钮
     */
    void dialogSure(String dialogName);

}