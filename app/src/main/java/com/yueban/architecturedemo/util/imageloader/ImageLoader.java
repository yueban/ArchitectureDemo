package com.yueban.architecturedemo.util.imageloader;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.io.File;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class ImageLoader {
    /**
     * 显示图片
     *
     * @param context   上下文
     * @param resId     图片 resId
     * @param imageView 控件
     */
    public static void displayImage(Context context, int resId, ImageView imageView) {
        displayImage(context, resId, imageView, ImageDisplayOption.DEFAULT_DISPLAY_OPTION);
    }

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param url       图片 url
     * @param imageView 控件
     */
    public static void displayImage(Context context, String url, ImageView imageView) {
        displayImage(context, url, imageView, ImageDisplayOption.DEFAULT_DISPLAY_OPTION);
    }

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param file      图片文件
     * @param imageView 控件
     */
    public static void displayImage(Context context, File file, ImageView imageView) {
        displayImage(context, file, imageView, ImageDisplayOption.DEFAULT_DISPLAY_OPTION);
    }

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param resId     图片 resId
     * @param imageView 控件
     * @param option    显示选项
     */
    public static void displayImage(Context context, int resId, ImageView imageView, ImageDisplayOption option) {
        Resources resources = context.getResources();
        Picasso.with(context)
            .load(resId)
            .placeholder(option.getHolderDrawable(resources))
            .error(option.getErrorDrawable(resources))
            // TODO: 2017/7/20 最大尺寸设置根据屏幕宽度确定
            .resize(400, 0)
            .into(imageView);
    }

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param url       图片 url
     * @param imageView 控件
     * @param option    显示选项
     */
    public static void displayImage(Context context, String url, ImageView imageView, ImageDisplayOption option) {
        Resources resources = context.getResources();
        if (url == null) {
            url = "";
        }
        Picasso.with(context)
            .load(Uri.parse(url))
            .placeholder(option.getHolderDrawable(resources))
            .error(option.getErrorDrawable(resources))
            .into(imageView);
    }

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param uri       资源文件 uri
     * @param imageView 控件
     */
    public static void displayImage(Context context, Uri uri, ImageView imageView) {
        displayImage(context, uri, imageView, ImageDisplayOption.DEFAULT_DISPLAY_OPTION);
    }

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param uri       资源文件 uri
     * @param imageView 控件
     * @param option    显示选项
     */
    public static void displayImage(Context context, Uri uri, ImageView imageView, ImageDisplayOption option) {
        Resources resources = context.getResources();
        Picasso.with(context)
            .load(uri)
            .placeholder(option.getHolderDrawable(resources))
            .error(option.getErrorDrawable(resources))
            .into(imageView);
    }

    /**
     * 显示图片
     *
     * @param context   上下文
     * @param file      图片文件
     * @param imageView 控件
     * @param option    显示选项
     */
    public static void displayImage(Context context, File file, ImageView imageView, ImageDisplayOption option) {
        Resources resources = context.getResources();
        Picasso.with(context)
            .load(file)
            .placeholder(option.getHolderDrawable(resources))
            .error(option.getErrorDrawable(resources))
            //.crossFade(option.getAnimDuration())
            .into(imageView);
    }

    /**
     * 取消图片加载
     */
    public static void cancelRequest(Context context, ImageView imageView) {
        Picasso.with(context).cancelRequest(imageView);
    }
}