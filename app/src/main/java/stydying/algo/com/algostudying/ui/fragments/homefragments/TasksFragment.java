package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.operations.GetTaskGroupsNames;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.operations.RemoveTaskGroup;
import stydying.algo.com.algostudying.ui.activities.EditUserTasksActivity;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.views.LoadingPlaceholderView;

/**
 * Created by Anton on 18.07.2015.
 */
public class TasksFragment extends BaseFragment implements LoadingPlaceholderView.OnRetryListener {

    @Bind(R.id.pager)
    ViewPager tasksPager;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.error_placeholder)
    LoadingPlaceholderView placeholderView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_tasks);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasksPager.setAdapter(LoginManager.getInstance(getContext()).getCurrentType() == User.Type.ADMIN
                ? new AdminTasksAdapter(getContext(), getChildFragmentManager())
                : new UserTasksAdapter(getChildFragmentManager()));
        tasksPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                supportInvalidateOptionsMenu();
            }
        });

        tabs.setupWithViewPager(tasksPager);

        placeholderView.loading();
        placeholderView.setOnRetryListener(this);
        onRetry();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        BusProvider.bus().unregister(this);
        super.onPause();
    }

    private void editGroup() {
        TaskGroup taskGroup = adapter().getTaskGroup(tasksPager.getCurrentItem());
        EditUserTasksActivity.startMe(getContext(), EditUserTasksActivity.Mode.TASK_GROUP,
                taskGroup == null ? -1 : taskGroup.getId());
    }

    private void removeGroup() {
        OperationProcessor.executeOperation(getContext(),
                new RemoveTaskGroup(adapter().getTaskGroup(tasksPager.getCurrentItem())));
    }

    @Override
    public void onRetry() {
        OperationProcessor.executeOperation(getContext(), new GetTaskGroupsNames());
    }

    @SuppressWarnings("unchecked")
    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(GetTaskGroupsNames.class)) {
            adapter().refill((List<TaskGroup>) event.data());
            tabs.setTabsFromPagerAdapter(adapter());
            placeholderView.success();

            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_groups, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_remove:
                removeGroup();
                return true;
            case R.id.menu_edit:
                editGroup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setVisible(adapter().shouldShowMenuItems(tasksPager.getCurrentItem()));
        }
    }

    private UserTasksAdapter adapter() {
        return (UserTasksAdapter) tasksPager.getAdapter();
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        if (event.isOperation(GetTaskGroupsNames.class)) {
            placeholderView.error(event.error);
        }
    }

    private static class AdminTasksAdapter extends UserTasksAdapter {

        private Context context;

        private AdminTasksAdapter(Context context, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.context = context;
        }

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? new SetupNewTaskFragment() : super.getItem(position - 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0
                    ? context.getResources().getString(R.string.create_task_tab_title)
                    : super.getPageTitle(position - 1);
        }

        @Override
        public void refill(List<TaskGroup> taskGroups) {
            super.refill(taskGroups);
            ((SetupNewTaskFragment) registeredFragments.get(0)).setTaskGroups(taskGroups);
        }

        @Override
        public boolean shouldShowMenuItems(int currentPosition) {
            return currentPosition > 0;
        }

        @Override
        @Nullable
        protected TaskGroup getTaskGroup(int position) {
            return position == 0 ? null : super.getTaskGroup(position - 1);
        }
    }

    private static class UserTasksAdapter extends FragmentPagerAdapter {

        protected SparseArray<Fragment> registeredFragments = new SparseArray<>();
        private List<TaskGroup> taskGroups = new ArrayList<>();

        public UserTasksAdapter(FragmentManager fm) {
            super(fm);
        }

        public void refill(List<TaskGroup> tasks) {
            taskGroups.clear();
            taskGroups.addAll(tasks);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return ListTasksFragment.instance(taskGroups.get(position));
        }

        @Override
        public int getCount() {
            return taskGroups.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return taskGroups.get(position).getTitle();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        public boolean shouldShowMenuItems(int currentPosition) {
            return false;
        }

        @Nullable
        protected TaskGroup getTaskGroup(int position) {
            return taskGroups.get(position);
        }
    }
}
