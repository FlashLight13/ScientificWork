package stydying.algo.com.algostudying.game.objects;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.ui.graphics.Model;
import stydying.algo.com.algostudying.utils.OBJLoader;
import stydying.algo.com.algostudying.utils.caches.ModelMemoryCache;
import stydying.algo.com.algostudying.utils.caches.PathsHolder;
import stydying.algo.com.algostudying.utils.vectors.Vector3f;
import stydying.algo.com.algostudying.utils.vectors.Vector3i;

/**
 * Created by anton on 27.06.15.
 */
public abstract class GameObject {

    private int x;
    private int y;
    private int z;
    private float angle;

    private boolean isSelected = false;

    public GameObject() {
    }

    public GameObject(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void initDrawing(Context context) throws IOException {
        String modelName = getModelName();
        if (!TextUtils.isEmpty(modelName)) {
            String key = getClass().getName();
            ModelMemoryCache cache = ModelMemoryCache.getInstance();
            Model model = cache.get(key);
            if (model == null) {
                model = OBJLoader.loadTexturedModel(new File(PathsHolder.getResDir(context) + "//" + getModelName().toLowerCase() + "//model.obj"));
                cache.put(key, model);
            }
            model.init();
        }
    }

    public void move() {
    }

    public Vector3f getWorldCoordinates() {
        return new Vector3f(x / GameWorld.GAME_CELL_MULTIPLIER, y / GameWorld.GAME_CELL_MULTIPLIER, z / GameWorld.GAME_CELL_MULTIPLIER);
    }

    public void setWorldCoordinates(int x, int y) {
        this.x = x * GameWorld.GAME_CELL_MULTIPLIER;
        this.y = y * GameWorld.GAME_CELL_MULTIPLIER;
    }

    public GameObject setCoordinates(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3i getCoordinates() {
        return new Vector3i(x, y, z);
    }

    public Vector3f getDisplayingCoordinates() {
        return new Vector3f(x, y, z);
    }

    public float getAngle() {
        return this.angle;
    }

    @Nullable
    public Model getModel() {
        return ModelMemoryCache.getInstance().get(getClass().getName());
    }

    @Nullable
    protected abstract String getModelName();

    public boolean isSelected() {
        return isSelected;
    }

    public GameObject setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }
}