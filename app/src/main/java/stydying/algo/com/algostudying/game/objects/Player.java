package stydying.algo.com.algostudying.game.objects;

import stydying.algo.com.algostudying.R;

/**
 * Created by anton on 27.06.15.
 */
public class Player extends GameObject {

    public enum LookDirection {
        STRAIT(0), LEFT(90), BACK(180), RIGHT(270);

        private float angleInDegrees;

        LookDirection(float angleInDegrees) {
            this.angleInDegrees = angleInDegrees;
        }
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

    public Player(float x, float y, float z) {
        super(x, y, z);
    }

    public LookDirection getLookDirection() {
        return LookDirection.values()[lookDirection];
    }

    public Player setLookDirection(LookDirection lookDirection) {
        this.lookDirection = lookDirection.ordinal();
        updateAngle();
        return this;
    }

    public void turnToTheLeft() {
        if (lookDirection == LookDirection.values().length - 1) {
            lookDirection = 0;
        } else {
            lookDirection++;
        }
        updateAngle();
    }

    public void turnToTheRight() {
        if (lookDirection == 0) {
            lookDirection = LookDirection.values().length - 1;
        } else {
            lookDirection--;
        }
        updateAngle();
    }

    private void updateAngle() {
        setAngle(LookDirection.values()[lookDirection].angleInDegrees);
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