package com.hey.skin;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

public class SkinAttribute {


    private static final List<String> mAttributes = new ArrayList<>();


    private List<SkinView> mSkinViews = new ArrayList<>();


    static {
        mAttributes.add("background");
        mAttributes.add("src");
        mAttributes.add("textColor");
        mAttributes.add("drawableLeft");
        mAttributes.add("drawableTop");
        mAttributes.add("drawableRight");
        mAttributes.add("drawableBottom");
    }

    // 查找View中需要换肤的属性
    public void look(View view, AttributeSet attrs) {
        List<SkinPair> mSkinPairs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            if (mAttributes.contains(attributeName)) {
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int resId = 0;
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    resId = ThemeUtils.getResId(view.getContext(), new int[]{attrId})[0];
                } else {
                    resId = Integer.parseInt(attributeValue.substring(1));
                }
                SkinPair skinPair = new SkinPair(attributeName, resId);
                mSkinPairs.add(skinPair);
            }
        }
        if (!mSkinPairs.isEmpty() || view instanceof SkinSupport) {
            SkinView skinView = new SkinView(view, mSkinPairs);
            applySkin();
            mSkinViews.add(skinView);
        }

    }


    public void applySkin() {
        for (SkinView mSkinView : mSkinViews) {
            mSkinView.applySkin();
        }
    }

    public void release() {
        mSkinViews.clear();
    }


    static class SkinView {

        View view;

        List<SkinPair> skinPairList;

        public SkinView(View view, List<SkinPair> skinPairList) {
            this.view = view;
            this.skinPairList = skinPairList;
        }


        public void applySkin() {
            applySkinSupport();
            for (SkinPair skinPair : skinPairList) {
                switch (skinPair.attributeName) {
                    case "background":
                        Object background = SkinResources.getInstance().getBackground(skinPair.resId);
                        if (background instanceof Integer) {
                            view.setBackgroundColor((Integer) background);
                        }

                        if (background instanceof Drawable) {
                            ViewCompat.setBackground(view, (Drawable) background);
                        }
                        break;
                    case "src":
                        background = SkinResources.getInstance().getBackground(skinPair.resId);
                        ImageView imageView = (ImageView) view;
                        if (background instanceof Integer) {

                            imageView.setImageDrawable(new ColorDrawable((Integer) background));
                        }

                        if (background instanceof Drawable) {
                            imageView.setImageDrawable((Drawable) background);
                        }
                    case "textColor":
                        TextView textView = (TextView) view;
                        textView.setTextColor(SkinResources.getInstance().getColorStateList(skinPair.resId));
                }
            }

        }


        private void applySkinSupport() {
            if (view instanceof SkinSupport) {
                ((SkinSupport) view).applySkin();
            }
        }

    }


    static class SkinPair {

        String attributeName;

        int resId;

        public SkinPair(String attributeName, int resId) {
            this.attributeName = attributeName;
            this.resId = resId;
        }
    }
}
