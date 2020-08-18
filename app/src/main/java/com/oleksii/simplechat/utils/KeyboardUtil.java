package com.oleksii.simplechat.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public interface KeyboardUtil {

    static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    static void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) view
                .getContext()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
