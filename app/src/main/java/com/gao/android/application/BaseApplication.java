package com.gao.android.application;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.gao.android.config.AppConfig;
import com.gao.android.db.greendao.DaoMaster;
import com.gao.android.db.greendao.DaoSession;
import com.gao.android.exception.CrashHandler;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.greendao.database.Database;

public class BaseApplication extends Application {

    private Context mContext;

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }

    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = false;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        mContext = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler.getInstance());
        // Logger配置信息
        AppConfig.initLogger();

        mRefWatcher = LeakCanary.install(this);

        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public Context getContext() {
        return mContext;
    }
}
