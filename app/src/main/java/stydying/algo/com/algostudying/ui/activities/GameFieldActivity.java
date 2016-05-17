package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.constants.Loaders;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.ui.controller.game_field.EditingController;
import stydying.algo.com.algostudying.ui.controller.game_field.GameFieldController;
import stydying.algo.com.algostudying.ui.controller.game_field.PlayController;
import stydying.algo.com.algostudying.ui.graphics.GameView;

/**
 * Created by Anton on 06.06.2015.
 */
public class GameFieldActivity extends BaseActivity implements GameFieldController.OnDataUpdatedListener {

    public enum Mode {
        EDIT, PLAY
    }

    private static final String MODE_EXTRA
            = "stydying.algo.com.algostudying.ui.activities.GameFieldActivity.MODE_EXTRA";


    protected GameView mGLView;
    private FrameLayout glassView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private Mode mode;

    private GameFieldController gameFieldController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = Mode.valueOf(getIntent().getStringExtra(MODE_EXTRA));
        switch (mode) {
            case EDIT:
                gameFieldController = new EditingController(this, this);
                break;
            case PLAY:
                gameFieldController = new PlayController(this, this);
                break;
        }

        mGLView = new GameView(this);
        setContentView(mGLView);
        initGlassView();
        initDrawer();
        initActionBar();

        gameFieldController.onCreate(savedInstanceState);
        gameFieldController.addCellHeightController(glassView);
        gameFieldController.addNavigationController(glassView);

        addContentView(drawerLayout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        getSupportLoaderManager().restartLoader(Loaders.GAME_WORLD_LOADER, null, gameFieldController);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        gameFieldController.onSaveInstanceState(outState);
    }

    private void initDrawer() {
        drawerLayout = new android.support.v4.widget.DrawerLayout(this);
        drawerLayout.addView(glassView, glassView.getLayoutParams());
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
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerLayout.addDrawerListener(drawerToggle);
        /*drawerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGLView.onTouchEvent(event);
            }
        });*/
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.deep_blue)));
        }
        setTitle(gameFieldController.getTitle());
    }

    protected void onDrawerClosed() {
    }

    protected void onDrawerOpened() {
    }

    @Override
    public void onDataUpdated(@Nullable GameWorld gameWorld) {
        if (gameWorld != null) {
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.addView(gameFieldController.getNavigationDrawerView(gameWorld));
            mGLView.init(gameWorld);
        }
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
        int padding = getResources().getDimensionPixelSize(R.dimen.content_margins);
        glassView.setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameFieldController.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        gameFieldController.onPause();
        mGLView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        drawerLayout.removeDrawerListener(drawerToggle);
        getSupportLoaderManager().destroyLoader(Loaders.GAME_WORLD_LOADER);
        mGLView.release();
        super.onDestroy();
    }

    public static void startMe(@NonNull Context context,
                               @Nullable Task gameFieldData,
                               @NonNull Mode mode) {
        Intent intent = new Intent(context, GameFieldActivity.class);
        if (gameFieldData != null) {
            intent.putExtra(PlayController.WORLD_DATA_EXTRA, gameFieldData);
        }
        intent.putExtra(MODE_EXTRA, mode.name());
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        gameFieldController.prepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        gameFieldController.createOptionsMenu(getMenuInflater(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item)
                || gameFieldController.onOptionsMenuItemClick(item)
                || super.onOptionsItemSelected(item);
    }
}