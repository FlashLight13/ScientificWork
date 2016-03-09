package stydying.algo.com.algostudying.game.commands;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.objects.Player;

/**
 * Created by anton on 27.06.15.
 */
public class TurnRightCommand extends Command {

    public TurnRightCommand() {
        super(R.string.turn_right_command,
                R.drawable.ic_turn_rigth_black_24dp,
                R.string.turn_right_command_desc);
    }

    public TurnRightCommand(Command command) {
        super(command);
    }

    @Override
    public void perform(GameWorld gameWorld, Player player) {
        player.turnToTheRight();
    }

    @Override
    public Command cloneCommand() {
        return new TurnRightCommand(this);
    }
}
