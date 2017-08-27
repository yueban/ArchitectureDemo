package com.yueban.architecturedemo.ui.main;

import android.widget.TextView;
import butterknife.BindView;
import com.yueban.architecturedemo.R;
import com.yueban.architecturedemo.data.model.main.Repo;
import com.yueban.architecturedemo.ui.base.view.BaseActivity;
import com.yueban.architecturedemo.ui.main.event.NetworkCompleteEvent;
import com.yueban.architecturedemo.ui.main.presenter.IMainPresenter;
import com.yueban.architecturedemo.ui.main.presenter.impl.MainPresenter;
import com.yueban.architecturedemo.ui.main.view.IMainView;
import com.yueban.architecturedemo.util.CollectionUtil;
import com.yueban.architecturedemo.util.eventbus.EventBusInstance;
import com.yueban.architecturedemo.util.rx.RxViewUtil;
import io.reactivex.functions.Consumer;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity<IMainPresenter> implements IMainView {
  @BindView(R.id.text_view) TextView mTextView;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_main;
  }

  @Override
  protected void initInjector() {
    EventBusInstance.getBus().register(this);
  }

  @Override
  protected IMainPresenter bindPresenter() {
    return new MainPresenter();
  }

  @Override
  protected void setView() {
    RxViewUtil.clicks(mTextView).subscribe(new Consumer<Object>() {
      @Override
      public void accept(Object o) throws Exception {
        mPresenter.requestNetData();
      }
    });
  }

  @Override
  protected void initToolbar() {
    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
      if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
      }
    }
  }

  @Override
  protected void initData() {
    mPresenter.requestNetData();
  }

  @Override
  public void showRepoData(List<Repo> repos) {
    mTextView.setText(CollectionUtil.toString(repos));
  }

  @Override
  @Subscribe
  public void onNetworkCompleteEvent(NetworkCompleteEvent networkCompleteEvent) {
    showMsg(networkCompleteEvent.getClass().getSimpleName());
  }
}
