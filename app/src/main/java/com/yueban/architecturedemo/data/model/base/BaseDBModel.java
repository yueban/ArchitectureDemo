package com.yueban.architecturedemo.data.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author yueban
 * @date 2017/8/1
 * @email fbzhh007@gmail.com
 */
public class BaseDBModel extends BaseModel implements Parcelable {
  public static final Creator<BaseDBModel> CREATOR = new Creator<BaseDBModel>() {
    @Override
    public BaseDBModel createFromParcel(Parcel source) {
      return new BaseDBModel(source);
    }

    @Override
    public BaseDBModel[] newArray(int size) {
      return new BaseDBModel[size];
    }
  };

  public BaseDBModel() {
  }

  protected BaseDBModel(Parcel in) {
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }
}
