package com.yueban.architecturedemo.util.eventbus;

import com.yueban.architecturedemo.BuildConfig;
import org.greenrobot.eventbus.EventBus;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class EventBusInstance {
  private final EventBus mBus;

  private EventBusInstance() {
    mBus = EventBus.builder().addIndex(new EventBusIndex()).throwSubscriberException(BuildConfig.DEBUG).build();
  }

  public static EventBus getBus() {
    return Holder.INSTANCE.mBus;
  }

  private static class Holder {
    static final EventBusInstance INSTANCE = new EventBusInstance();
  }
}
