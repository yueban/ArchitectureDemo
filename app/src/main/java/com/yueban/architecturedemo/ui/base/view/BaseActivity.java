package com.yueban.architecturedemo.ui.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.yueban.architecturedemo.R;
import com.yueban.architecturedemo.ui.base.presenter.BasePresenter;
import com.yueban.architecturedemo.ui.base.presenter.IPresenter;
import com.yueban.architecturedemo.util.L;
import com.yueban.architecturedemo.util.MessageFactory;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity implements IView, LifecycleProvider<ActivityEvent> {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    protected Toolbar mToolbar;
    protected View mRootView;
    protected MaterialDialog mProgressDialog;
    private IPresenter mPresenter;
    private RxPermissions mRxPermissions;

    @SuppressLint("InflateParams")
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        if (getBackgroundColor() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(getBackgroundColor()));
        }
        onBeforeCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        mLifecycleSubject.onNext(ActivityEvent.CREATE);

        L.i("current_activity:" + getClass().getSimpleName());

        onBeforeContentViewInit(savedInstanceState);
        if (hasToolbar()) {
            mRootView = LayoutInflater.from(this).inflate(R.layout.activity_base, null);
            mToolbar = (Toolbar) mRootView.findViewById(R.id.base_toolbar);
            ViewStub containerView = (ViewStub) mRootView.findViewById(R.id.base_container);
            containerView.setLayoutResource(getLayoutId());
            containerView.inflate();
        } else {
            mRootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        }
        onLayoutInflated();
        setContentView(mRootView);
        ButterKnife.bind(this);
        // 执行Dagger2的注入
        initInjector();
        // 执行Presenter的绑定, 与当前Activity的生命周期相绑定
        setPresenter(bindPresenter());
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).create();
        }
        // 设置toolbar
        initToolbar();
        onAfterContentViewInit(savedInstanceState);
        // 执行View的初始化操作
        setView();
        // 执行数据的初始化
        initData();
    }

    protected void onLayoutInflated() {

    }

    /**
     * 执行在所有数据初始化之前的操作
     */
    protected void onBeforeCreate(Bundle savedInstanceState) {

    }

    /**
     * 执行在所有数据初始化之前的操作
     */
    protected void onBeforeContentViewInit(Bundle savedInstanceState) {

    }

    protected void onAfterContentViewInit(Bundle savedInstanceState) {

    }

    protected boolean hasToolbar() {
        return true;
    }

    /**
     * Get the layout Id.
     */
    protected abstract int getLayoutId();

    /**
     * Get Activity Window Background Color. null means default, respect theme style.
     */
    protected Integer getBackgroundColor() {
        return null;
    }

    /**
     * Init the injectors.
     */
    protected abstract void initInjector();

    /**
     * Bind the presenter
     *
     * @return IPresenter
     */
    protected abstract IPresenter bindPresenter();

    /**
     * Init the listener.
     */
    protected abstract void setView();

    /**
     * Init the data.
     */
    protected abstract void initData();

    protected void initToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayUseLogoEnabled(false);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(false);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mToolbar != null) {
            mToolbar.setTitle(titleId);
        } else {
            super.setTitle(titleId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setStatusBar();
    }

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getApplicationContext().getResources().getColor(R.color.toolbar_default));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(ActivityEvent.START);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(ActivityEvent.RESUME);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifecycleSubject.onNext(ActivityEvent.PAUSE);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycleSubject.onNext(ActivityEvent.STOP);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleSubject.onNext(ActivityEvent.DESTROY);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).destroy();
            mPresenter = null;
        }
    }

    @Override
    public Context context() {
        return this;
    }

    protected IPresenter getPresenter() {
        return mPresenter;
    }

    private void setPresenter(IPresenter presenter) {
        mPresenter = presenter;
        if (mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).setView(this);
        }
    }

    @Override
    public void showError(Throwable e) {
        showError(e, null);
    }

    @Override
    public void showError(Throwable e, int resId) {
        showError(e, getString(resId));
    }

    @Override
    public void showError(Throwable e, String message) {
        String error = MessageFactory.error(getResources(), e, message);
        if (!TextUtils.isEmpty(error)) {
            Snackbar.make(mRootView, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showMsg(String message) {
        if (!TextUtils.isEmpty(message)) {
            Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMsg(@StringRes int msgRes) {
        if (msgRes > 0) {
            Snackbar.make(mRootView, getString(msgRes), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public final void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this).progressIndeterminateStyle(false)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
        }
        mProgressDialog.show();
    }

    @Override
    public final void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public final Observable<Permission> requestPermission(String... permissions) {
        return getRxPermissions().requestEach(permissions).compose(this.<Permission>bindToDestroyEvent());
    }

    @Override
    public final ObservableTransformer<Object, Permission> requestPermissionTransform(String... permissions) {
        return getRxPermissions().ensureEach(permissions);
    }

    @Override
    public final RxPermissions getRxPermissions() {
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(this);
        }
        return mRxPermissions;
    }

    @NonNull
    @Override
    public final Observable<ActivityEvent> lifecycle() {
        return mLifecycleSubject.hide();
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(mLifecycleSubject);
    }

    @NonNull
    public final <T> ObservableTransformer<T, T> bindToDestroyEvent() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }
}