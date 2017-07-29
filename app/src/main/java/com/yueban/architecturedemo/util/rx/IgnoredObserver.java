package com.yueban.architecturedemo.util.rx;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class IgnoredObserver<T> extends SimpleObserver<T> {
    @Override
    public void onNext(T t) {

    }
}
