package stydying.algo.com.algostudying.utils.vectors;

import java.io.Serializable;

/**
 * Created by Anton on 08.02.2016.
 */
public class Vector2i implements Serializable {

    private static final long serialVersionUID = 6709045624659312171L;
    public int x;
    public int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{ x=" + x + ", y=" + y + " }";
    }
}
