package com.yueban.architecturedemo.ui.main.presenter.impl;

import com.yueban.architecturedemo.data.model.main.Repo;
import com.yueban.architecturedemo.domain.main.MainViewData;
import com.yueban.architecturedemo.ui.base.presenter.BasePresenter;
import com.yueban.architecturedemo.ui.main.event.NetworkCompleteEvent;
import com.yueban.architecturedemo.ui.main.presenter.IMainPresenter;
import com.yueban.architecturedemo.ui.main.view.IMainView;
import com.yueban.architecturedemo.util.eventbus.EventBusInstance;
import com.yueban.architecturedemo.util.rx.RxUtil;
import io.reactivex.functions.Consumer;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class MainPresenter extends BasePresenter<IMainView> implements IMainPresenter {
  @Override
  public void requestNetData() {
    MainViewData.listRepos("yueban")
        .compose(RxUtil.<List<Repo>>commonDialog(mView))
        .compose(this.<List<Repo>>bindToDestroyEvent())
        .subscribe(new Consumer<List<Repo>>() {
          @Override
          public void accept(List<Repo> repos) throws Exception {
            mView.showRepoData(repos);
            EventBusInstance.getBus().post(new NetworkCompleteEvent());
          }
        });
  }
}
