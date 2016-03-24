package stydying.algo.com.algostudying.ui.controller.game_field;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;

/**
 * Created by Anton on 24.03.2016.
 */
public class PlayController extends GameFieldController {

    public static final String WORLD_DATA_EXTRA = PlayController.class.getName() + "WORLD_DATA_EXTRA";

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
        return null;
    }

    @Override
    public void createOptionsMenu(@NonNull MenuInflater inflater, @NonNull Menu menu) {

    }

    @Override
    public boolean onOptionsMenuItemClick(@NonNull MenuItem menuItem) {
        return false;
    }
}
