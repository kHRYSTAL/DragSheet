package me.khrystal.widget.dragsheet;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {

    private static Screen screen = null;

    /**
     * 根据分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得屏幕尺寸
     * @param context
     * @return
     */
    public static Point getScreenSize(Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }

    public static int getHeight(Context context) {
        if (screen == null) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(dm);
            screen = new Screen(dm.widthPixels, dm.heightPixels);
        }
        if (screen != null) {
            return screen.widthPixels;
        }
        return 0;
    }

    public static class Screen {
        public int widthPixels;
        public int heightPixels;

        public Screen() {
        }

        public Screen(int widthPixels, int heightPixels) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
        }

        @Override
        public String toString() {
            return "(" + widthPixels + "," + heightPixels + ")";
        }

    }
}