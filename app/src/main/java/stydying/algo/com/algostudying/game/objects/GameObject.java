package stydying.algo.com.algostudying.game.objects;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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

    private State state;

    public enum State {
        IDLE, SELECTED, INVISIBLE
    }

    public GameObject() {
    }

    public GameObject(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public GameObject(GameObject object) {
        this.x = object.x;
        this.y = object.y;
        this.z = object.z;
        this.angle = object.angle;
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
        }
    }

    public Vector3i getWorldCoordinates() {
        return new Vector3i(x / GameWorld.GAME_CELL_MULTIPLIER,
                y / GameWorld.GAME_CELL_MULTIPLIER,
                z / GameWorld.GAME_CELL_MULTIPLIER);
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
        return state == State.SELECTED;
    }

    public boolean isVisible() {
        return this.state == State.INVISIBLE;
    }

    public GameObject setVisible(boolean isVisible) {
        this.state = isVisible ? State.IDLE : State.INVISIBLE;
        return this;
    }

    public GameObject setSelected(boolean selected) {
        state = selected ? State.SELECTED : State.IDLE;
        return this;
    }

    public abstract GameObject cloneObject();

    @StringRes
    public abstract int getDescription();
}