package com.yueban.architecturedemo.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class ApiModel<T> {
    @SerializedName("error_code") public int error_code;
    @SerializedName("error_msg") public String error_msg;
    @SerializedName("success") public boolean success;

    @SerializedName("data") public T data;
}