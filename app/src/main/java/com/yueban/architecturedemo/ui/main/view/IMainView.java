package com.yueban.architecturedemo.ui.main.view;

import com.yueban.architecturedemo.data.model.net.Repo;
import com.yueban.architecturedemo.ui.base.view.IView;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public interface IMainView extends IView {
    void showRepoData(List<Repo> repos);
}