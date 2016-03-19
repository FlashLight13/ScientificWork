package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.operations.LoadUsersOperation;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.fragments.edit_user_tasks_fragments.EditTaskFragment;
import stydying.algo.com.algostudying.ui.fragments.edit_user_tasks_fragments.EditTaskGroupFragment;
import stydying.algo.com.algostudying.utils.BundleBuilder;

/**
 * Created by Anton on 29.02.2016.
 */
public class EditUserTasksActivity extends BaseActivity {

    @Bind(R.id.list)
    ListView listView;

    private static final String MODE_EXTRA
            = "stydying.algo.com.algostudying.ui.activities.EditUserTasksActivity.MODE_EXTRA";
    public static final String ID_EXTRA
            = "stydying.algo.com.algostudying.ui.activities.EditUserTasksActivity.ID_EXTRA";

    public enum Mode {
        TASK_GROUP(EditTaskGroupFragment.class), TASK(EditTaskFragment.class);

        private Class<? extends BaseFragment> fragmentClass;

        Mode(Class<? extends BaseFragment> fragmentClass) {
            this.fragmentClass = fragmentClass;
        }

        BaseFragment fragment(long id) {
            try {
                BaseFragment baseFragment = fragmentClass.newInstance();
                baseFragment.setArguments(new BundleBuilder().putLong(ID_EXTRA, id).build());
                return baseFragment;
            } catch (Exception e) {
                return null;
            }
        }
    }

    private Mode mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_edit_user_tasks);
        this.mode = Mode.valueOf(getIntent().getStringExtra(MODE_EXTRA));
        getSupportFragmentManager().beginTransaction().replace(
                R.id.content, mode.fragment(getIntent().getLongExtra(ID_EXTRA, -1)), null).commit();
        OperationProcessor.executeOperation(this, new LoadUsersOperation());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(LoadUsersOperation.class)) {
            List<User> users = event.data();
            List<UserData> userDatas = new ArrayList<>(users.size());
            for (User user : users) {
                userDatas.add(new UserData(user, false));
            }
            listView.setAdapter(new UsersAdapter(this, userDatas));
        }
    }

    public List<User> getSelectedUsers() {
        List<User> result = new ArrayList<>();
        for (UserData userData : ((UsersAdapter) listView.getAdapter()).getUsers()) {
            if (userData.isSelected) {
                result.add(userData.user);
            }
        }
        return result;
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        if (event.isOperation(LoadUsersOperation.class)) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    protected void onPause() {
        BusProvider.bus().unregister(this);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_done, menu);
        return true;
    }

    public static void startMe(@NonNull Context context, @NonNull Mode mode, long id) {
        Intent intent = new Intent(context, EditUserTasksActivity.class);
        intent.putExtra(MODE_EXTRA, mode.name());
        intent.putExtra(ID_EXTRA, id);
        context.startActivity(intent);
    }

    public static final class UserData {
        public final User user;
        public boolean isSelected;

        public UserData(User user, boolean isSelected) {
            this.user = user;
            this.isSelected = isSelected;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public UserData setSelected(boolean selected) {
            isSelected = selected;
            return this;
        }
    }

    private static class UsersAdapter extends BaseAdapter {

        private List<UserData> users;
        private Context context;

        public UsersAdapter(Context context, List<UserData> users) {
            this.context = context;
            this.users = users;
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public UserData getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox view = (CheckBox) convertView;
            if (view == null) {
                view = new CheckBox(context);
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        context.getResources().getDimensionPixelSize(R.dimen.list_item_height_medium));
                int margin = context.getResources().getDimensionPixelSize(R.dimen.basic_list_item_sides_margin);
                layoutParams.setMargins(margin, 0, margin, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    layoutParams.setMarginEnd(margin);
                    layoutParams.setMarginStart(margin);
                }
                view.setLayoutParams(layoutParams);
                view.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                view.setPadding(context.getResources().getDimensionPixelSize(R.dimen.list_item_height_medium), 0, 0, 0);
            }

            UserData userData = getItem(position);
            view.setChecked(userData.isSelected);
            view.setText(userData.user.getName());

            return view;
        }

        public List<UserData> getUsers() {
            return users;
        }
    }
}
