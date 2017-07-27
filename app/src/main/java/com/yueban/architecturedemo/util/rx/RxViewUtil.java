package com.yueban.architecturedemo.util.rx;

import android.annotation.TargetApi;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.jakewharton.rxbinding.internal.Preconditions;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.view.ViewAttachEvent;
import com.jakewharton.rxbinding.view.ViewLayoutChangeEvent;
import com.jakewharton.rxbinding.view.ViewScrollChangeEvent;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;
import rx.schedulers.Timestamped;

import static android.os.Build.VERSION_CODES.M;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class RxViewUtil {
    /**
     * 输入相应延迟
     */
    public static final int TEXT_CHANGE_DEBOUNCE_TIMEZONE = 1000;
    /**
     * 控件点击时间周期
     */
    private static final int CLICK_PERIOD_TIME_MILLS = 300;

    private RxViewUtil() {
        throw new AssertionError("No instances.");
    }

    @NonNull
    public static Observable<Void> attaches(@NonNull View view) {
        return RxView.attaches(view);
    }

    @NonNull
    public static Observable<ViewAttachEvent> attachEvents(@NonNull View view) {
        return RxView.attachEvents(view);
    }

    @NonNull
    public static Observable<Void> detaches(@NonNull View view) {
        return RxView.detaches(view);
    }

    @NonNull
    public static Observable<Void> clicks(@NonNull View view) {
        return RxView.clicks(view).throttleFirst(CLICK_PERIOD_TIME_MILLS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
    }

    @NonNull
    public static Observable<DragEvent> drags(@NonNull View view) {
        return RxView.drags(view);
    }

    @NonNull
    public static Observable<DragEvent> drags(@NonNull View view, @NonNull Func1<? super DragEvent, Boolean> handled) {
        return RxView.drags(view, handled);
    }

    @NonNull
    public static Observable<Void> draws(@NonNull View view) {
        return RxView.draws(view);
    }

    @NonNull
    public static Observable<Boolean> focusChanges(@NonNull View view) {
        return RxView.focusChanges(view);
    }

    @NonNull
    public static Observable<Void> globalLayouts(@NonNull View view) {
        return RxView.globalLayouts(view);
    }

    @NonNull
    public static Observable<MotionEvent> hovers(@NonNull View view) {
        return RxView.hovers(view);
    }

    @NonNull
    public static Observable<MotionEvent> hovers(@NonNull View view, @NonNull Func1<? super MotionEvent, Boolean> handled) {
        return RxView.hovers(view, handled);
    }

    @NonNull
    public static Observable<Void> layoutChanges(@NonNull View view) {
        return RxView.layoutChanges(view);
    }

    @NonNull
    public static Observable<ViewLayoutChangeEvent> layoutChangeEvents(@NonNull View view) {
        return RxView.layoutChangeEvents(view);
    }

    @NonNull
    public static Observable<Void> longClicks(@NonNull View view) {
        return RxView.longClicks(view);
    }

    @NonNull
    public static Observable<Void> longClicks(@NonNull View view, @NonNull Func0<Boolean> handled) {
        return RxView.longClicks(view, handled);
    }

    @NonNull
    public static Observable<Void> preDraws(@NonNull View view, @NonNull Func0<Boolean> proceedDrawingPass) {
        return RxView.preDraws(view, proceedDrawingPass);
    }

    @TargetApi(M)
    @NonNull
    public static Observable<ViewScrollChangeEvent> scrollChangeEvents(@NonNull View view) {
        return RxView.scrollChangeEvents(view);
    }

    @NonNull
    public static Observable<Integer> systemUiVisibilityChanges(@NonNull View view) {
        return RxView.systemUiVisibilityChanges(view);
    }

    @NonNull
    public static Observable<MotionEvent> touches(@NonNull View view) {
        return RxView.touches(view);
    }

    @NonNull
    public static Observable<MotionEvent> touches(@NonNull View view, @NonNull Func1<? super MotionEvent, Boolean> handled) {
        return RxView.touches(view, handled);
    }

    @NonNull
    public static Action1<? super Boolean> activated(@NonNull final View view) {
        return RxView.activated(view);
    }

    @NonNull
    public static Action1<? super Boolean> clickable(@NonNull final View view) {
        return RxView.clickable(view);
    }

    @NonNull
    public static Action1<? super Boolean> enabled(@NonNull final View view) {
        return RxView.enabled(view);
    }

    @NonNull
    public static Action1<? super Boolean> pressed(@NonNull final View view) {
        return RxView.pressed(view);
    }

    @NonNull
    public static Action1<? super Boolean> selected(@NonNull final View view) {
        return RxView.selected(view);
    }

    @NonNull
    public static Action1<? super Boolean> visibility(@NonNull View view) {
        return RxView.visibility(view);
    }

    @NonNull
    public static Action1<? super Boolean> visibility(@NonNull final View view, final int visibilityWhenFalse) {
        return RxView.visibility(view, visibilityWhenFalse);
    }

    @NonNull
    public static Observable<KeyEvent> keys(@NonNull View view) {
        return RxView.keys(view);
    }

    @NonNull
    public static Observable<KeyEvent> keys(@NonNull View view, @NonNull Func1<? super KeyEvent, Boolean> handled) {
        return RxView.keys(view, handled);
    }

    /**
     * 检查多个文本框,同时满足某种条件
     *
     * @param checker 检查器
     * @param views   多个文本框 eg: EditText
     */
    @NonNull
    public static Observable<Boolean> multiTextCheck(@NonNull final Func1<CharSequence, Boolean> checker,
        @NonNull TextView... views) {

        Preconditions.checkNotNull(checker, "checker == null");
        Preconditions.checkNotNull(views, "views == null");

        List<Observable<CharSequence>> observableList = new ArrayList<>();
        for (TextView view : views) {
            observableList.add(RxTextView.textChanges(view));
        }

        return Observable.combineLatest(observableList, new FuncN<Boolean>() {
            @Override
            public Boolean call(Object... args) {
                boolean pass;
                for (Object arg : args) {
                    // 校验类型 & 外部判断
                    pass = arg instanceof CharSequence && checker.call(((CharSequence) arg));
                    // 如果一个不通过, 直接false
                    if (!pass) return false;
                }
                return true;
            }
        });
    }

    /**
     * 要求多个输入框都输入数据才能通过
     */
    @NonNull
    public static Observable<Boolean> multiInputNotEmpty(@NonNull TextView... views) {

        return multiTextCheck(new Func1<CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence sequence) {
                return sequence.length() > 0;
            }
        }, views);
    }

    /**
     * 输入框按回车,自动点击按钮完成操作
     */
    public static void buttonAutoClick(@NonNull TextView view, @NonNull final View button, final int actionId) {

        Preconditions.checkNotNull(view, "view == null");
        Preconditions.checkNotNull(button, "button == null");

        RxTextView.editorActions(view, new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer integer) {
                return integer == actionId;
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                button.performClick();
            }
        });
    }

    /**
     * 默认 actionId 为 EditorInfo.IME_ACTION_GO, xml 中需要指定 android:imeOptions="actionGo"
     */
    public static void buttonAutoClick(@NonNull TextView view, @NonNull final View button) {
        buttonAutoClick(view, button, EditorInfo.IME_ACTION_GO);
    }

    /**
     * 自动清除错误提示
     */
    public static void autoClearError(@NonNull final EditText et) {
        Preconditions.checkNotNull(et, "MaterialEditText == null");

        RxTextView.textChanges(et).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence sequence) {
                if (!TextUtils.isEmpty(et.getError())) {
                    et.setError(null);
                }
            }
        });
    }

    /**
     * 多次点击检测
     *
     * @param view               被点击 View
     * @param maxIntervalMillis  点击最大时间间隔
     * @param minComboTimesCared 最小连击次数
     * @return 符合要求的连击事件
     */
    public static Observable<Void> multiClickDetector(View view, final long maxIntervalMillis, final int minComboTimesCared) {
        return RxView.clicks(view).map(new Func1<Void, Integer>() {
            @Override
            public Integer call(Void aVoid) {
                return 1;
            }
        }).timestamp().scan(new Func2<Timestamped<Integer>, Timestamped<Integer>, Timestamped<Integer>>() {
            @Override
            public Timestamped<Integer> call(Timestamped<Integer> lastOne, Timestamped<Integer> thisOne) {
                if (thisOne.getTimestampMillis() - lastOne.getTimestampMillis() <= maxIntervalMillis) {
                    return new Timestamped<>(thisOne.getTimestampMillis(), lastOne.getValue() + 1);
                } else {
                    return new Timestamped<>(thisOne.getTimestampMillis(), 1);
                }
            }
        }).map(new Func1<Timestamped<Integer>, Integer>() {
            @Override
            public Integer call(Timestamped<Integer> timestamped) {
                return timestamped.getValue();
            }
        }).filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer combo) {
                return combo >= minComboTimesCared;
            }
        }).map(new Func1<Integer, Void>() {
            @Override
            public Void call(Integer integer) {
                return null;
            }
        }).throttleFirst(CLICK_PERIOD_TIME_MILLS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
    }

    /**
     * 遍历设置 RadioGroup 中的 RadioButton enable 状态
     */
    public static void setRadioGroupEnable(@NonNull RadioGroup group, final boolean enable) {
        Observable.from(group.getTouchables()).map(new Func1<View, Void>() {
            @Override
            public Void call(View view) {
                view.setEnabled(enable);
                return null;
            }
        }).subscribe();
    }
}
