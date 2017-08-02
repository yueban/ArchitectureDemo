package com.yueban.architecturedemo;

import android.app.Application;
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
    }

    private void initDatabase() {
        FlowManager.init(this);
    }
}
