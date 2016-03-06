package stydying.algo.com.algostudying.utils.caches;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import stydying.algo.com.algostudying.AlgoApplication;
import stydying.algo.com.algostudying.utils.StreamUtils;

/**
 * Created by Anton on 27.02.2016.
 */
public class MapCache implements Cache<String[][][]> {

    private static final String MAP_FILE_TYPE = ".map";
    private static final String LOG_TAG = "MapCache";

    private String cachePath;

    public MapCache(Context context) {
        cachePath = PathsHolder.getMapsDir(context);
    }

    @Override
    public String[][][] get(String key) {
        try {
            validateDir();
            return new Gson().fromJson(new FileReader(cachePath + "//" + key + MAP_FILE_TYPE), new TypeToken<String[][][]>() {
            }.getType());
        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to get map for " + key, e);
        }
        return null;
    }

    @Override
    public void put(String key, String[][][] value) {
        BufferedOutputStream bos = null;
        try {
            validateDir();
            String json = new Gson().toJson(value);
            bos = new BufferedOutputStream(
                    new FileOutputStream(new File(cachePath + "//" + key + MAP_FILE_TYPE)));
            bos.write(json.getBytes());
            bos.flush();
        } catch (Exception e) {
            Log.d(LOG_TAG, "Failed to put map for " + key, e);
        } finally {
            StreamUtils.close(bos);
        }
    }

    @Override
    public void clear() {
        for (File current : new File(cachePath).listFiles()) {
            if (!current.delete()) {
                Log.d(LOG_TAG, "Failed to delete " + current.getName());
            }
        }
    }

    private void validateDir() throws IllegalStateException {
        File file = new File(cachePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IllegalStateException("Failed to create dirs");
            }
        }
    }

    public static MapCache getInstance(Context context) {
        return ((AlgoApplication) context.getApplicationContext()).getMapCache();
    }
}
