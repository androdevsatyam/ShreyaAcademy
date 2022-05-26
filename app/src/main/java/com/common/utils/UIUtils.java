package com.common.utils;

import android.view.Window;

import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * @Authoer Dharmesh
 * @Date 12-03-2022
 * <p>
 * Information
 **/
public class UIUtils {

    /**
     * Show keyboard for backward API Supported
     */
    public static void showKeyboard(Window window) {
        new WindowInsetsControllerCompat(window, window.getDecorView()).show(WindowInsetsCompat.Type.ime());
    }

    /**
     * Hide keyboard for backward API Supported
     */
    public static void hideKeyboard( Window window) {
        new WindowInsetsControllerCompat(window, window.getDecorView()).hide(WindowInsetsCompat.Type.ime());
    }

}
