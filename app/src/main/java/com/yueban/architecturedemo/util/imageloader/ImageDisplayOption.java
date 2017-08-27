package com.yueban.architecturedemo.util.imageloader;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import com.yueban.architecturedemo.R;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class ImageDisplayOption {
  /**
   * 头像加载选项
   */
  public final static ImageDisplayOption AVATAR_DISPLAY_OPTION =
      new ImageDisplayOption.Builder().holder(R.color.bg_gray_e).error(R.color.bg_gray_e).animDuration(300).build();
  /**
   * 默认加载选项
   */
  public final static ImageDisplayOption DEFAULT_DISPLAY_OPTION =
      new ImageDisplayOption.Builder().holder(R.color.bg_gray_e).error(R.color.bg_gray_e).animDuration(300).build();
  /**
   * 加载出错显示的资源 id
   */
  private int mErrorResId;
  /**
   * 占位图资源 id
   */
  private int mHolderResId;
  /**
   * 加载错误图
   */
  private Drawable mErrorDrawable;
  /**
   * 占位图
   */
  private Drawable mHolderDrawable;
  /**
   * 动画时间
   */
  private int mAnimDuration = 300;

  public ImageDisplayOption(Builder builder) {
    mErrorResId = builder.mErrorResId;
    mErrorDrawable = builder.mErrorDrawable;
    mHolderResId = builder.mErrorResId;
    mHolderDrawable = builder.mHolderDrawable;
    mAnimDuration = builder.mAnimDuration;
  }

  public Drawable getErrorDrawable(Resources resources) {
    return mErrorResId != 0 ? resources.getDrawable(mErrorResId) : mErrorDrawable;
  }

  public Drawable getHolderDrawable(Resources resources) {
    return mHolderResId != 0 ? resources.getDrawable(mHolderResId) : mHolderDrawable;
  }

  public int getAnimDuration() {
    return mAnimDuration;
  }

  public static class Builder {
    public int mErrorResId;
    public int mHolderResId;
    public Drawable mErrorDrawable;
    public Drawable mHolderDrawable;
    public int mAnimDuration;

    public Builder() {
    }

    public Builder holder(int holderResId) {
      mHolderResId = holderResId;
      return this;
    }

    public Builder holder(Drawable holderDrawable) {
      mHolderDrawable = holderDrawable;
      return this;
    }

    public Builder error(int errorResId) {
      mErrorResId = errorResId;
      return this;
    }

    public Builder error(Drawable errorDrawable) {
      mErrorDrawable = errorDrawable;
      return this;
    }

    public Builder animDuration(int animDuration) {
      mAnimDuration = animDuration;
      return this;
    }

    public ImageDisplayOption build() {
      return new ImageDisplayOption(this);
    }
  }
}
