package stydying.algo.com.algostudying.game.commands;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;

/**
 * Created by anton on 27.06.15.
 */
public class TurnLeftCommand extends Command {

    public TurnLeftCommand() {
        super(R.string.turn_left_command,
                R.drawable.ic_turn_left_black_24dp,
                R.string.turn_left_command_desc);
    }

    public TurnLeftCommand(Command command) {
        super(command);
    }

    @Override
    public boolean perform(GameWorld gameWorld) throws InterruptedException {
        gameWorld.getPlayer().turnToTheLeft();
        return true;
    }

    @Override
    public Command cloneCommand() {
        return new TurnLeftCommand(this);
    }
}
