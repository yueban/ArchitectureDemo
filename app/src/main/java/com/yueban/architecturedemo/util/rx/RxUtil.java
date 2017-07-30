package com.yueban.architecturedemo.util.rx;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import com.yueban.architecturedemo.ui.base.view.IView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class RxUtil {
    private RxUtil() {
        throw new AssertionError();
    }

    public static <T> ObservableTransformer<T, T> applyAsySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@io.reactivex.annotations.NonNull Observable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * 基于 {@link IView#showLoading()} {@link IView#hideLoading()}
     */
    public static <T> ObservableTransformer<T, T> loadingView(@NonNull final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@io.reactivex.annotations.NonNull Observable<T> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        view.showLoading();
                    }
                }).doAfterTerminate(new Action() {
                    /**
                     * 在调用 {@link Observer#onComplete()} 或者 {@link Observer#onError(Throwable)} 之后隐藏 loading
                     */
                    @Override
                    public void run() throws Exception {
                        view.hideLoading();
                    }
                });
            }
        };
    }

    /**
     * 基于 {@link IView#showLoadingDialog()} {@link IView#hideLoadingDialog()}
     */
    public static <T> ObservableTransformer<T, T> loadingDialogView(@NonNull final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@io.reactivex.annotations.NonNull Observable<T> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        view.showLoadingDialog();
                    }
                }).doAfterTerminate(new Action() {
                    /**
                     * 在调用 {@link Observer#onComplete()} 或者 {@link Observer#onError(Throwable)} 之后隐藏 loading
                     */
                    @Override
                    public void run() throws Exception {
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
    public static <T> ObservableTransformer<List<T>, List<T>> filter(final Predicate<T> predicate) {
        return new ObservableTransformer<List<T>, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(@io.reactivex.annotations.NonNull Observable<List<T>> upstream) {
                return upstream.flatMap(new Function<List<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@io.reactivex.annotations.NonNull List<T> ts) throws Exception {
                        return Observable.fromIterable(ts);
                    }
                }).filter(predicate).toList().toObservable();
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
    public static <T> List<T> filter(List<T> ts, final Predicate<T> predicate) {
        return Observable.fromIterable(ts).filter(predicate).toList().toObservable().blockingFirst();
    }

    /**
     * 过滤 List<String> 中的空字符串
     */
    public static ObservableTransformer<List<String>, List<String>> filterEmptyString() {
        return filter(new Predicate<String>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull String s) throws Exception {
                return !TextUtils.isEmpty(s);
            }
        });
    }

    /**
     * 通用错误处理
     */
    public static <T> ObservableTransformer<T, T> error(final IView view, final String message) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@io.reactivex.annotations.NonNull Observable<T> upstream) {
                return upstream.doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showError(throwable, message);
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> error(final IView view) {
        return error(view, null);
    }

    public static <T> ObservableTransformer<T, T> error(final IView view, @StringRes int resId) {
        return error(view, view.context().getResources().getString(resId));
    }

    public static <T> ObservableTransformer<List<T>, List<T>> sort() {
        return new ObservableTransformer<List<T>, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(@io.reactivex.annotations.NonNull Observable<List<T>> upstream) {
                return upstream.flatMap(new Function<List<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(@io.reactivex.annotations.NonNull List<T> ts) throws Exception {
                        return Observable.fromIterable(ts);
                    }
                }).toSortedList().toObservable();
            }
        };
    }

    /**
     * 常用的组合
     */
    public static <T> ObservableTransformer<T, T> common(final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@io.reactivex.annotations.NonNull Observable<T> upstream) {
                return upstream.compose(RxUtil.<T>applyAsySchedulers())
                    .compose(RxUtil.<T>loadingView(view))
                    .compose(RxUtil.<T>error(view));
            }
        };
    }

    /**
     * 常用的组合
     */
    public static <T> ObservableTransformer<T, T> commonWithDialog(final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@io.reactivex.annotations.NonNull Observable<T> upstream) {
                return upstream.compose(RxUtil.<T>applyAsySchedulers())
                    .compose(RxUtil.<T>loadingDialogView(view))
                    .compose(RxUtil.<T>error(view));
            }
        };
    }
}
