package com.yueban.architecturedemo.data.cache.db;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import java.util.List;

/**
 * @author yueban
 * @date 2017/8/1
 * @email fbzhh007@gmail.com
 */
public class DBHelper {
  private DBHelper() {
  }

  public static <T extends BaseModel> Single<Long> count(Class<T> tClass) {
    return RXSQLite.rx(SQLite.select().from(tClass)).count();
  }

  public static <T extends BaseModel> Single<Long> count(Class<T> tClass, SQLOperator... conditions) {
    return RXSQLite.rx(SQLite.select().from(tClass).where(conditions)).count();
  }

  public static <T extends BaseModel> Single<List<T>> select(Class<T> tClass) {
    return RXSQLite.rx(SQLite.select().from(tClass)).queryList();
  }

  public static <T extends BaseModel> Single<List<T>> select(Class<T> tClass, SQLOperator... conditions) {
    return RXSQLite.rx(SQLite.select().from(tClass).where(conditions)).queryList();
  }

  public static <T extends BaseModel> Single<List<T>> select(Class<T> tClass, int limit, int offset, SQLOperator... conditions) {
    return RXSQLite.rx(SQLite.select().from(tClass).where(conditions).limit(limit).offset(offset)).queryList();
  }

  public static <T extends BaseModel> Maybe<T> selectSingle(Class<T> tClass, SQLOperator... conditions) {
    return RXSQLite.rx(SQLite.select().from(tClass).where(conditions)).querySingle();
  }

  public static <T extends BaseModel> Single<List<T>> orderBy(Class<T> tClass, IProperty property, boolean ascending,
      SQLOperator... conditions) {
    return RXSQLite.rx(SQLite.select().from(tClass).where(conditions).orderBy(property, ascending)).queryList();
  }

  public static <T extends BaseModel> Single<List<T>> groupBy(Class<T> tClass, IProperty... properties) {
    return RXSQLite.rx(SQLite.select().from(tClass).groupBy(properties)).queryList();
  }

  //public static <T extends BaseModel> Single<List<T>> groupBy(Class<T> tClass, SQLOperator... havingCondition,
  //    IProperty... properties) {
  //    return RXSQLite.rx(SQLite.select().from(tClass).groupBy(properties).having(havingCondition)).queryList();
  //    return Observable.just(null).flatMap(new Func1<Object, Observable<List<T>>>() {
  //        @Override
  //        public Observable<List<T>> call(Object o) {
  //            return Observable.just(SQLite.select().from(tClass).groupBy(ts).having(havingCondition).queryList());
  //        }
  //    });
  //}

  public static <T extends BaseModel> Single<List<T>> groupBy(Class<T> tClass, SQLOperator whereCondition,
      IProperty... properties) {
    return RXSQLite.rx(SQLite.select().from(tClass).where(whereCondition).groupBy(properties)).queryList();
  }

  public static <T extends BaseModel> Observable<Boolean> save(final T t) {
    return Observable.create(new ObservableOnSubscribe<Boolean>() {
      @Override
      public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> e) throws Exception {
        ProcessModelTransaction<T> transaction =
            new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<T>() {
              @Override
              public void processModel(T t, DatabaseWrapper wrapper) {
                t.save();
              }
            }).add(t).build();
        executeTransaction(e, transaction);
      }
    });
  }

  public static <T extends BaseModel> Observable<Boolean> save(final List<T> list) {
    return Observable.create(new ObservableOnSubscribe<Boolean>() {
      @Override
      public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> e) throws Exception {
        ProcessModelTransaction<T> transaction =
            new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<T>() {
              @Override
              public void processModel(T t, DatabaseWrapper wrapper) {
                t.save();
              }
            }).addAll(list).build();
        executeTransaction(e, transaction);
      }
    });
  }

  public static <T extends BaseModel> Observable<Boolean> delete(final T t) {
    return Observable.create(new ObservableOnSubscribe<Boolean>() {
      @Override
      public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> e) throws Exception {
        ProcessModelTransaction<T> transaction =
            new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<T>() {
              @Override
              public void processModel(T t, DatabaseWrapper wrapper) {
                t.delete();
              }
            }).add(t).build();
        executeTransaction(e, transaction);
      }
    });
  }

  public static <T extends BaseModel> Observable<Boolean> delete(final List<T> list) {
    return Observable.create(new ObservableOnSubscribe<Boolean>() {
      @Override
      public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> e) throws Exception {
        ProcessModelTransaction<T> transaction =
            new ProcessModelTransaction.Builder<>(new ProcessModelTransaction.ProcessModel<T>() {
              @Override
              public void processModel(T t, DatabaseWrapper wrapper) {
                t.delete();
              }
            }).addAll(list).build();
        executeTransaction(e, transaction);
      }
    });
  }

  public static <T extends BaseModel> Observable<Boolean> delete(final Class<T> tClass) {
    return Observable.create(new ObservableOnSubscribe<Boolean>() {
      @Override
      public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> e) throws Exception {
        FastStoreModelTransaction<T> transaction =
            FastStoreModelTransaction.deleteBuilder(FlowManager.getModelAdapter(tClass)).build();
        executeTransaction(e, transaction);
      }
    });
  }

  private static void executeTransaction(@io.reactivex.annotations.NonNull final ObservableEmitter<Boolean> e,
      ITransaction transaction) {
    AppDataBase.getInstance().beginTransactionAsync(transaction).success(new Transaction.Success() {
      @Override
      public void onSuccess(@NonNull Transaction transaction) {
        if (!e.isDisposed()) {
          e.onNext(true);
          e.onComplete();
        }
      }
    }).error(new Transaction.Error() {
      @Override
      public void onError(@NonNull Transaction transaction, @NonNull Throwable error) {
        if (!e.isDisposed()) {
          e.onError(error);
        }
      }
    }).build().execute();
  }
}
