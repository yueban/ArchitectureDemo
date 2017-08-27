package com.yueban.architecturedemo.data.cache.db.main;

import com.yueban.architecturedemo.data.cache.db.DBHelper;
import com.yueban.architecturedemo.data.cache.source.main.IMainDataSource;
import com.yueban.architecturedemo.data.model.main.Repo;
import io.reactivex.Observable;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/30
 * @email fbzhh007@gmail.com
 */
public class MainDataSourceDB implements IMainDataSource {
  private MainDataSourceDB() {
  }

  public static MainDataSourceDB getInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public Observable<Boolean> saveRepos(List<Repo> repoList) {
    return DBHelper.save(repoList);
  }

  private static class Holder {
    private static final MainDataSourceDB INSTANCE = new MainDataSourceDB();
  }
}
