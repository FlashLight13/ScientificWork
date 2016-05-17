package stydying.algo.com.algostudying.game.conditions;

import stydying.algo.com.algostudying.game.GameWorld;

/**
 * Created by Anton on 04.05.2016.
 */
public class BaseWinCondition implements WinCondition {

    @Override
    public boolean win(GameWorld gameWorld) {
        return gameWorld.getSpheresCount() == gameWorld.getCollectedSpheres().size();
    }
}
