package com.yueban.architecturedemo.ui.base.view;

import android.content.Context;
import android.support.annotation.StringRes;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public interface IView {
  Context context();

  void showError(Throwable e);

  void showError(Throwable e, String message);

  void showError(Throwable e, @StringRes int resId);

  /**
   * Show a toast message.
   *
   * @param message A string message.
   */
  void showMsg(String message);

  /**
   * Show the msg.
   *
   * @param msgRes the resource of
   */
  void showMsg(@StringRes int msgRes);

  /**
   * 结束界面
   */
  void finishView();

  /**
   * 显示 loading
   */
  void showLoading();

  /**
   * 隐藏 loading
   */
  void hideLoading();

  /**
   * 显示 loading 弹窗
   */
  void showLoadingDialog();

  /**
   * 隐藏 loading 弹窗
   */
  void hideLoadingDialog();

  /**
   * 获取系统权限
   *
   * @param permissions 权限数组
   */
  Observable<Boolean> requestPermission(String... permissions);

  /**
   * 获取系统权限数据转换操作
   *
   * @param permissions 权限数组
   */
  ObservableTransformer<Object, Permission> requestPermissionTransform(String... permissions);

  /**
   * 获取 RxPermissions 对象
   */
  RxPermissions getRxPermissions();
}
