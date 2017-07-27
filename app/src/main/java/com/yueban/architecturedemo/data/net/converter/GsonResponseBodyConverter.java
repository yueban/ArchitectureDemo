package com.yueban.architecturedemo.data.net.converter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.yueban.architecturedemo.data.exception.APIException;
import com.yueban.architecturedemo.data.model.net.ApiModel;
import com.yueban.architecturedemo.data.model.net.Repo;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        try {
            TypeAdapter<?> adapter;
            String response = value.string();

            if ((((ParameterizedType) type).getActualTypeArguments()[0] == Repo.class
                && ((ParameterizedType) type).getRawType() == List.class)
                //type == ThirdAuthEntity.class
                //|| type == UpgradeInfo.class
                //||
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == ApiModel.class)) {
                adapter = gson.getAdapter(TypeToken.get(type));
                T t = (T) adapter.fromJson(response);
                if (t != null) {
                    return t;
                }
            }

            Type newType = new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    // 当返回数据的type为Void的类型的时候, 将其替换成string来解析
                    // 将具体的数据类型, 包装成为ApiModel<T>的类型
                    return (type == Void.class || type == Boolean.class) ? new Type[] { Object.class } : new Type[] { type };
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }

                @Override
                public Type getRawType() {
                    return ApiModel.class;
                }
            };
            adapter = gson.getAdapter(TypeToken.get(newType));
            ApiModel apiModel = (ApiModel) adapter.fromJson(response);
            if (apiModel != null) {
                // 因为API定义返回的结果为bool值类型, 会相应地取最外层返回结果的success的值.
                if (type == Boolean.class) {
                    apiModel.data = apiModel.success;
                }
                if (apiModel.success && type == Void.class) {
                    return null;
                } else if (apiModel.success) {
                    return apiModel.data;
                } else {
                    APIException exception = new APIException(apiModel.error_code, apiModel.error_msg);
                    // TODO: 2017/7/29 crashreport
                    //CrashReport.postCatchedException(exception);
                    throw exception;
                }
            }
            APIException exception = new APIException(APIException.DATA_PARSE_ERROR);
            // TODO: 2017/7/29 crashreport
            //CrashReport.postCatchedException(exception);
            throw exception;
        } finally {
            value.close();
        }
    }
}
