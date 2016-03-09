package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.ui.fragments.homefragments.AboutFragment;
import stydying.algo.com.algostudying.ui.fragments.homefragments.ProfileFragment;
import stydying.algo.com.algostudying.ui.fragments.homefragments.SettingsFragment;
import stydying.algo.com.algostudying.ui.fragments.homefragments.TasksFragment;
import stydying.algo.com.algostudying.ui.fragments.homefragments.UsersFragment;

/**
 * Created by Anton on 18.07.2015.
 */
public class HomeActivity extends NavigationDrawerActivity {

    private static final NavigationTask LOGOUT_TASK = new NavigationTask() {
        @Override
        public void execute(BaseActivity activity) {
            LoginManager.getInstance(activity).logout(activity);
            LoginActivity.startMe(activity);
            activity.finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTab(0);
    }

    @Override
    protected NavigationTab[] getTabs() {
        User user = LoginManager.getInstance(this).getCurrentUser();
        if (user != null) {
            switch (user.getType()) {
                case ADMIN:
                    return new AdminTabsProvider().getTabs();
                case STUDENT:
                    return new PlayerTabsProvider().getTabs();
            }
        }
        return new NavigationTab[0];
    }

    public static void startMe(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    private static final class AdminTabsProvider {

        private enum HomeTab implements NavigationTab {
            TASKS(TasksFragment.class, null, R.string.home_tab_task, R.drawable.ic_content_paste_black_24dp, true),
            STATS(ProfileFragment.class, null, R.string.home_tab_profile, R.drawable.ic_person_outline_black_24dp, true),
            ABOUT(AboutFragment.class, null, R.string.home_tab_about, R.drawable.ic_help_outline_black_24dp, true),
            USERS(UsersFragment.class, null, R.string.home_tab_users, R.drawable.ic_people_outline_black_24dp, true),
            SETTINGS(SettingsFragment.class, null, R.string.home_tab_settings, false),
            LOGOUT(null, LOGOUT_TASK, R.string.home_tab_logout, false);

            private Class fragmentClass;
            private int titleRes;
            private boolean isMain;
            private NavigationTask task;
            private int icon = -1;

            HomeTab(Class fragmentClass, NavigationTask task, int titleRes, int icon, boolean isMain) {
                this.fragmentClass = fragmentClass;
                this.titleRes = titleRes;
                this.isMain = isMain;
                this.task = task;
                this.icon = icon;
            }

            HomeTab(Class fragmentClass, NavigationTask task, int titleRes, boolean isMain) {
                this.fragmentClass = fragmentClass;
                this.titleRes = titleRes;
                this.isMain = isMain;
                this.task = task;
            }

            @Override
            public Fragment getFragmentInstance() {
                try {
                    return (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public int getTitleRes() {
                return titleRes;
            }

            @Override
            public int getIcon() {
                return icon;
            }

            @Override
            public boolean isMain() {
                return isMain;
            }

            @Nullable
            @Override
            public NavigationTask getTask() {
                return task;
            }


        }

        public NavigationTab[] getTabs() {
            return HomeTab.values();
        }
    }

    private static final class PlayerTabsProvider {

        private enum HomeTab implements NavigationTab {
            TASKS(TasksFragment.class, null, R.string.home_tab_task, R.drawable.ic_content_paste_black_24dp, true),
            STATS(ProfileFragment.class, null, R.string.home_tab_profile, R.drawable.ic_people_outline_black_24dp, true),
            ABOUT(AboutFragment.class, null, R.string.home_tab_about, R.drawable.ic_help_outline_black_24dp, true),
            SETTINGS(SettingsFragment.class, null, R.string.home_tab_settings, false),
            LOGOUT(null, LOGOUT_TASK, R.string.home_tab_logout, false);

            private Class fragmentClass;
            private int titleRes;
            private boolean isMain;
            private NavigationTask task;
            private int icon;

            HomeTab(Class fragmentClass, NavigationTask task, int titleRes, int icon, boolean isMain) {
                this.fragmentClass = fragmentClass;
                this.titleRes = titleRes;
                this.isMain = isMain;
                this.task = task;
                this.icon = icon;
            }

            HomeTab(Class fragmentClass, NavigationTask task, int titleRes, boolean isMain) {
                this.fragmentClass = fragmentClass;
                this.titleRes = titleRes;
                this.isMain = isMain;
                this.task = task;
            }

            @Override
            public Fragment getFragmentInstance() {
                try {
                    return (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public int getTitleRes() {
                return titleRes;
            }

            @Override
            public int getIcon() {
                return icon;
            }

            @Override
            public boolean isMain() {
                return isMain;
            }

            @Nullable
            @Override
            public NavigationTask getTask() {
                return task;
            }


        }

        public NavigationTab[] getTabs() {
            return HomeTab.values();
        }
    }
}
