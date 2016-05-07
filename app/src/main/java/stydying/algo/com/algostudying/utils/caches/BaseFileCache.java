package stydying.algo.com.algostudying.utils.caches;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import stydying.algo.com.algostudying.AlgoApplication;

/**
 * Created by Anton on 22.03.2016.
 */
public class BaseFileCache implements Cache<byte[]> {

    private static final String LOG_TAG = "BaseFileCache";
    private String cachePath;

    public BaseFileCache(Context context) {
        cachePath = PathsHolder.getMapsDir(context);
    }

    @Nullable
    @Override
    public byte[] get(String key) {
        try {
            validateDir();
            return IOUtils.toByteArray(new FileInputStream(cachePath + "//"+ key));
        } catch (IOException e) {
            Log.d(LOG_TAG, "error", e);
            return null;
        }
    }

    @Override
    public void put(String key, byte[] value) {
        try {
            validateDir();
            FileUtils.writeByteArrayToFile(new File(cachePath + "//"+ key), value);
        } catch (IOException e) {
            Log.d(LOG_TAG, "error", e);
        }
    }

    @Override
    public void clear() {
        try {
            validateDir();
            for (File current : new File(cachePath).listFiles()) {
                if (!current.delete()) {
                    Log.d(LOG_TAG, "Failed to delete " + current.getName());
                }
            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "error", e);
        }
    }

    public boolean exists(String key) {
        return new File(cachePath + "//"+ key).exists();
    }

    private void validateDir() throws IOException {
        File file = new File(cachePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("Failed to create dirs");
            }
        }
    }

    public static BaseFileCache getInstance(Context context) {
        return ((AlgoApplication) context.getApplicationContext()).getBaseFileCache();
    }
}
