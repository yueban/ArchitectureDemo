package com.yueban.architecturedemo.util.rx;

import com.yueban.architecturedemo.util.L;
import rx.Subscriber;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public abstract class SimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        L.e(e);
    }
}
