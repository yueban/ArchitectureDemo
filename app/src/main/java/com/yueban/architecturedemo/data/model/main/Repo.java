package com.yueban.architecturedemo.data.model.main;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.yueban.architecturedemo.data.cache.db.AppDataBase;
import com.yueban.architecturedemo.data.model.base.BaseDBModel;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
@Table(database = AppDataBase.class)
public class Repo extends BaseDBModel implements Parcelable {
    public static final Creator<Repo> CREATOR = new Creator<Repo>() {
        @Override
        public Repo createFromParcel(Parcel source) {
            return new Repo(source);
        }

        @Override
        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };
    /**
     * id : 97594144
     * name : ahbottomnavigation
     * full_name : yueban/ahbottomnavigation
     */

    @PrimaryKey @SerializedName("id") public int id;
    @Column @SerializedName("name") public String name;
    @Column @SerializedName("full_name") public String fullName;

    public Repo() {
    }

    protected Repo(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        this.fullName = in.readString();
    }

    @Override
    public String toString() {
        return "Repo{" + "id=" + id + ", name='" + name + '\'' + ", fullName='" + fullName + '\'' + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
    }
}
