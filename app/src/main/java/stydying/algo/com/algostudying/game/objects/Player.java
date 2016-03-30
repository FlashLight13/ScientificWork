package stydying.algo.com.algostudying.game.objects;

import stydying.algo.com.algostudying.R;

/**
 * Created by anton on 27.06.15.
 */
public class Player extends GameObject {

    public enum LookDirection {
        STRAIT, LEFT, BACK, RIGHT
    }

    public enum State {
        MOVING, IDLE
    }

    private int lookDirection;

    public Player() {
    }

    public Player(GameObject object) {
        super(object);
        this.lookDirection = ((Player) object).lookDirection;
    }

    public Player(int x, int y, int z) {
        super(x, y, z);
    }

    public LookDirection getSightDirection() {
        return LookDirection.values()[lookDirection];
    }

    public void turnToTheLeft() {
        if (lookDirection == LookDirection.values().length - 1) {
            lookDirection = 0;
        } else {
            lookDirection++;
        }
    }

    public void turnToTheRight() {
        if (lookDirection == 0) {
            lookDirection = LookDirection.values().length - 1;
        } else {
            lookDirection--;
        }
    }

    @Override
    protected String getModelName() {
        return ModelNames.PLAYER;
    }

    @Override
    public GameObject cloneObject() {
        return new Player(this);
    }

    @Override
    public int getDescription() {
        return R.string.player_obj_title;
    }
}