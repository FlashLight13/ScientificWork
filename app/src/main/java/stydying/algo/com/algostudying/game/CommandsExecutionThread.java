package stydying.algo.com.algostudying.game;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;

import stydying.algo.com.algostudying.errors.BaseException;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.conditions.WinCondition;

/**
 * Created by Anton on 03.05.2016.
 */
public class CommandsExecutionThread extends Thread {

    public interface ExecutionListener {
        /**
         * Notified on executing all commands. Will not be called if thread was interrupted.
         */
        void onFinish();
    }

    private List<Command> commandList;
    private GameWorld gameWorld;
    private WinCondition winCondition;
    private Context context;
    private ExecutionListener executionListener;

    public CommandsExecutionThread(@NonNull Context context,
                                   @NonNull WinCondition winCondition,
                                   @NonNull GameWorld gameWorld) {
        super();
        this.gameWorld = gameWorld;
        this.context = context;
        this.winCondition = winCondition;
    }

    public CommandsExecutionThread setCommands(@NonNull List<Command> commands) {
        synchronized (this) {
            this.commandList = commands;
        }
        return this;
    }

    public CommandsExecutionThread setExecutionListener(@Nullable ExecutionListener executionListener) {
        this.executionListener = executionListener;
        return this;
    }

    @Override
    public void run() {
        // TODO may be implement some animation here
        synchronized (this) {
            final long delay = SettingsController.getInstance().getBetweenOperationsDelay();
            for (Command command : commandList) {
                try {
                    if (isInterrupted()) {
                        gameWorld.reset();
                        return;
                    }
                    if (!command.perform(gameWorld)) {
                        notifyListener();
                        gameWorld.reset();
                        showError(new BaseException(BaseException.ERROR_EXECUTING_ALGO));
                        return;
                    }
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    gameWorld.reset();
                    return;
                }
            }
            notifyListener();
            if (!winCondition.win(gameWorld)) {
                showError(new BaseException(BaseException.ERROR_NOT_ALL_COLLECTED));
                gameWorld.reset();
            }
        }
    }

    private void notifyListener() {
        if (executionListener != null) {
            executionListener.onFinish();
        }
    }

    private void showError(@NonNull BaseException exception) {
        Toast.makeText(context, exception.getMessageRes(), Toast.LENGTH_SHORT).show();
    }
}
