package com.hey.skin;

import android.app.Application;

public class SkinPreference {

    private volatile static SkinPreference instance;

    public static void init(Application application) {
        if (instance==null){
            synchronized (SkinPreference.class){
                if (instance==null){
                    instance = new SkinPreference();
                }
            }
        }
    }


    public static SkinPreference getInstance() {
        return instance;
    }


    public String getSkinPath(){

        return "./assets/app-debug.apk";
    }

    public void reset(){

    }

    public void setSkinPath(String skinPath) {
    }
}
