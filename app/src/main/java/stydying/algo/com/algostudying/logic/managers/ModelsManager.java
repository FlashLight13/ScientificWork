package stydying.algo.com.algostudying.logic.managers;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stydying.algo.com.algostudying.game.objects.ModelNames;
import stydying.algo.com.algostudying.ui.graphics.Model;
import stydying.algo.com.algostudying.utils.OBJLoader;

/**
 * Created by Anton on 06.05.2016.
 */
public class ModelsManager {
    private static final String MODELS_ASSETS_DIR = "models";
    private static final ModelsManager INSTANCE = new ModelsManager();

    private Map<String, Model> modelMemoryCache;
    private List<String> modelNames;

    public ModelsManager() {
        modelMemoryCache = new HashMap<>();

        modelNames = new ArrayList<>(3);
        modelNames.add(ModelNames.CUBE);
        modelNames.add(ModelNames.PLAYER);
        modelNames.add(ModelNames.SPHERE);
    }

    public void init(final Context context) throws IOException {
        for (String modelName : modelNames) {
            Model model = modelMemoryCache.get(modelName);
            if (model == null) {
                model = OBJLoader.loadTexturedModel(new OBJLoader.ResourceProvider() {
                    @Override
                    public InputStream open(String name) throws IOException {
                        return context.getAssets().open(MODELS_ASSETS_DIR + "/" + name);
                    }
                }, modelName.toLowerCase());
                modelMemoryCache.put(modelName, model);
            }
        }
    }

    public Model getModel(String name) {
        return modelMemoryCache.get(name);
    }

    public static ModelsManager getInstance() {
        return INSTANCE;
    }
}
