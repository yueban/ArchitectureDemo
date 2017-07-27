package com.yueban.architecturedemo.data.exception;

import java.io.IOException;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class APIException extends IOException {

    public final static String UNKNOWN = "API未知错误";
    public final static String DATA_PARSE_ERROR = "数据转换出错";

    @ErrorCode public int errorCode;
    public String errorMsg;

    public APIException() {
        this(UNKNOWN);
        this.errorMsg = UNKNOWN;
    }

    public APIException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public APIException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }
}
