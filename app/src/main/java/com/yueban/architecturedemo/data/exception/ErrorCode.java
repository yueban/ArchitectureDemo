package com.yueban.architecturedemo.data.exception;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
@IntDef({ ErrorCode.NotFound, ErrorCode.MethodNotAllowed })
@Retention(RetentionPolicy.SOURCE)
public @interface ErrorCode {
  /**
   * 找不到该请求地址
   */
  int NotFound = 404;
  /**
   * 不支持的提交方式
   */
  int MethodNotAllowed = 405;
}
