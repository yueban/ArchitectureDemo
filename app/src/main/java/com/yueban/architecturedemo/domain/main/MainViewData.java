package com.yueban.architecturedemo.domain.main;

import com.yueban.architecturedemo.data.model.net.Repo;
import com.yueban.architecturedemo.data.net.NetRepository;
import io.reactivex.Observable;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class MainViewData {
    private static final NetRepository NET_REPOSITORY = NetRepository.getInstance();

    private MainViewData() {
        throw new AssertionError();
    }

    public static Observable<List<Repo>> listRepos(String user) {
        return NET_REPOSITORY.getApiService().listRepos(user);
    }
}
