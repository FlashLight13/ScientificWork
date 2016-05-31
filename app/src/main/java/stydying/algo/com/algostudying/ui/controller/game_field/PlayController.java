package stydying.algo.com.algostudying.ui.controller.game_field;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.Stat;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.game.CommandsExecutionThread;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.operations.stats.UpdateStatsOperation;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.views.game_controls.GameNavigationDrawerView;

/**
 * Created by Anton on 24.03.2016.
 */
public class PlayController extends GameFieldController implements CommandsExecutionThread.ExecutionListener {

    public static final String WORLD_DATA_EXTRA = PlayController.class.getName() + "WORLD_DATA_EXTRA";

    private GameNavigationDrawerView navigationView;
    private long playStartTime;
    private long accomulatedPlayTime;

    public PlayController(GameFieldActivity activity, OnDataUpdatedListener onDataUpdatedListener) {
        super(activity, onDataUpdatedListener);
        task = activity.getIntent().getParcelableExtra(WORLD_DATA_EXTRA);
    }

    @Override
    public void addCellHeightController(@NonNull ViewGroup rootView) {
    }

    @Override
    public void addNavigationController(@NonNull ViewGroup rootView) {
    }

    @Nullable
    @Override
    public View getNavigationDrawerView(@Nullable GameWorld gameWorld) {
        navigationView = GameNavigationDrawerView.getInstance(activity);
        return navigationView;
    }

    @Override
    public void createOptionsMenu(@NonNull MenuInflater inflater, @NonNull Menu menu) {
        inflater.inflate(R.menu.menu_execute_operations, menu);
    }

    @Override
    public boolean onOptionsMenuItemClick(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_item_control) {
            if (gameWorld != null && navigationView != null) {
                if (gameWorld.isExecuting()) {
                    gameWorld.stopExecuting();
                } else {
                    gameWorld.executeCommands(activity, (navigationView).getCommands(), this);
                }
                activity.supportInvalidateOptionsMenu();
            }
            return true;
        }
        return false;
    }

    @Override
    protected GameFieldActivity.Mode getMode() {
        return GameFieldActivity.Mode.PLAY;
    }

    @Override
    public void prepareOptionsMenu(@NonNull Menu menu) {
        final boolean isExecuting = gameWorld != null && gameWorld.isExecuting();
        final MenuItem menuItem = menu.findItem(R.id.menu_item_control);
        menuItem.setIcon(isExecuting ? R.drawable.ic_stop_white_24dp : R.drawable.ic_play_arrow_white_24dp);
        menuItem.setTitle(isExecuting ? R.string.menu_item_execute : R.string.menu_item_stop);
    }

    @Override
    public void onFinish() {
        gameWorld.stopExecuting();
        activity.supportInvalidateOptionsMenu();
    }

    private void updateAccumulatedTime() {
        accomulatedPlayTime += System.currentTimeMillis() - playStartTime;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(getClass().getName() + "playStartTime", playStartTime);
        updateAccumulatedTime();
        outState.putLong(getClass().getName() + "accomulatedPlayTime", accomulatedPlayTime);
    }

    @Override
    public void onWin(int commandsCount) {
        updateAccumulatedTime();
        User currentUser = LoginManager.getInstance(activity).getCurrentUser();

        Stat stat = new Stat();
        stat.setTaskGroupId(task.getTaskGroup().getId());
        stat.setTaskId(task.getId());
        stat.setTime(accomulatedPlayTime);
        stat.setCommandsCount(commandsCount);
        stat.setTaskName(task.getTitle());
        stat.setGroupName(task.getTaskGroup().getTitle());

        OperationProcessor.executeOperation(activity,
                new UpdateStatsOperation(stat, currentUser.getLogin(), currentUser.getPass()));
        new AlertDialog.Builder(activity)
                .setTitle(R.string.message_title_win)
                .setMessage(activity.getString(R.string.message_win, commandsCount))
                .setPositiveButton(R.string.ok, null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        activity.finish();
                    }
                })
                .setCancelable(true)
                .show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        if (savedState != null) {
            playStartTime = savedState.getLong(getClass().getName() + "playS\ntartTime");
            accomulatedPlayTime = savedState.getLong(getClass().getName() + "accomulatedPlayTime");
        } else {
            playStartTime = System.currentTimeMillis();
            accomulatedPlayTime = 0;
        }
    }
}
