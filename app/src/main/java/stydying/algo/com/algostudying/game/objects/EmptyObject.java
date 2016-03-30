package stydying.algo.com.algostudying.game.objects;

import stydying.algo.com.algostudying.R;

/**
 * Created by anton on 27.06.15.
 */
public class EmptyObject extends GameObject {

    public EmptyObject() {
    }

    public EmptyObject(GameObject object) {
        super(object);
    }

    public EmptyObject(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    protected String getModelName() {
        return ModelNames.EMPTY;
    }

    @Override
    public GameObject cloneObject() {
        return new EmptyObject(this);
    }

    @Override
    public int getDescription() {
        return R.string.empty_obj_title;
    }
}
