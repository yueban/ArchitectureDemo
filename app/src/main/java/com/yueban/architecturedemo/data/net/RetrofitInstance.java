package com.yueban.architecturedemo.data.net;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yueban.architecturedemo.BuildConfig;
import com.yueban.architecturedemo.data.net.converter.GsonConverterFactory;
import com.yueban.architecturedemo.data.net.interceptor.HttpLoggingInterceptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public final class RetrofitInstance {
    private final Retrofit mRetrofit;

    private RetrofitInstance() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(NetConstant.BASE_URL)
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        try {
            // 修改retrofit的convertFactories的设置, 去掉其默认值BuiltInConverters(), 替换为GsonConvertFactory.
            List<Converter.Factory> convertFactories = new ArrayList<>();
            convertFactories.add(GsonConverterFactory.create(gson));
            Field field = builder.getClass().getDeclaredField("converterFactories");
            field.setAccessible(true);
            field.set(builder, convertFactories);
        } catch (Exception e) {
            builder.addConverterFactory(GsonConverterFactory.create(gson));
        }

        mRetrofit = builder.build();
    }

    public static Retrofit getInstance() {
        return Holder.INSTANCE.mRetrofit;
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder clientBuilder =
            new OkHttpClient.Builder().connectTimeout(NetConstant.API_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(NetConstant.API_READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(NetConstant.API_WRITE_TIMEOUT, TimeUnit.MILLISECONDS);

        //stetho网络监控
        if (BuildConfig.DEBUG) {
            // TODO: 2017/7/29 add Interceptor
            //clientBuilder.addNetworkInterceptor(new StethoInterceptor());
            //clientBuilder.addInterceptor(new MockInterceptor());

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(logging);
        }

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                HttpUrl api = HttpUrl.parse(NetConstant.BASE_URL);

                // 如果不是请求 API ， 不添加额外的参数
                if (!TextUtils.equals(request.url().host(), api.host())) {
                    return chain.proceed(request);
                }

                HttpUrl newUrl = request.url()
                    .newBuilder()
                    .addEncodedQueryParameter("format", "json")
                    .addEncodedQueryParameter("account", "true")
                    .build();
                request = request.newBuilder().url(newUrl).build();
                return chain.proceed(request);
            }
        });
        return clientBuilder.build();
    }

    static class Holder {
        static final RetrofitInstance INSTANCE = new RetrofitInstance();
    }
}
