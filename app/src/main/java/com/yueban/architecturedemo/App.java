package com.yueban.architecturedemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class App extends Application {
    private static App sApp;

    public static App getApp() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        initDatabase();
        initStetho();
    }

    private void initDatabase() {
        FlowManager.init(this);
    }

    private void initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
