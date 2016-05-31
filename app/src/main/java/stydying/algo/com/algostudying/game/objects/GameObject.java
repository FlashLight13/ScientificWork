package stydying.algo.com.algostudying.game.objects;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.logic.managers.ModelsManager;
import stydying.algo.com.algostudying.ui.graphics.Model;
import stydying.algo.com.algostudying.utils.vectors.Vector3f;
import stydying.algo.com.algostudying.utils.vectors.Vector3i;

/**
 * Created by anton on 27.06.15.
 */
public abstract class GameObject {

    private float x;
    private float y;
    private float z;
    private float angle;

    private State state;

    public enum State {
        IDLE, SELECTED, INVISIBLE
    }

    public GameObject() {
    }

    public GameObject(float x, float y, float z) {
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

    public Vector3i getWorldCoordinates() {
        return new Vector3i((int) x / GameWorld.GAME_CELL_MULTIPLIER,
                (int) y / GameWorld.GAME_CELL_MULTIPLIER,
                (int) z / GameWorld.GAME_CELL_MULTIPLIER);
    }

    public void setWorldCoordinates(int x, int y) {
        this.x = x * GameWorld.GAME_CELL_MULTIPLIER;
        this.y = y * GameWorld.GAME_CELL_MULTIPLIER;
    }

    public GameObject setCoordinates(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f getCoordinates() {
        return new Vector3f(x, y, z);
    }

    public Vector3f getDisplayingCoordinates() {
        return new Vector3f(x, y, z);
    }

    public float getAngle() {
        return this.angle;
    }

    public GameObject setAngle(float angle) {
        this.angle = angle;
        return this;
    }

    @Nullable
    public Model getModel() {
        return ModelsManager.getInstance().getModel(getModelName());
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