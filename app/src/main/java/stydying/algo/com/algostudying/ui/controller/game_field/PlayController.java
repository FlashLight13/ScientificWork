package stydying.algo.com.algostudying.ui.controller.game_field;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.views.game_controls.GameNavigationDrawerView;

/**
 * Created by Anton on 24.03.2016.
 */
public class PlayController extends GameFieldController {

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
        if (menuItem.getItemId() == R.id.menu_item_execute) {
            if (gameWorld != null && navigationView != null) {
                gameWorld.executeCommands((navigationView).getCommands());
            }
            return true;
        }
        return false;
    }
}
