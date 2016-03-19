package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.constants.Loaders;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.objects.CubeBlock;
import stydying.algo.com.algostudying.game.objects.Player;
import stydying.algo.com.algostudying.operations.CreateUpdateTaskOperation;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.ui.graphics.GameView;
import stydying.algo.com.algostudying.ui.views.game_controls.GameFieldCellsHeightControl;
import stydying.algo.com.algostudying.ui.views.game_controls.GameFieldSelectControl;
import stydying.algo.com.algostudying.ui.views.game_controls.GameNavigationDrawerView;
import stydying.algo.com.algostudying.ui.views.game_controls.GameObjectsListView;
import stydying.algo.com.algostudying.utils.loaders.BaseAsyncLoader;

/**
 * Created by Anton on 06.06.2015.
 */
public class GameFieldActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<GameWorld> {

    public enum Mode {
        EDIT, PLAY;

        @NonNull
        public View getNavigationView(Context context, @Nullable GameWorld gameWorld) {
            switch (this) {
                case EDIT:
                    GameObjectsListView gameObjectsListView = new GameObjectsListView(context);
                    gameObjectsListView.setControlListener(gameWorld == null
                            ? null
                            : gameWorld.getGameWorldEditor());
                    gameObjectsListView.setPossibleObjects(new Class[]{CubeBlock.class, Player.class});
                    gameObjectsListView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.START));
                    return gameObjectsListView;
                case PLAY:
                    return GameNavigationDrawerView.getInstance(context);
                default:
                    throw new IllegalStateException("Unknown mode");
            }
        }

        private void createOptionsMenu(BaseActivity baseActivity, Menu menu) {
            switch (this) {
                case EDIT:
                    baseActivity.getMenuInflater().inflate(R.menu.menu_create_world, menu);
                    return;
                case PLAY:
                    baseActivity.getMenuInflater().inflate(R.menu.menu_execute_operations, menu);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown mode");
            }
        }
    }

    private static final String WORLD_DATA_EXTRA
            = "stydying.algo.com.algostudying.ui.activities.GameFieldActivity.WORLD_DATA_EXTRA";
    private static final String MODE_EXTRA
            = "stydying.algo.com.algostudying.ui.activities.GameFieldActivity.MODE_EXTRA";


    protected GameView mGLView;
    private FrameLayout glassView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private GameFieldSelectControl gameFieldSelectControl;
    private GameFieldCellsHeightControl gameFieldCellsHeightControl;
    private View navigationView;

    private Task task;
    private Mode mode;
    private GameWorld gameWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = getIntent().getParcelableExtra(WORLD_DATA_EXTRA);
        mode = Mode.valueOf(getIntent().getStringExtra(MODE_EXTRA));

        mGLView = new GameView(this);
        setContentView(mGLView);
        initGlassView();
        initDrawer();
        initActionBar();
        initGameFieldNavigationControl(glassView);
        initGameFieldCellsHeightControl(glassView);

        addContentView(drawerLayout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        getSupportLoaderManager().restartLoader(Loaders.GAME_WORLD_LOADER, null, this);
    }

    private void initGameFieldNavigationControl(@NonNull FrameLayout rootView) {
        if (mode == Mode.EDIT) {
            this.gameFieldSelectControl = new GameFieldSelectControl(this);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM | Gravity.END);
            rootView.addView(gameFieldSelectControl, layoutParams);
        }
    }

    private void initGameFieldCellsHeightControl(@NonNull FrameLayout rootView) {
        if (mode == Mode.EDIT) {
            this.gameFieldCellsHeightControl = new GameFieldCellsHeightControl(this);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.TOP | Gravity.END);
            rootView.addView(gameFieldCellsHeightControl, layoutParams);
        }
    }

    private void initDrawer() {
        drawerLayout = new android.support.v4.widget.DrawerLayout(this);
        drawerLayout.addView(glassView, glassView.getLayoutParams());
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                GameFieldActivity.this.onDrawerClosed();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                GameFieldActivity.this.onDrawerOpened();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGLView.onTouchEvent(event);
            }
        });
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        Toast.makeText(this, R.string.message_world_created, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onError(OperationErrorEvent event) {
        Toast.makeText(this, R.string.message_world_not_created, Toast.LENGTH_SHORT).show();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.deep_blue)));
        }
        setTitle(task.getTitle());
        drawerToggle.syncState();
    }

    private void create() {
        OperationProcessor.executeOperation(this,
                new CreateUpdateTaskOperation(gameWorld.createTask()));
    }

    private void execute() {
        if (gameWorld != null && navigationView != null) {
            gameWorld.executeCommands(((GameNavigationDrawerView) navigationView).getCommands());
        }
    }

    protected void onDrawerClosed() {
    }

    protected void onDrawerOpened() {
    }

    @Override
    public Loader<GameWorld> onCreateLoader(int id, final Bundle args) {
        return new BaseAsyncLoader<GameWorld>(this) {

            @Override
            public GameWorld loadInBackground() {
                try {
                    GameWorld gameWorld = new GameWorld(getContext(), task);
                    gameWorld.initDrawing(GameFieldActivity.this);
                    return gameWorld;
                } catch (IOException e) {
                    Log.d("DebugLogs", "Failed to init world ", e);
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, GameWorld data) {
        navigationView = mode.getNavigationView(this, data);
        drawerLayout.addView(navigationView);
        if (data != null) {
            gameWorld = data;
            mGLView.init(data);
            if (mode == Mode.EDIT) {
                gameFieldSelectControl.setControlListener(data.getGameWorldEditor());
                gameFieldCellsHeightControl.setControlListener(data.getGameWorldEditor());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void initGlassView() {
        glassView = new FrameLayout(this);
        glassView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        glassView.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        glassView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGLView.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        BusProvider.bus().unregister(this);
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onDestroy() {
        getSupportLoaderManager().destroyLoader(Loaders.GAME_WORLD_LOADER);
        super.onDestroy();
    }

    public static void startMe(@NonNull Context context, @NonNull Task gameFieldData, @NonNull Mode mode) {
        Intent intent = new Intent(context, GameFieldActivity.class);
        intent.putExtra(WORLD_DATA_EXTRA, gameFieldData);
        intent.putExtra(MODE_EXTRA, mode.name());
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mode.createOptionsMenu(this, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_item_create:
                create();
                return true;
            case R.id.menu_item_execute:
                execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
