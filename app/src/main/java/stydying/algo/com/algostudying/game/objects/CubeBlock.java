package stydying.algo.com.algostudying.game.objects;

/**
 * Created by anton on 01.09.15.
 */
public class CubeBlock extends GameObject {

    public CubeBlock() {
    }

    public CubeBlock(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    protected String getModelName() {
        return ModelNames.CUBE;
    }
}
