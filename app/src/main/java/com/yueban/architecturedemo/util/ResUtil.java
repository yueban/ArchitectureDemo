package com.yueban.architecturedemo.util;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatDrawableManager;
import com.yueban.architecturedemo.App;
import java.io.InputStream;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class ResUtil {
  private ResUtil() {
    throw new AssertionError();
  }

  private static Resources getResources() {
    return App.getApp().getResources();
  }

  @NonNull
  public static String getString(@StringRes int resId) throws Resources.NotFoundException {
    return getResources().getString(resId);
  }

  @NonNull
  public static String getString(@StringRes int resId, Object... formatArgs) throws Resources.NotFoundException {
    return getResources().getString(resId, formatArgs);
  }

  public static String[] getStringArray(@ArrayRes int id) throws Resources.NotFoundException {
    return getResources().getStringArray(id);
  }

  public static int[] getIntArray(@ArrayRes int id) throws Resources.NotFoundException {
    return getResources().getIntArray(id);
  }

  public static int getColor(@ColorRes int id) throws Resources.NotFoundException {
    return getResources().getColor(id);
  }

  @TargetApi(Build.VERSION_CODES.M)
  public static int getColor(@ColorRes int id, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
    return getResources().getColor(id, theme);
  }

  @Nullable
  public static ColorStateList getColorStateList(@ColorRes int id) throws Resources.NotFoundException {
    return getResources().getColorStateList(id);
  }

  @TargetApi(Build.VERSION_CODES.M)
  @Nullable
  public static ColorStateList getColorStateList(@ColorRes int id, @Nullable Resources.Theme theme)
      throws Resources.NotFoundException {
    return getResources().getColorStateList(id, theme);
  }

  @Nullable
  public static Drawable getDrawable(@DrawableRes int id) throws Resources.NotFoundException {
    if (id == 0) {
      return null;
    }
    return AppCompatResources.getDrawable(App.getApp(), id);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Nullable
  public static Drawable getDrawable(@DrawableRes int id, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
    return getResources().getDrawable(id, theme);
  }

  //google official bug: https://stackoverflow.com/questions/41150995/appcompatactivity-oncreate-can-only-be-called-from
  // -within-the-same-library-group
  @SuppressWarnings("RestrictedApi")
  public static Bitmap getBitmap(@DrawableRes int id) {
    if (id == 0) {
      return null;
    }
    Drawable drawable = AppCompatDrawableManager.get().getDrawable(App.getApp(), id);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      drawable = (DrawableCompat.wrap(drawable)).mutate();
    }

    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }

  public static float getDimension(@DimenRes int id) throws Resources.NotFoundException {
    return getResources().getDimension(id);
  }

  public static int getDimensionPixelOffset(@DimenRes int id) throws Resources.NotFoundException {
    return getResources().getDimensionPixelOffset(id);
  }

  public static int getDimensionPixelSize(@DimenRes int id) throws Resources.NotFoundException {
    return getResources().getDimensionPixelSize(id);
  }

  public static int getIdentifier(String name, String defType) {
    return getResources().getIdentifier(name, defType, App.getApp().getPackageName());
  }

  public static int getIdentifier(String name, String defType, String defPackage) {
    return getResources().getIdentifier(name, defType, defPackage);
  }

  public static InputStream openRawResource(@RawRes int id) throws Resources.NotFoundException {
    return getResources().openRawResource(id);
  }
}