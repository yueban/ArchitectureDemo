package com.yueban.architecturedemo.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yueban.architecturedemo.R;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public class DialogBuilder extends MaterialDialog.Builder {
  private boolean showPositiveButton = true;
  private boolean showNegativeButton = true;

  public DialogBuilder(@NonNull Context context) {
    super(context);
  }

  public DialogBuilder showPositiveButton(boolean showPositiveButton) {
    this.showPositiveButton = showPositiveButton;
    return this;
  }

  public DialogBuilder showNegativeButton(boolean showNegativeButton) {
    this.showNegativeButton = showNegativeButton;
    return this;
  }

  @Override
  public MaterialDialog build() {
    if (showPositiveButton && TextUtils.isEmpty(positiveText)) {
      positiveText(R.string.confirm);
    }
    if (showNegativeButton && TextUtils.isEmpty(negativeText)) {
      negativeText(R.string.cancel);
    }
    if (!positiveColorSet) {
      positiveColorRes(R.color.colorPrimary);
    }
    if (!negativeColorSet) {
      negativeColorRes(R.color.text_gray_3);
    }
    if (!neutralColorSet) {
      neutralColorRes(R.color.text_red_warn);
    }
    return super.build();
  }
}
