package com.hey.skin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class SkinResources {

    private volatile static SkinResources instance;

    private boolean isDefaultSkin;

    private Resources mAppResources;
    private Resources mSkinResources;
    private String mSkinPackageName;


    private SkinResources(Context context) {
        mAppResources = context.getResources();
    }

    public static SkinResources init(Context context) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null) {
                    instance = new SkinResources(context);
                }
            }
        }
        return instance;
    }


    public static SkinResources getInstance() {
        return instance;
    }

    public void applySkin(Resources resources, String packageName) {
        mSkinResources = resources;
        mSkinPackageName = packageName;
        isDefaultSkin = TextUtils.isEmpty(packageName) || resources == null;
    }

    public void reset() {
        mSkinResources = null;
        mSkinPackageName = null;
        isDefaultSkin = true;
    }

    /**
     * 获取R.color.xxx的名字
     * 根据名字和类型获取皮肤包中的ID
     *
     * @param resId
     * @return
     */
    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        // 获取原res的名称和type
        String resName = mAppResources.getResourceEntryName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        int skinId = mSkinResources.getIdentifier(resName, resType, mSkinPackageName);
        return skinId;
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(skinId);
        }
        return mSkinResources.getColor(skinId);
    }
    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(skinId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(skinId);
        }
        return mSkinResources.getDrawable(skinId);
    }


    public Object getBackground(int resId) {
        String typeName = mAppResources.getResourceTypeName(resId);
        if ("color".equals(typeName)) {
            return getColor(resId);
        }
        return getDrawable(resId);
    }

}
