package stydying.algo.com.algostudying.game.objects;

import stydying.algo.com.algostudying.R;

/**
 * Created by anton on 01.09.15.
 */
public class CubeBlock extends GameObject {

    public CubeBlock() {
    }

    public CubeBlock(float x, float y, float z) {
        super(x, y, z);
    }

    public CubeBlock(GameObject object) {
        super(object);
    }

    @Override
    protected String getModelName() {
        return ModelNames.CUBE;
    }

    @Override
    public GameObject cloneObject() {
        return new CubeBlock(this);
    }

    @Override
    public int getDescription() {
        return R.string.cube_obj_title;
    }
}
