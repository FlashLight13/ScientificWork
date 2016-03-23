package stydying.algo.com.algostudying.utils.caches;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import stydying.algo.com.algostudying.AlgoApplication;

/**
 * Created by Anton on 27.02.2016.
 */
public class MapCache implements Cache<String[][][]> {

    private static final String MAP_FILE_TYPE = ".map";
    private static final String LOG_TAG = "MapCache";

    private BaseFileCache baseFileCache;

    public MapCache(Context context) {
        this.baseFileCache = new BaseFileCache(context);
    }

    @Override
    public String[][][] get(String key) {
        try {
            return new Gson().fromJson(new InputStreamReader(new ByteArrayInputStream(baseFileCache.get(key + MAP_FILE_TYPE))),
                    new TypeToken<String[][][]>() {
                    }.getType());
        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to get map for " + key, e);
        }
        return null;
    }

    @Override
    public void put(String key, String[][][] value) {
        try {
            String json = new Gson().toJson(value);
            baseFileCache.put(key + MAP_FILE_TYPE, json.getBytes());
        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to put map for " + key, e);
        }
    }

    @Override
    public void clear() {
        baseFileCache.clear();
    }

    public static MapCache getInstance(Context context) {
        return ((AlgoApplication) context.getApplicationContext()).getMapCache();
    }
}
