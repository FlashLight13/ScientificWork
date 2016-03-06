package stydying.algo.com.algostudying.utils.vectors;

import java.io.Serializable;

/**
 * Created by Anton on 07.07.2015.
 */
public class Vector2f implements Serializable {
    private static final long serialVersionUID = -4712678569529088415L;

    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{ x=" + x + ", y=" + y + " }";
    }
}
