package stydying.algo.com.algostudying.game.commands;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;

/**
 * Created by Anton on 16.05.2016.
 */
public class MoveUpCommand extends Command {

    public MoveUpCommand() {
        super(R.string.move_command_up, R.drawable.ic_file_upload_black_48dp, R.string.move_command_up_desc);
    }

    public MoveUpCommand(Command command) {
        super(command);
    }

    @Override
    public boolean perform(GameWorld gameWorld) throws InterruptedException {
        return false;
    }

    @Override
    public Command cloneCommand() {
        return null;
    }
}
