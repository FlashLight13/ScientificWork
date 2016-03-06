package stydying.algo.com.algostudying.game.objects;

/**
 * Created by Anton on 28.02.2016.
 */
public class ObjectSerializator {

    public static String toJsonString(Class<? extends GameObject> gameObjectClass) {
        return gameObjectClass.getName();
    }

    public static String toJsonString(GameObject gameObject) {
        return gameObject.getClass().getName();
    }

    public static GameObject gameObjectFromJsonString(String jsonString) {
        if (CubeBlock.class.getName().equals(jsonString)) {
            return new CubeBlock();
        }
        if (EmptyObject.class.getName().equals(jsonString)) {
            return new EmptyObject();
        }
        if (Player.class.getName().equals(jsonString)) {
            return new Player();
        }
        throw new IllegalArgumentException("Unknown object: " + jsonString);
    }
}
