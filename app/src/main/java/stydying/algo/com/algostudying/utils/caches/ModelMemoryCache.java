package stydying.algo.com.algostudying.utils.caches;

import java.util.HashMap;
import java.util.Map;

import stydying.algo.com.algostudying.ui.graphics.Model;

/**
 * Created by anton on 10.08.15.
 */
public class ModelMemoryCache implements Cache<Model> {

    private static ModelMemoryCache instance;
    private Map<String, Model> map = new HashMap<>();

    synchronized public static ModelMemoryCache getInstance() {
        if (instance == null) {
            instance = new ModelMemoryCache();
        }
        return instance;
    }

    @Override
    public Model get(String key) {
        return map.get(key);
    }

    @Override
    public void put(String key, Model value) {
        map.put(key, value);
    }

    @Override
    public void clear() {
        map.clear();
    }
}
