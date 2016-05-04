package stydying.algo.com.algostudying.game;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import stydying.algo.com.algostudying.errors.BaseException;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.conditions.WinCondition;

/**
 * Created by Anton on 03.05.2016.
 */
public class CommandsExecutionThread extends Thread {

    private List<Command> commandList;
    private GameWorld gameWorld;
    private WinCondition winCondition;
    private Context context;

    public CommandsExecutionThread(Context context, WinCondition winCondition, GameWorld gameWorld) {
        super();
        this.gameWorld = gameWorld;
        this.context = context;
        this.winCondition = winCondition;
    }

    public CommandsExecutionThread setCommands(List<Command> commands) {
        synchronized (this) {
            this.commandList = commands;
        }
        return this;
    }

    @Override
    public void run() {
        // TODO may be implement some animation here
        synchronized (this) {
            for (Command command : commandList) {
                if (isInterrupted()) {
                    gameWorld.reset();
                    return;
                }
                if (!command.perform(gameWorld)) {
                    gameWorld.reset();
                    showError(new BaseException(BaseException.ERROR_EXECUTING_ALGO));
                    return;
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    gameWorld.reset();
                    return;
                }
            }
            if (!winCondition.win(gameWorld)) {
                showError(new BaseException(BaseException.ERROR_NOT_ALL_COLLECTED));
                gameWorld.reset();
            }
        }
    }

    private void showError(BaseException exception) {
        Toast.makeText(context, exception.getMessageRes(), Toast.LENGTH_SHORT).show();
    }
}
