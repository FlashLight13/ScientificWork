package stydying.algo.com.algostudying.utils.vectors;

import java.io.Serializable;

/**
 * Created by anton on 27.06.15.
 */
public class Vector3f implements Serializable {
    private static final long serialVersionUID = 620593923150311212L;

    public float x;
    public float y;
    public float z;

    public Vector3f(Vector3f vector3f) {
        this.x = vector3f.x;
        this.y = vector3f.y;
        this.z = vector3f.z;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "{ x=" + x + ", y=" + y + ", z=" + z + " }";
    }
}
