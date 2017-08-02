package com.yueban.architecturedemo.data.cache.db;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * @author yueban
 * @date 2017/7/31
 * @email fbzhh007@gmail.com
 */
@Database(name = AppDataBase.NAME, version = AppDataBase.VERSION)
public abstract class AppDataBase {
    static final int VERSION = 1;
    static final String NAME = "database_app";

    public static DatabaseDefinition getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final DatabaseDefinition INSTANCE = FlowManager.getDatabase(AppDataBase.class);
    }
}
