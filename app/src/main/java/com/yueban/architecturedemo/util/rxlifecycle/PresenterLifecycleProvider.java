package com.yueban.architecturedemo.util.rxlifecycle;

import com.trello.rxlifecycle2.OutsideLifecycleException;
import io.reactivex.functions.Function;

/**
 * @author yueban
 * @date 2017/7/27
 * @email fbzhh007@gmail.com
 */
public interface PresenterLifecycleProvider {
  Function<PresenterEvent, PresenterEvent> PRESENTER_LIFECYCLE = new Function<PresenterEvent, PresenterEvent>() {
    @Override
    public PresenterEvent apply(@io.reactivex.annotations.NonNull PresenterEvent event) throws Exception {
      switch (event) {
        case CREATE:
          return PresenterEvent.DESTROY;
        case START:
          return PresenterEvent.STOP;
        case RESUME:
          return PresenterEvent.PAUSE;
        case PAUSE:
          return PresenterEvent.STOP;
        case STOP:
          return PresenterEvent.DESTROY;
        case DESTROY:
          throw new OutsideLifecycleException("Cannot bind to Presenter lifecycle when outside of it.");
        default:
          throw new UnsupportedOperationException("Binding to " + event + " not yet implemented");
      }
    }
  };
}