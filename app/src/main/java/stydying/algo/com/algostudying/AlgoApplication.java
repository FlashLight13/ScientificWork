package stydying.algo.com.algostudying;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;

import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.utils.StreamUtils;
import stydying.algo.com.algostudying.utils.caches.MapCache;

/**
 * Created by Anton on 04.06.2015.
 */
public class AlgoApplication extends Application {

    private LoginManager loginManager;

    private MapCache mapCache;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);

        initManagers();
        initCaches();
        initExternalStorage();
    }

    private void initExternalStorage() {
        if (StreamUtils.isExternalStorageWritable()) {
            File externalDir = getExternalCacheDir();
            if (externalDir != null) {
                if (!externalDir.exists()) {
                    if (externalDir.mkdirs()) {
                        Log.d("DebugLogs", "External dirs are created");
                    } else {
                        Log.d("DebugLogs", "External dirs are not created");
                    }
                } else {
                    Log.d("DebugLogs", "External dirs exist");
                }
            }
        }
    }

    private void initManagers() {
        this.loginManager = new LoginManager(this);
    }

    private void initCaches() {
        this.mapCache = new MapCache(this);
    }

    @NonNull
    public LoginManager getLoginManager() {
        return this.loginManager;
    }

    @NonNull
    public MapCache getMapCache() {
        return this.mapCache;
    }
}
