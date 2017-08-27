package com.yueban.architecturedemo.util;

import android.annotation.SuppressLint;
import android.content.res.Resources;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class MessageFactory {
  /**
   * 需求:
   * 1. APIException 直接返回 errorMsg
   * 2. NetworkConnectionException 返回 网络异常
   * 3. 其他 Exception 暂不处理
   *
   * 格式:
   * "描述信息 : 异常信息"
   */
  private MessageFactory() {
    throw new AssertionError();
  }

  public static String error(Resources res, Throwable throwable) {
    return error(res, throwable, null);
  }

  @SuppressLint("SwitchIntDef")
  public static String error(Resources res, Throwable throwable, String message) {
    String error = null;

    //if (throwable instanceof APIException) {
    //    APIException apiException = (APIException) throwable;
    //    // 后续根据 code 选取提示信息
    //    switch (apiException.errorCode) {
    //        case ErrorCode.BaseBadToken:
    //            // 由底层 Toast
    //            break;
    //        default:
    //            try {
    //                error = res.getString(res.getIdentifier("error_code_" + apiException.errorCode, "string"));
    //            } catch (Exception ignored) {
    //            }
    //            if (!notEmpty(error)) error = apiException.errorMsg;
    //    }
    //} else if (throwable instanceof NetworkConnectionException) {
    //    // TODO: 16/7/21 网络异常 文案提示
    //    error = res.getString(R.string.common_network_not_available);
    //} else if (throwable instanceof CustomException) {
    //    String msg = throwable.getMessage();
    //    if (!TextUtils.isEmpty(msg)) {
    //        error = msg;
    //    }
    //}

    return concat(error, message);
  }

  /**
   * 连接提示信息
   */
  private static String concat(String exception, String message) {
    if (notEmpty(exception) && notEmpty(message)) {
      return message + " : " + exception;
    }

    if (notEmpty(exception)) {
      return exception;
    }

    if (notEmpty(message)) {
      return message;
    }

    return null;
  }

  private static boolean notEmpty(String str) {
    return str != null && str.length() > 0;
  }
}