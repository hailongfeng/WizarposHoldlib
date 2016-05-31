package com.wizarpos.holdlib.db;

import android.text.TextUtils;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.File;

/**
 * Created by Harlen on 2016/4/15.
 */
public class DBHelp {
    private static DBHelp dbHelp;
    private DbManager.DaoConfig daoConfig;

    private DBHelp() {
    }

    public static synchronized DBHelp getInstance() {
        if (dbHelp == null) {
            dbHelp = new DBHelp();
        }
        return dbHelp;
    }

    public synchronized void initDB(String dbName, String dbDir, int DbVersion, DbManager.DbUpgradeListener dbUpgradeListener) {
        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig();
            daoConfig.setDbName(dbName);
            // 不设置dbDir时, 默认存储在app的私有目录.
            if (!TextUtils.isEmpty(dbDir)) {
                daoConfig.setDbDir(new File(dbDir)); // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            }
            daoConfig.setDbVersion(DbVersion);
            daoConfig.setDbOpenListener(new DbManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(DbManager db) {
                            // 开启WAL, 对写入加速提升巨大
                            db.getDatabase().enableWriteAheadLogging();
                        }
                    });
            daoConfig.setDbUpgradeListener(dbUpgradeListener);
            x.getDb(daoConfig);
            LogUtil.d("数据库初始化");
        } else {
            LogUtil.e("数据库已经初始化过！");
        }

    }

    public DbManager getDbManager() {
        DbManager db = x.getDb(daoConfig);
        return db;
    }
}
