package com.yueban.architecturedemo.util.rx;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import com.yueban.architecturedemo.ui.base.view.IView;
import java.util.List;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class RxUtil {
    private RxUtil() {
        throw new AssertionError();
    }

    public static <T> Observable.Transformer<T, T> applyAsySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * 基于 {@link IView#showLoadingDialog()}
     */
    public static <T> Observable.Transformer<T, T> loadingView(@NonNull final IView view) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        view.showLoadingDialog();
                    }
                }).doAfterTerminate(new Action0() {
                    /**
                     * 在调用 {@link Observer#onCompleted()} 或者 {@link Observer#onError(Throwable)} 之后隐藏 loading
                     */
                    @Override
                    public void call() {
                        view.hideLoadingDialog();
                    }
                });
            }
        };
    }

    /**
     * 对 List<?> 列表中的元素进行过滤
     *
     * @param predicate 检查器
     * @param <T>       列表中的元素类型
     * @return 原列表类型
     */
    public static <T> Observable.Transformer<List<T>, List<T>> filter(final Func1<T, Boolean> predicate) {
        return new Observable.Transformer<List<T>, List<T>>() {
            @Override
            public Observable<List<T>> call(Observable<List<T>> observable) {
                return observable.flatMap(new Func1<List<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(List<T> ts) {
                        return Observable.from(ts);
                    }
                }).filter(predicate).toList();
            }
        };
    }

    /**
     * 同步过滤列表(可能会发生阻塞)
     *
     * @param ts        数据源
     * @param predicate 过滤器
     * @param <T>       列表元素类型
     * @return 过滤后的列表
     */
    public static <T> List<T> filter(List<T> ts, final Func1<T, Boolean> predicate) {
        return Observable.from(ts).filter(predicate).toList().toBlocking().first();
    }

    /**
     * 过滤 List<String> 中的空字符串
     */
    public static Observable.Transformer<List<String>, List<String>> filterEmptyString() {
        return filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return !TextUtils.isEmpty(s);
            }
        });
    }

    /**
     * 通用错误处理
     */
    public static <T> Observable.Transformer<T, T> error(final IView view, final String message) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showError(throwable, message);
                    }
                });
            }
        };
    }

    public static <T> Observable.Transformer<T, T> error(final IView view) {
        return error(view, null);
    }

    public static <T> Observable.Transformer<T, T> error(final IView view, @StringRes int resId) {
        return error(view, view.context().getResources().getString(resId));
    }

    public static <T> Observable.Transformer<List<T>, List<T>> sort() {
        return new Observable.Transformer<List<T>, List<T>>() {
            @Override
            public Observable<List<T>> call(Observable<List<T>> observable) {
                return observable.flatMap(new Func1<List<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(List<T> sessions) {
                        return Observable.from(sessions);
                    }
                }).toSortedList();
            }
        };
    }

    /**
     * 常用的组合
     */
    public static <T> Observable.Transformer<T, T> common(final IView view) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.compose(RxUtil.<T>applyAsySchedulers())
                    .compose(RxUtil.<T>loadingView(view))
                    .compose(RxUtil.<T>error(view));
            }
        };
    }
}
