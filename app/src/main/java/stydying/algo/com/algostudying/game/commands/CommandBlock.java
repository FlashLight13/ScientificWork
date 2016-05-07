package stydying.algo.com.algostudying.game.commands;

import java.util.ArrayList;
import java.util.List;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.SettingsController;

/**
 * Created by anton on 27.06.15.
 */
public class CommandBlock extends Command {

    private List<Command> commands = new ArrayList<>();

    public CommandBlock() {
        super(R.string.cycle_command, R.drawable.ic_repeat_black_24dp, R.string.cycle_command_desc);
    }

    public CommandBlock(Command command) {
        super(command);
        if (command instanceof CommandBlock) {
            this.commands = new ArrayList<>(((CommandBlock) command).commands);
        }
    }

    @Override
    public boolean perform(GameWorld gameWorld) throws InterruptedException {
        for (Command currentCommand : commands) {
            if (!currentCommand.perform(gameWorld)) {
                return false;
            }
            Thread.sleep(SettingsController.getInstance().getBetweenOperationsDelay());
        }
        return true;
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void clear() {
        this.commands.clear();
    }

    public void remove(int index) {
        this.commands.remove(index);
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    @Override
    public Command cloneCommand() {
        return new CommandBlock(this);
    }
}
