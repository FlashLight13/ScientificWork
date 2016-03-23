package stydying.algo.com.algostudying.game.objects;

import android.support.annotation.Nullable;

/**
 * Created by Anton on 20.03.2016.
 */
public class Sphere extends GameObject {

    public Sphere(int x, int y, int z) {
        super(x, y, z);
    }

    public Sphere() {
    }

    @Nullable
    @Override
    protected String getModelName() {
        return null;
    }
}