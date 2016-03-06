package stydying.algo.com.algostudying.utils.caches;

/**
 * Created by anton on 10.08.15.
 */
public interface Cache<T> {
    String TAG = "MemoryCache";

    T get(String key);

    void put(String key, T value);

    void clear();
}
