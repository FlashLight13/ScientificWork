package stydying.algo.com.algostudying.ui.controller.game_field;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.errors.BaseException;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.objects.CubeBlock;
import stydying.algo.com.algostudying.game.objects.EmptyObject;
import stydying.algo.com.algostudying.game.objects.Player;
import stydying.algo.com.algostudying.game.objects.Sphere;
import stydying.algo.com.algostudying.logic.creation.GameFieldCreationController;
import stydying.algo.com.algostudying.ui.activities.EditUserTasksActivity;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.views.game_controls.GameFieldCellsHeightControl;
import stydying.algo.com.algostudying.ui.views.game_controls.GameFieldSelectControl;
import stydying.algo.com.algostudying.ui.views.game_controls.GameObjectsListView;

/**
 * Created by Anton on 24.03.2016.
 */
public class EditingController extends GameFieldController {

    private GameFieldSelectControl gameFieldSelectControl;
    private GameFieldCellsHeightControl gameFieldCellsHeightControl;

    public EditingController(GameFieldActivity activity, OnDataUpdatedListener onDataUpdatedListener) {
        super(activity, onDataUpdatedListener);
        this.task = GameFieldCreationController.getInstance(activity).getTask();
    }

    @Override
    public void onLoadFinished(Loader<GameWorld> loader, GameWorld data) {
        super.onLoadFinished(loader, data);
        if (data != null) {
            gameFieldSelectControl.setControlListener(data.getGameWorldEditor());
            gameFieldCellsHeightControl.setControlListener(data.getGameWorldEditor());
        }
    }

    @Override
    public void addCellHeightController(@NonNull ViewGroup rootView) {
        gameFieldCellsHeightControl = new GameFieldCellsHeightControl(rootView.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.END);
        rootView.addView(gameFieldCellsHeightControl, layoutParams);
    }

    @Override
    public void addNavigationController(@NonNull ViewGroup rootView) {
        gameFieldSelectControl = new GameFieldSelectControl(rootView.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.END);
        rootView.addView(gameFieldSelectControl, layoutParams);
    }

    @Nullable
    @Override
    public View getNavigationDrawerView(@Nullable GameWorld gameWorld) {
        GameObjectsListView gameObjectsListView = new GameObjectsListView(activity);
        gameObjectsListView
                .setControlListener(gameWorld == null ? null : gameWorld.getGameWorldEditor())
                .setPossibleObjects(new EmptyObject(), new Player(), new Sphere())
                .setLayoutParams(new DrawerLayout.LayoutParams(
                        activity.getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width),
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        Gravity.START));
        return gameObjectsListView;
    }

    @Override
    public void createOptionsMenu(@NonNull MenuInflater inflater, @NonNull Menu menu) {
        inflater.inflate(R.menu.menu_create_world, menu);
    }

    @Override
    public boolean onOptionsMenuItemClick(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_item_create) {
            try {
                GameFieldCreationController.getInstance(activity).setGameField(activity, gameWorld.createGameWorld());
            } catch (BaseException e) {
                Toast.makeText(activity, e.getMessageRes(), Toast.LENGTH_SHORT).show();
                return true;
            }
            EditUserTasksActivity.startMe(activity, EditUserTasksActivity.Mode.NEW, -1);
            return true;
        }
        return false;
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        Toast.makeText(activity, R.string.message_world_created, Toast.LENGTH_SHORT).show();
        activity.finish();
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        Toast.makeText(activity, R.string.message_world_not_created, Toast.LENGTH_SHORT).show();
    }
}
