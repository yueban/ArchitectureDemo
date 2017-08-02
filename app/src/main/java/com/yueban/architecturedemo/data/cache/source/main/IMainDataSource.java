package com.yueban.architecturedemo.data.cache.source.main;

import com.yueban.architecturedemo.data.model.main.Repo;
import io.reactivex.Observable;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/30
 * @email fbzhh007@gmail.com
 */
public interface IMainDataSource {
    Observable<Boolean> saveRepos(List<Repo> repoList);
}
