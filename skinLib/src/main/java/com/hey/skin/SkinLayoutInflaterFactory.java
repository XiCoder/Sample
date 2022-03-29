package com.hey.skin;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SkinLayoutInflaterFactory implements LayoutInflater.Factory2, Observer {

    private static final String TAG = "SkinLayoutFactory";

    private static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    // 记录对应View的构造函数
    private static final HashMap<String, Constructor<? extends View>> mConstructorMap =
            new HashMap<String, Constructor<? extends View>>();


    private String[] classPrefixes = new String[]{
            "android.widget.",
            "android.webkit.",
            "android.app.",
            "android.view."
    };

    private SkinAttribute skinAttribute;

    private Activity activity;


    public SkinLayoutInflaterFactory(Activity activity) {
        this.activity = activity;
        skinAttribute = new SkinAttribute();
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View view = createSDKView(name, context, attrs);
        if (view == null) {
            view = createView(name, context, attrs);
        }
        if (view != null) {
            skinAttribute.look(view, attrs);
        }
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        View view = createSDKView(name, context, attrs);
        if (view == null) {
            view = createView(name, context, attrs);
        }
        if (view != null) {
            skinAttribute.look(view, attrs);
        }
        return view;
    }


    private View createSDKView(String name, Context context, AttributeSet attrs) {
        // 如果包含.则说明不是Android自带的View，可能是自定义View或者Support库中的View
        if (-1 != name.indexOf('.')) {
            return null;
        }
        // 如果是Android自带的View就在name之前拼上：android.widget.前缀尝试通过反射去创建
        for (int i = 0; i < classPrefixes.length; i++) {
            return createView(classPrefixes[i] + name, context, attrs);
        }
        return null;
    }


    private View createView(String name, Context context, AttributeSet attrs) {
        Log.e(TAG, "createView " + name);
        Constructor<? extends View> constructor = findConstructor(context, name);
        try {
            return constructor.newInstance(context, attrs);
        } catch (Exception e) {
            Log.e(TAG, "createView " + name + " failed cause by " + e.getLocalizedMessage());
        }
        return null;
    }


    private Constructor<? extends View> findConstructor(Context context, String name) {
        Constructor<? extends View> constructor = mConstructorMap.get(name);
        if (constructor == null) {
            try {
                Class<? extends View> clazz = context.getClassLoader().loadClass(name).asSubclass(View.class);
                constructor = clazz.getConstructor(mConstructorSignature);
                mConstructorMap.put(name, constructor);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        }
        return constructor;

    }

    @Override
    public void update(Observable o, Object arg) {
        ThemeUtils.setStatusBarColor(activity);
        skinAttribute.applySkin();
    }
}
