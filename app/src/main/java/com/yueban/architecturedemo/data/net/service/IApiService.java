package com.yueban.architecturedemo.data.net.service;

import com.yueban.architecturedemo.data.model.main.Repo;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public interface IApiService {
    @GET("users/{user}/repos")
    Observable<List<Repo>> listRepos(@Path("user") String user);
}