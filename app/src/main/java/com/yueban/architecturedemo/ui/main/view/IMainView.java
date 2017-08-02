package com.yueban.architecturedemo.ui.main.view;

import com.yueban.architecturedemo.data.model.main.Repo;
import com.yueban.architecturedemo.ui.base.view.IView;
import com.yueban.architecturedemo.ui.main.event.NetworkCompleteEvent;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public interface IMainView extends IView {
    void showRepoData(List<Repo> repos);

    @Subscribe
    void onNetworkCompleteEvent(NetworkCompleteEvent networkCompleteEvent);
}
