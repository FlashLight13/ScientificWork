package stydying.algo.com.algostudying.ui.controller.game_field;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.CommandsExecutionThread;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.views.game_controls.GameNavigationDrawerView;

/**
 * Created by Anton on 24.03.2016.
 */
public class PlayController extends GameFieldController implements CommandsExecutionThread.ExecutionListener {

    public static final String WORLD_DATA_EXTRA = PlayController.class.getName() + "WORLD_DATA_EXTRA";

    private GameNavigationDrawerView navigationView;

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
                    gameWorld.executeCommands((navigationView).getCommands(), this);
                }
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
        final boolean isExecuting = gameWorld.isExecuting();
        final MenuItem menuItem = menu.findItem(R.id.menu_item_control);
        menuItem.setIcon(isExecuting ? R.drawable.ic_stop_white_24dp : R.drawable.ic_play_arrow_white_24dp);
        menuItem.setTitle(isExecuting ? R.string.menu_item_execute : R.string.menu_item_stop);
    }

    @Override
    public void onFinish() {
        activity.supportInvalidateOptionsMenu();
    }
}
