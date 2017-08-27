package com.yueban.architecturedemo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

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
    //LeakCanary Initialization
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);

    //App Initialization
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
