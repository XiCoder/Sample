package com.hey.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.collection.SimpleArrayMap;

import java.lang.reflect.Constructor;
import java.util.Observable;
import java.util.Observer;

public class SkinViewInflater extends AppCompatViewInflater implements Observer {

    private static final Class<?>[] sConstructorSignature = new Class<?>[]{
            Context.class, AttributeSet.class};

    private final Object[] mConstructorArgs = new Object[2];

    private static final SimpleArrayMap<String, Constructor<? extends View>> sConstructorMap =
            new SimpleArrayMap<>();

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    SkinAttribute skinAttribute;

    public SkinViewInflater(SkinAttribute skinAttribute) {
        this.skinAttribute = skinAttribute;
    }

    @Nullable
    @Override
    protected View createView(Context context, String name, AttributeSet attrs) {
        View view = createViewFromTag(context, name, attrs);
        lookSkin(context, view, attrs);
        return view;
    }


    private void lookSkin(Context context, View view, AttributeSet attrs) {
        skinAttribute.look(view, attrs);
        SkinManager.getInstance().putInflater(context, this);
    }


    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }
        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;
            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < sClassPrefixList.length; i++) {
                    final View view = createViewByPrefix(context, name, sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createViewByPrefix(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createViewByPrefix(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);
        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = Class.forName(
                        prefix != null ? (prefix + name) : name,
                        false,
                        context.getClassLoader()).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }


    @NonNull
    @Override
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        AppCompatTextView appCompatTextView = new AppCompatTextView(context, attrs);
        lookSkin(context, appCompatTextView, attrs);
        return appCompatTextView;
    }

    @NonNull
    @Override
    protected AppCompatImageView createImageView(Context context, AttributeSet attrs) {
        AppCompatImageView appCompatImageView = new AppCompatImageView(context, attrs);
        lookSkin(context, appCompatImageView, attrs);
        return appCompatImageView;
    }

    @NonNull
    @Override
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        AppCompatButton appCompatButton = new AppCompatButton(context, attrs);
        lookSkin(context, appCompatButton, attrs);
        return appCompatButton;
    }

    @NonNull
    @Override
    protected AppCompatEditText createEditText(Context context, AttributeSet attrs) {
        AppCompatEditText appCompatEditText = new AppCompatEditText(context, attrs);
        lookSkin(context, appCompatEditText, attrs);
        return appCompatEditText;
    }

    @NonNull
    @Override
    protected AppCompatSpinner createSpinner(Context context, AttributeSet attrs) {
        AppCompatSpinner appCompatSpinner = new AppCompatSpinner(context, attrs);
        lookSkin(context, appCompatSpinner, attrs);
        return appCompatSpinner;
    }

    @NonNull
    @Override
    protected AppCompatImageButton createImageButton(Context context, AttributeSet attrs) {
        AppCompatImageButton appCompatImageButton = new AppCompatImageButton(context, attrs);
        lookSkin(context, appCompatImageButton, attrs);
        return appCompatImageButton;
    }

    @NonNull
    @Override
    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attrs) {
        AppCompatCheckBox appCompatCheckBox = new AppCompatCheckBox(context, attrs);
        lookSkin(context, appCompatCheckBox, attrs);
        return appCompatCheckBox;
    }

    @NonNull
    @Override
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        AppCompatRadioButton appCompatRadioButton = new AppCompatRadioButton(context, attrs);
        lookSkin(context, appCompatRadioButton, attrs);
        return appCompatRadioButton;
    }

    @NonNull
    @Override
    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attrs) {
        AppCompatCheckedTextView appCompatCheckedTextView = new AppCompatCheckedTextView(context, attrs);
        lookSkin(context, appCompatCheckedTextView, attrs);
        return appCompatCheckedTextView;
    }

    @NonNull
    @Override
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attrs) {
        AppCompatAutoCompleteTextView appCompatAutoCompleteTextView = new AppCompatAutoCompleteTextView(context, attrs);
        lookSkin(context, appCompatAutoCompleteTextView, attrs);
        return appCompatAutoCompleteTextView;
    }

    @NonNull
    @Override
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        AppCompatMultiAutoCompleteTextView appCompatMultiAutoCompleteTextView = new AppCompatMultiAutoCompleteTextView(context, attrs);
        lookSkin(context, appCompatMultiAutoCompleteTextView, attrs);
        return appCompatMultiAutoCompleteTextView;
    }

    @NonNull
    @Override
    protected AppCompatRatingBar createRatingBar(Context context, AttributeSet attrs) {
        AppCompatRatingBar appCompatRatingBar = new AppCompatRatingBar(context, attrs);
        lookSkin(context, appCompatRatingBar, attrs);
        return appCompatRatingBar;
    }

    @NonNull
    @Override
    protected AppCompatSeekBar createSeekBar(Context context, AttributeSet attrs) {
        AppCompatSeekBar appCompatSeekBar = new AppCompatSeekBar(context, attrs);
        lookSkin(context, appCompatSeekBar, attrs);
        return appCompatSeekBar;
    }

    @NonNull
    @Override
    protected AppCompatToggleButton createToggleButton(Context context, AttributeSet attrs) {
        AppCompatToggleButton appCompatToggleButton = new AppCompatToggleButton(context, attrs);
        lookSkin(context, appCompatToggleButton, attrs);
        return appCompatToggleButton;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (skinAttribute != null) {
            skinAttribute.applySkin();
        }
    }

    public void release(){
        skinAttribute.release();
    }
}
