package com.yueban.architecturedemo.ui.base.presenter;

import android.support.annotation.NonNull;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.yueban.architecturedemo.ui.base.view.BaseActivity;
import com.yueban.architecturedemo.ui.base.view.BaseFragment;
import com.yueban.architecturedemo.ui.base.view.IView;
import com.yueban.architecturedemo.util.rxlifecycle.PresenterEvent;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.yueban.architecturedemo.util.rxlifecycle.PresenterLifecycleProvider.PRESENTER_LIFECYCLE;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class BasePresenter<V extends IView> implements IPresenter, LifecycleProvider<PresenterEvent> {
  private final BehaviorSubject<PresenterEvent> lifecycleSubject = BehaviorSubject.create();
  protected V mView;

  public V getView() {
    return mView;
  }

  public void setView(V v) {
    mView = v;
    if (mView == null) {
      throw new RuntimeException("mView cant be null");
    }
    if ((!(mView instanceof BaseActivity) && !(mView instanceof BaseFragment))) {
      throw new RuntimeException("mView must be extends BaseActivity or BaseFragment");
    }
  }

  /**
   * Method that control the lifecycle of the view. It should be called in the view's
   * (Activity or Fragment) onCreate() / onCreateView() method.
   */
  public void create() {
    lifecycleSubject.onNext(PresenterEvent.CREATE);
  }

  /**
   * Method that control the lifecycle of the view. It should be called in the view's
   * (Activity or Fragment) onStart() method.
   */
  public void start() {
    lifecycleSubject.onNext(PresenterEvent.START);
  }

  /**
   * Method that control the lifecycle of the view. It should be called in the view's
   * (Activity or Fragment) onResume() method.
   */
  public void resume() {
    lifecycleSubject.onNext(PresenterEvent.RESUME);
  }

  /**
   * Method that control the lifecycle of the view. It should be called in the view's
   * (Activity or Fragment) onPause() method.
   */
  public void pause() {
    lifecycleSubject.onNext(PresenterEvent.PAUSE);
  }

  /**
   * Method that control the lifecycle of the view. It should be called in the view's
   * (Activity or Fragment) onStop() method.
   */
  public void stop() {
    lifecycleSubject.onNext(PresenterEvent.STOP);
  }

  /**
   * Method that control the lifecycle of the view. It should be called in the view's
   * (Activity or Fragment) onDestroy() method.
   */
  public void destroy() {
    lifecycleSubject.onNext(PresenterEvent.DESTROY);
  }

  @NonNull
  @Override
  public final Observable<PresenterEvent> lifecycle() {
    return lifecycleSubject.hide();
  }

  @NonNull
  @Override
  public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull PresenterEvent event) {
    return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
  }

  @NonNull
  public final <T> LifecycleTransformer<T> bindToDestroyEvent() {
    return bindUntilEvent(PresenterEvent.DESTROY);
  }

  @NonNull
  @Override
  public <T> LifecycleTransformer<T> bindToLifecycle() {
    return RxLifecycle.bind(lifecycleSubject, PRESENTER_LIFECYCLE);
  }
}
