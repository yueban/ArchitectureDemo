package com.yueban.architecturedemo.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class CollectionUtil {
    private CollectionUtil() {
        throw new AssertionError();
    }

    /**
     * 将List集合转换为","间隔的String字符串
     */
    public static String toString(List list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object object : list) {
            sb.append(object.toString());
            sb.append(",");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 将","间隔的String字符串转换为List集合
     */
    public static List<String> getListFromString(String str) {
        if (TextUtils.isEmpty(str)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(str.split(",")));
    }

    /**
     * 判断集合的大小
     *
     * @return 集合为 null 时返回 0
     */
    public static int getSize(@Nullable Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    /**
     * 像集合里添加 N 个相同的元素
     *
     * @param c     目标集合
     * @param item  要添加的元素
     * @param count 添加多少个
     * @param <T>   元素类型
     */
    public static <T> void addSameItems(Collection<T> c, T item, int count) {
        for (int i = 0; i < count; i++) {
            c.add(item);
        }
    }

    /**
     * 添加单个元素到 ArrayList 中
     *
     * @param item 要添加的元素
     */
    public static <T> ArrayList<T> singleItemArrayList(T item) {
        ArrayList<T> list = new ArrayList<>();
        list.add(item);
        return list;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection null 或 0 个元素都视为空
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return getSize(collection) == 0;
    }
}