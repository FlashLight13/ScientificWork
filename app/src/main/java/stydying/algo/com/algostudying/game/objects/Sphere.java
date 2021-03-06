package stydying.algo.com.algostudying.game.objects;

import android.support.annotation.Nullable;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 20.03.2016.
 */
public class Sphere extends GameObject {

    public Sphere(float x, float y, float z) {
        super(x, y, z);
    }

    public Sphere() {
    }

    public Sphere(GameObject object) {
        super(object);
    }

    @Nullable
    @Override
    protected String getModelName() {
        return ModelNames.SPHERE;
    }

    @Override
    public GameObject cloneObject() {
        return new Sphere(this);
    }

    @Override
    public int getDescription() {
        return R.string.sphere_obj_title;
    }
}