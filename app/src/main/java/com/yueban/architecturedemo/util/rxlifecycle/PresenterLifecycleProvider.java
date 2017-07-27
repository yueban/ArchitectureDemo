package com.yueban.architecturedemo.util.rxlifecycle;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.trello.rxlifecycle.OutsideLifecycleException;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public interface PresenterLifecycleProvider {
    Func1<PresenterEvent, PresenterEvent> PRESENTER_LIFECYCLE = new Func1<PresenterEvent, PresenterEvent>() {
        @Override
        public PresenterEvent call(PresenterEvent lastEvent) {
            switch (lastEvent) {
                case CREATE:
                    return PresenterEvent.DESTROY;
                case START:
                    return PresenterEvent.STOP;
                case RESUME:
                    return PresenterEvent.PAUSE;
                case PAUSE:
                    return PresenterEvent.STOP;
                case STOP:
                    return PresenterEvent.DESTROY;
                case DESTROY:
                    throw new OutsideLifecycleException("Cannot bind to Presenter lifecycle when outside of it.");
                default:
                    throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
            }
        }
    };

    /**
     * @return a sequence of {@link BasePresenter} lifecycle events
     */
    @NonNull
    @CheckResult
    Observable<PresenterEvent> lifecycle();

    /**
     * Binds a source until a specific {@link PresenterEvent} occurs.
     * <p>
     * Intended for use with {@link Observable#compose(Observable.Transformer)}
     *
     * @param event the {@link PresenterEvent} that triggers unsubscription
     * @return a reusable {@link rx.Observable.Transformer} which unsubscribes when the event triggers.
     */
    @NonNull
    @CheckResult
    <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull PresenterEvent event);

    /**
     * Binds a source until the next reasonable {@link PresenterEvent} occurs.
     * <p>
     * Intended for use with {@link Observable#compose(Observable.Transformer)}
     *
     * @return a reusable {@link rx.Observable.Transformer} which unsubscribes at the correct time.
     */
    @NonNull
    @CheckResult
    <T> Observable.Transformer<T, T> bindToLifecycle();
}