package com.hlw.demo.ui;

/**
 * BaseFragmentDialog Listener
 *
 * @author von
 */
public interface BaseFragmentDialogClickListener {
    /**
     * 点击取消按钮/X按钮
     */
    void dialogCancel(String dialogName);

    /**
     * 点击确定按钮
     */
    void dialogSure(String dialogName);

}