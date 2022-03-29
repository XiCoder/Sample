package com.hey.report;

import android.app.Application;

import com.hey.skin.SkinManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
