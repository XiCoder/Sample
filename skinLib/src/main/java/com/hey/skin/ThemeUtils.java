package com.hey.skin;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;

public class ThemeUtils {


    private static int[] COLOR_PRIMARY_DARK_ATTRS = {androidx.appcompat.R.attr.colorPrimaryDark};

    private static int[] STATUS_BAR_ATTRS = {android.R.attr.statusBarColor, android.R.attr.navigationBarColor};


    public static int[] getResId(Context context, int[] attrs) {
        int[] resIds = new int[attrs.length];
        TypedArray a = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < attrs.length; i++) {
            resIds[i] = a.getResourceId(i, 0);
        }
        a.recycle();
        return resIds;
    }

    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        // 获得statusBarColor 和 navigationBarColor
        int[] resId = getResId(activity, STATUS_BAR_ATTRS);
        int statusBarColorId = resId[0];
        int navigationBarColorId = resId[1];
        if (statusBarColorId != 0) {
            int color = SkinResources.getInstance().getColor(statusBarColorId);
            activity.getWindow().setStatusBarColor(color);
        } else {
            int darkResId = getResId(activity, COLOR_PRIMARY_DARK_ATTRS)[0];
            if (darkResId != 0) {
                int color = SkinResources.getInstance().getColor(darkResId);
                activity.getWindow().setStatusBarColor(color);
            }
        }
        // set NavigationBarColor
        if (navigationBarColorId!=0){
            int color = SkinResources.getInstance().getColor(navigationBarColorId);
            activity.getWindow().setNavigationBarColor(color);
        }

    }


}
