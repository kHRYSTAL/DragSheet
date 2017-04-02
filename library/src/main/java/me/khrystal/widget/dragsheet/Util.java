package me.khrystal.widget.dragsheet;

import android.content.Context;
import android.view.WindowManager;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 17/4/3
 * update time:
 * email: 723526676@qq.com
 */

public class Util {
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }
}
