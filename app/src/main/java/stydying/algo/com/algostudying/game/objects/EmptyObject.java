package stydying.algo.com.algostudying.game.objects;

/**
 * Created by anton on 27.06.15.
 */
public class EmptyObject extends GameObject {

    public EmptyObject() {
    }

    public EmptyObject(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    protected String getModelName() {
        return ModelNames.EMPTY;
    }
}
