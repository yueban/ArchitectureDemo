package com.yueban.architecturedemo.util.rx;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.yueban.architecturedemo.util.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class RxViewUtil {
  /**
   * 控件点击时间周期
   */
  private static final int CLICK_PERIOD_TIME_MILLS = 300;

  private RxViewUtil() {
    throw new AssertionError("No instances.");
  }

  @CheckResult
  @NonNull
  public static Observable<Object> clicks(@NonNull View view) {
    return RxView.clicks(view).throttleFirst(CLICK_PERIOD_TIME_MILLS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
  }

  /**
   * 检查多个文本框,同时满足某种条件
   *
   * @param checker 检查器
   * @param views   多个文本框 eg: EditText
   */
  @NonNull
  public static Observable<Boolean> multiTextCheck(@NonNull final Function<CharSequence, Boolean> checker,
      @NonNull TextView... views) {

    Preconditions.checkNotNull(checker, "checker == null");
    Preconditions.checkNotNull(views, "views == null");

    List<Observable<CharSequence>> observableList = new ArrayList<>();
    for (TextView view : views) {
      observableList.add(RxTextView.textChanges(view));
    }

    return Observable.combineLatest(observableList, new Function<Object[], Boolean>() {
      @Override
      public Boolean apply(@io.reactivex.annotations.NonNull Object[] objects) throws Exception {
        boolean pass;
        for (Object arg : objects) {
          // 校验类型 & 外部判断
          pass = arg instanceof CharSequence && checker.apply(((CharSequence) arg));
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
    return multiTextCheck(new Function<CharSequence, Boolean>() {
      @Override
      public Boolean apply(@io.reactivex.annotations.NonNull CharSequence sequence) throws Exception {
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

    RxTextView.editorActions(view, new Predicate<Integer>() {
      @Override
      public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
        return integer == actionId;
      }
    }).subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) throws Exception {
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

    RxTextView.textChanges(et).subscribe(new Consumer<CharSequence>() {
      @Override
      public void accept(CharSequence sequence) throws Exception {
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
    return RxView.clicks(view).map(new Function<Object, Integer>() {
      @Override
      public Integer apply(@io.reactivex.annotations.NonNull Object o) throws Exception {
        return 1;
      }
    }).timestamp().scan(new BiFunction<Timed<Integer>, Timed<Integer>, Timed<Integer>>() {
      @Override
      public Timed<Integer> apply(@io.reactivex.annotations.NonNull Timed<Integer> timed,
          @io.reactivex.annotations.NonNull Timed<Integer> timed2) throws Exception {
        if (timed2.time() - timed.time() <= maxIntervalMillis) {
          return new Timed<Integer>(timed.value() + 1, timed2.time(), TimeUnit.MILLISECONDS);
        } else {
          return new Timed<Integer>(1, timed2.time(), TimeUnit.MILLISECONDS);
        }
      }
    }).map(new Function<Timed<Integer>, Integer>() {
      @Override
      public Integer apply(@io.reactivex.annotations.NonNull Timed<Integer> timed) throws Exception {
        return timed.value();
      }
    }).filter(new Predicate<Integer>() {
      @Override
      public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
        return integer >= minComboTimesCared;
      }
    }).map(new Function<Integer, Void>() {
      @Override
      public Void apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
        return null;
      }
    }).throttleFirst(CLICK_PERIOD_TIME_MILLS, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
  }

  /**
   * 遍历设置 RadioGroup 中的 RadioButton enable 状态
   */
  public static void setRadioGroupEnable(@NonNull RadioGroup group, final boolean enable) {
    Observable.fromIterable(group.getTouchables()).map(new Function<View, Object>() {
      @Override
      public Object apply(@io.reactivex.annotations.NonNull View view) throws Exception {
        view.setEnabled(enable);
        return null;
      }
    }).subscribe();
  }
}
