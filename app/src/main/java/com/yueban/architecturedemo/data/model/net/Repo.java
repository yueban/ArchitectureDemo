package com.yueban.architecturedemo.data.model.net;

import com.google.gson.annotations.SerializedName;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class Repo {
    /**
     * id : 97594144
     * name : ahbottomnavigation
     * full_name : yueban/ahbottomnavigation
     */

    @SerializedName("id") public int id;
    @SerializedName("name") public String name;
    @SerializedName("full_name") public String fullName;

    @Override
    public String toString() {
        return "Repo{" + "id=" + id + ", name='" + name + '\'' + ", fullName='" + fullName + '\'' + '}';
    }
}
