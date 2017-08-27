package com.yueban.architecturedemo.util;

import android.support.annotation.IntDef;
import android.util.Log;
import com.yueban.architecturedemo.BuildConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public final class L {
  /**
   * isPrint: print switch, true will print. false not print
   */
  private static boolean isPrint = BuildConfig.DEBUG;
  private static String defaultTag = "Log";
  @LogLevel private static int sLocalLogLevel = LogLevel.NONE;

  private L() {
    throw new AssertionError();
  }

  public static void setLocalLogLevel(int logLevel) {
    sLocalLogLevel = logLevel;
  }

  public static void setTag(String tag) {
    defaultTag = tag;
  }

  public static int v(String m) {
    return v(defaultTag, m);
  }

  public static int d(String m) {
    return d(defaultTag, m);
  }

  public static int i(String m) {
    return i(defaultTag, m);
  }

  public static int w(String m) {
    return w(defaultTag, m);
  }

  public static int e(String m) {
    return e(defaultTag, m);
  }

  public static int e(Throwable tr) {
    return e(defaultTag, "", tr);
  }

  public static int e(String m, Throwable tr) {
    return e(defaultTag, m, tr);
  }

  /**
   * ******************** Log **************************
   */
  public static int v(String tag, String msg) {
    return isPrint && msg != null ? Log.v(tag, msg) : -1;
  }

  public static int d(String tag, String msg) {
    return isPrint && msg != null ? Log.d(tag, msg) : -1;
  }

  public static int i(String tag, String msg) {
    return isPrint && msg != null ? Log.i(tag, msg) : -1;
  }

  public static int w(String tag, String msg) {
    return isPrint && msg != null ? Log.w(tag, msg) : -1;
  }

  public static int e(String tag, String msg) {
    return isPrint && msg != null ? Log.e(tag, msg) : -1;
  }

  public static int e(String tag, String msg, Throwable tr) {
    return isPrint && msg != null ? Log.e(tag, msg, tr) : -1;
  }

  @IntDef({ LogLevel.VERBOSE, LogLevel.DEBUG, LogLevel.INFO, LogLevel.WARN, LogLevel.ERROR, LogLevel.ASSERT, LogLevel.NONE })
  @Retention(RetentionPolicy.SOURCE)
  public @interface LogLevel {
    int NONE = 0;
    int ASSERT = 1;
    int ERROR = 2;
    int WARN = 3;
    int INFO = 4;
    int DEBUG = 5;
    int VERBOSE = 6;
  }
}