package stydying.algo.com.algostudying.ui.controller.game_field;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.utils.loaders.BaseAsyncLoader;

/**
 * Created by Anton on 24.03.2016.
 */
public abstract class GameFieldController implements LoaderManager.LoaderCallbacks<GameWorld> {

    public interface OnDataUpdatedListener {
        void onDataUpdated(@Nullable GameWorld gameWorld);
    }

    protected GameWorld gameWorld;
    protected Task task;
    protected GameFieldActivity activity;

    private OnDataUpdatedListener onDataUpdatedListener;

    public GameFieldController(GameFieldActivity activity, OnDataUpdatedListener onDataUpdatedListener) {
        this.activity = activity;
        this.onDataUpdatedListener = onDataUpdatedListener;
    }

    @Override
    public Loader<GameWorld> onCreateLoader(int id, Bundle args) {
        return new BaseAsyncLoader<GameWorld>(activity) {

            @Override
            public GameWorld loadInBackground() {
                try {
                    return new GameWorld(getContext(), task, getMode());
                } catch (IOException e) {
                    Log.d("DebugLogs", "Failed to init world ", e);
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<GameWorld> loader, GameWorld data) {
        this.gameWorld = data;
        if (onDataUpdatedListener != null) {
            onDataUpdatedListener.onDataUpdated(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<GameWorld> loader) {

    }

    public String getTitle() {
        return task.getTitle();
    }

    public void prepareOptionsMenu(@NonNull Menu menu) {
    }

    protected abstract GameFieldActivity.Mode getMode();

    public abstract void addCellHeightController(@NonNull ViewGroup rootView);

    public abstract void addNavigationController(@NonNull ViewGroup rootView);

    @Nullable
    public abstract View getNavigationDrawerView(@Nullable GameWorld gameWorld);

    public abstract void createOptionsMenu(@NonNull MenuInflater inflater, @NonNull Menu menu);

    public abstract boolean onOptionsMenuItemClick(@NonNull MenuItem menuItem);

    public void onResume() {
        BusProvider.bus().register(this);
    }

    public void onPause() {
        BusProvider.bus().unregister(this);
    }

    public void onCreate(@Nullable Bundle savedState) {
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
    }
}
