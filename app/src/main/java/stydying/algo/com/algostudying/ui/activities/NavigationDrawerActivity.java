package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.ui.views.NavigationItemView;

/**
 * Created by Anton on 18.07.2015.
 */
public abstract class NavigationDrawerActivity extends BaseActivity {
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.left_drawer)
    ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;

    protected NavigationTab currentTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_navigaiton_drawer);
        initActionBar();

        drawerList.setAdapter(new NavigationDrawerAdapter(this, getTabs()));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                NavigationDrawerActivity.this.onDrawerClosed();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                NavigationDrawerActivity.this.onDrawerOpened();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    protected void onDrawerClosed() {
    }

    protected void onDrawerOpened() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    protected abstract NavigationTab[] getTabs();

    public interface NavigationTab {

        @Nullable
        Fragment getFragmentInstance();

        @StringRes
        int getTitleRes();

        @DrawableRes
        int getIcon();

        boolean isMain();

        @Nullable
        NavigationTask getTask();
    }

    public interface NavigationTask {
        void execute(BaseActivity context);
    }

    public void showTab(int pos) {
        drawerList.setItemChecked(pos, true);
        showTab(getTabs()[pos]);
    }

    public void showTab(NavigationTab tab) {
        currentTab = tab;
        Fragment fragment = currentTab.getFragmentInstance();
        NavigationTask task = currentTab.getTask();
        if (fragment != null) {
            openFragment(fragment);
            drawerLayout.closeDrawer(drawerList);
            setTitle(currentTab.getTitleRes());
        }
        if (task != null) {
            task.execute(NavigationDrawerActivity.this);
        }
    }

    private static class NavigationDrawerAdapter extends BaseAdapter {
        private Context context;
        private NavigationTab[] tabs;

        private NavigationDrawerAdapter(Context context, NavigationTab[] tabs) {
            this.context = context;
            this.tabs = tabs;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public NavigationTab getItem(int position) {
            return tabs[position];
        }

        @Override
        public long getItemId(int position) {
            return tabs[position].isMain() ? 1 : 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final NavigationTab tab = getItem(position);
            NavigationItemView view = (NavigationItemView) convertView;
            if (view == null || view.isMain() != tab.isMain()) {
                view = new NavigationItemView(new ContextThemeWrapper(context, tab.isMain()
                        ? R.style.NavigationBarItemPrimary
                        : R.style.NavigationBarItemSecondary));
            }
            view.setTitle(tab.getTitleRes());
            view.setIcon(tab.getIcon());
            view.setMain(tab.isMain());
            return view;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            showTab(position);
        }
    }
}
