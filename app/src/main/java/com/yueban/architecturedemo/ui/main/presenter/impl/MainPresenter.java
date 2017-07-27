package com.yueban.architecturedemo.ui.main.presenter.impl;

import com.yueban.architecturedemo.data.model.net.Repo;
import com.yueban.architecturedemo.domain.main.MainViewData;
import com.yueban.architecturedemo.ui.base.presenter.BasePresenter;
import com.yueban.architecturedemo.ui.main.presenter.IMainPresenter;
import com.yueban.architecturedemo.ui.main.view.IMainView;
import com.yueban.architecturedemo.util.rx.RxUtil;
import com.yueban.architecturedemo.util.rx.SimpleSubscriber;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class MainPresenter<T extends IMainView> extends BasePresenter<T> implements IMainPresenter {
    @Override
    public void requestNetData() {
        MainViewData.listRepos("yueban")
            .compose(RxUtil.<List<Repo>>common(mView))
            .compose(this.<List<Repo>>bindToDestroyEvent())
            .subscribe(new SimpleSubscriber<List<Repo>>() {
                @Override
                public void onNext(List<Repo> repos) {
                    mView.showRepoData(repos);
                }
            });
    }
}
