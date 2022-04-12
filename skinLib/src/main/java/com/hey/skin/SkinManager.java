package com.hey.skin;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;

public class SkinManager extends Observable {

    private static SkinManager instance;

    private SkinLifeCycleCallback skinLifeCycleCallback;

    private Application application;

    private Executor executor;

    private WeakHashMap<Context, SkinViewInflater> viewInflaterMap = new WeakHashMap<>();

    private SkinManager(Application application) {
        this.application = application;
        //记录当前使用的皮肤
        copyAssetAndWrite(application, "app-debug.apk");
        SkinPreference.init(application);
        SkinResources.init(application);
        skinLifeCycleCallback = new SkinLifeCycleCallback();
        application.registerActivityLifecycleCallbacks(skinLifeCycleCallback);
        // 加载上次使用的皮肤
        loadSkin(application.getCacheDir() + "/app-debug.apk");
    }

    public void loadSkin(String skinPath) {


        if (TextUtils.isEmpty(skinPath)) {
            SkinResources.getInstance().reset();
            SkinPreference.getInstance().reset();
        } else {
            try {
                Resources appResource = application.getResources();
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                addAssetPath.invoke(assetManager, skinPath);
                Resources skinResource = new Resources(assetManager, appResource.getDisplayMetrics(), appResource.getConfiguration());
                PackageManager packageManager = application.getPackageManager();
                PackageInfo info = packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                String packageName = info.packageName;
                SkinResources.getInstance().applySkin(skinResource, packageName);
                // 储存SkinPath
                SkinPreference.getInstance().setSkinPath(skinPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 通知更新皮肤
            setChanged();
            notifyObservers(null);
        }

    }

    public static SkinManager getInstance() {
        return instance;
    }

    public static void init(Application application) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(application);
                }
            }
        }
    }

    public void putInflater(Context context, SkinViewInflater viewInflater) {
        if (!viewInflaterMap.containsKey(context)) {
            viewInflaterMap.put(context, viewInflater);
            addObserver(viewInflater);
        }
    }

    public void removeInflater(Context context) {
        SkinViewInflater viewInflater = viewInflaterMap.remove(context);
        if (viewInflater != null) {
            viewInflater.release();
        }
        deleteObserver(viewInflater);
    }


    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(Application application, String fileName) {
        try {
            Log.e("skin", "copy skin apk start");
            File cacheDir = application.getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return true;
                }
            }
            InputStream is = application.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            Log.e("skin", "copy skin apk success");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
