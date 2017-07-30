package com.yueban.architecturedemo.ui.base.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.yueban.architecturedemo.R;
import com.yueban.architecturedemo.ui.base.presenter.BasePresenter;
import com.yueban.architecturedemo.ui.base.presenter.IPresenter;
import com.yueban.architecturedemo.util.L;
import com.yueban.architecturedemo.util.eventbus.EventBusInstance;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public abstract class BaseFragment extends Fragment implements IView, LifecycleProvider<FragmentEvent> {
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    protected BaseActivity mActivity;
    protected View mView;
    protected MaterialDialog mProgressDialog;
    private IPresenter mPresenter;
    private Unbinder mUnBinder;
    private RxPermissions mRxPermissions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.i("current_fragment:" + getClass().getSimpleName());
        mLifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);

        mView = inflater.inflate(getLayoutId(), null);
        mUnBinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector();
        setPresenter(bindPresenter());
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).create();
        }
        setView();
        initData();
    }

    /**
     * Get the layout Id.
     */
    protected abstract int getLayoutId();

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

    private void setPresenter(IPresenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
            if (mPresenter instanceof BasePresenter) {
                ((BasePresenter) mPresenter).setView(this);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLifecycleSubject.onNext(FragmentEvent.ATTACH);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleSubject.onNext(FragmentEvent.START);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleSubject.onNext(FragmentEvent.RESUME);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mLifecycleSubject.onNext(FragmentEvent.PAUSE);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mLifecycleSubject.onNext(FragmentEvent.STOP);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).stop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        if (mPresenter != null && mPresenter instanceof BasePresenter) {
            ((BasePresenter) mPresenter).destroy();
            mPresenter = null;
        }
        mUnBinder.unbind();
    }

    @Override
    public void onDestroy() {
        mLifecycleSubject.onNext(FragmentEvent.DESTROY);
        if (EventBusInstance.getBus().isRegistered(this)) {
            EventBusInstance.getBus().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLifecycleSubject.onNext(FragmentEvent.DETACH);
    }

    @NonNull
    @Override
    public final Observable<FragmentEvent> lifecycle() {
        return mLifecycleSubject.hide();
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    @NonNull
    public final <T> ObservableTransformer<T, T> bindToDestroyEvent() {
        return bindUntilEvent(FragmentEvent.DESTROY);
    }

    @NonNull
    @Override
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(mLifecycleSubject);
    }

    @Override
    public Context context() {
        return mActivity;
    }

    @Override
    public void showError(Throwable e) {
        mActivity.showError(e);
    }

    @Override
    public void showError(Throwable e, String message) {
        mActivity.showError(e, message);
    }

    @Override
    public void showError(Throwable e, @StringRes int resId) {
        mActivity.showError(e, resId);
    }

    @Override
    public void showMsg(String message) {
        mActivity.showMsg(message);
    }

    @Override
    public void showMsg(@StringRes int msgRes) {
        mActivity.showMsg(msgRes);
    }

    @Override
    public void finishView() {
        mActivity.finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(mActivity).content(R.string.loading)
                .progress(true, 0)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
        }
        mProgressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public Observable<Permission> requestPermission(String... permissions) {
        return getRxPermissions().requestEach(permissions);
    }

    @Override
    public ObservableTransformer<Object, Permission> requestPermissionTransform(String... permissions) {
        return getRxPermissions().ensureEach(permissions);
    }

    @Override
    public RxPermissions getRxPermissions() {
        if (mRxPermissions == null) {
            mRxPermissions = new RxPermissions(mActivity);
        }
        return mRxPermissions;
    }
}
