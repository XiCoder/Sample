package com.hey.skin;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.LayoutInflaterCompat;

import java.lang.reflect.Field;
import java.util.Observable;

public class SkinLifeCycleCallback implements Application.ActivityLifecycleCallbacks {

    private Observable mObservable;

    private ArrayMap<Activity, SkinLayoutInflaterFactory> mLayoutFactoryList = new ArrayMap<>();

    public SkinLifeCycleCallback(Observable mObservable) {
        this.mObservable = mObservable;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity);
        forceSetFactory2(layoutInflater,skinLayoutInflaterFactory);
        mObservable.addObserver(skinLayoutInflaterFactory);
        mLayoutFactoryList.put(activity, skinLayoutInflaterFactory);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        SkinLayoutInflaterFactory skinLayoutInflaterFactory = mLayoutFactoryList.remove(activity);
        mObservable.deleteObserver(skinLayoutInflaterFactory);
    }

    private static void forceSetFactory2(LayoutInflater inflater,LayoutInflater.Factory2 factory2) {
        Class<LayoutInflaterCompat> compatClass = LayoutInflaterCompat.class;
        Class<LayoutInflater> inflaterClass = LayoutInflater.class;
        try {
            Field sCheckedField = compatClass.getDeclaredField("sCheckedField");
            sCheckedField.setAccessible(true);
            sCheckedField.setBoolean(inflater, false);
            Field mFactory = inflaterClass.getDeclaredField("mFactory");
            mFactory.setAccessible(true);
            Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
            mFactory2.setAccessible(true);
            mFactory2.set(inflater, factory2);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
