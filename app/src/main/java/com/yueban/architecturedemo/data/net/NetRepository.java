package com.yueban.architecturedemo.data.net;

import com.yueban.architecturedemo.data.net.service.IApiService;
import retrofit2.Retrofit;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class NetRepository {
    private final IApiService mApiService;

    private NetRepository() {
        Retrofit retrofit = RetrofitInstance.getInstance();
        mApiService = retrofit.create(IApiService.class);
    }

    public static NetRepository getInstance() {
        return Holder.INSTANCE;
    }

    public IApiService getApiService() {
        return mApiService;
    }

    static class Holder {
        static final NetRepository INSTANCE = new NetRepository();
    }
}
