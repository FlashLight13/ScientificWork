package stydying.algo.com.algostudying.game.objects;

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
}