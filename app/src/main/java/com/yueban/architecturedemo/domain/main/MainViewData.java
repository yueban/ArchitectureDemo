package com.yueban.architecturedemo.domain.main;

import com.yueban.architecturedemo.data.cache.db.main.MainDataSourceDB;
import com.yueban.architecturedemo.data.cache.source.main.IMainDataSource;
import com.yueban.architecturedemo.data.model.main.Repo;
import com.yueban.architecturedemo.data.net.NetRepository;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class MainViewData {
    private static final NetRepository NET_REPOSITORY = NetRepository.getInstance();
    private static final IMainDataSource MAIN_DATA_SOURCE = MainDataSourceDB.getInstance();

    private MainViewData() {
        throw new AssertionError();
    }

    public static Observable<List<Repo>> listRepos(String user) {
        return NET_REPOSITORY.getApiService().listRepos(user).doOnNext(new Consumer<List<Repo>>() {
            @Override
            public void accept(List<Repo> repos) throws Exception {
                MAIN_DATA_SOURCE.saveRepos(repos).subscribe();
            }
        });
    }
}
