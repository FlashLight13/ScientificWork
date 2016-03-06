package stydying.algo.com.algostudying.game.commands;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.objects.Player;

/**
 * Created by anton on 27.06.15.
 */
public class TurnLeftCommand extends Command {

    public TurnLeftCommand() {
        super(R.string.turn_left_command,
                R.drawable.ic_turn_left_black_24dp,
                R.string.turn_left_command_desc);
    }

    @Override
    public void perform(GameWorld gameWorld, Player player) {
        player.turnToTheLeft();
    }
}
