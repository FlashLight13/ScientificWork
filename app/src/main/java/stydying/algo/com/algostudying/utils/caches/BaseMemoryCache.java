package stydying.algo.com.algostudying.utils.caches;

import android.support.v4.util.LruCache;

/**
 * Created by anton on 09.08.15.
 */
public class BaseMemoryCache implements Cache<byte[]> {
    protected LruCache<String, byte[]> lruCache;

    public BaseMemoryCache(int size) {
        lruCache = new LruCache<>(size);
    }

    synchronized public void put(String key, byte[] data) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null");
        }
        lruCache.put(key, data);
    }

    synchronized public byte[] get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can't be null");
        }
        return lruCache.get(key);
    }

    synchronized public void clear() {
        lruCache.evictAll();
    }
}
