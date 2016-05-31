package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.operations.GetTaskOperation;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.operations.RemoveTaskOperation;
import stydying.algo.com.algostudying.ui.activities.EditUserTasksActivity;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.views.TaskListItemView;
import stydying.algo.com.algostudying.utils.BundleBuilder;

/**
 * Created by Anton on 25.01.2016.
 */
public class ListTasksFragment extends BaseFragment implements TaskListItemView.OnEditTaskListener,
        TaskListItemView.OnRemoveTaskListener {

    private static final String ARG_TASK_GROUP =
            "stydying.algo.com.algostudying.ui.fragments.homefragments.ListTasksFragment.ARG_TASK_GROUP";

    @Bind(R.id.list)
    protected ListView tasksListView;

    private TaskGroup taskGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            taskGroup = args.getParcelable(ARG_TASK_GROUP);
        }
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_list_tasks);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Task> tasks = taskGroup.getTasks();
        Collections.sort(tasks, new Task.ByTaskDifficultyComparator());
        tasksListView.setAdapter(new TasksAdapter(this, this).setTasks(tasks));
        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TasksAdapter adapter = (TasksAdapter) parent.getAdapter();
                launchTask(adapter.getItem(position));
            }
        });
    }

    private TasksAdapter adapter() {
        return (TasksAdapter) tasksListView.getAdapter();
    }

    private void launchTask(Task task) {
        OperationProcessor.executeOperation(getContext(), new GetTaskOperation(task));
    }

    @Override
    public void onEditTaskClicked(int position) {
        EditUserTasksActivity.startMe(getActivity(), EditUserTasksActivity.Mode.TASK,
                adapter().getItem(position).getId());
    }

    @Override
    public void onRemoveTaskClicked(int position) {
        OperationProcessor.executeOperation(getContext(),
                new RemoveTaskOperation(adapter().getItem(position).getId()));
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(GetTaskOperation.class)) {
            GameFieldActivity.startMe(getContext(), (Task) event.data(), GameFieldActivity.Mode.PLAY);
        }
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        if (event.isOperation(GetTaskOperation.class)) {
            Toast.makeText(getContext(), R.string.error_failed_to_load_world, Toast.LENGTH_SHORT).show();
        }
    }

    private static class TasksAdapter extends BaseAdapter {

        private TaskListItemView.OnEditTaskListener onEditTaskListenerListener;
        private TaskListItemView.OnRemoveTaskListener onRemoveTaskListener;

        private List<Task> tasks = new ArrayList<>();

        public TasksAdapter(@Nullable TaskListItemView.OnEditTaskListener onEditTaskListenerListener,
                            @Nullable TaskListItemView.OnRemoveTaskListener onRemoveTaskListener) {
            this.onEditTaskListenerListener = onEditTaskListenerListener;
            this.onRemoveTaskListener = onRemoveTaskListener;
        }

        public TasksAdapter setTasks(List<Task> tasks) {
            this.tasks = tasks;
            notifyDataSetChanged();
            return this;
        }

        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Task getItem(int position) {
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskListItemView view = (TaskListItemView) convertView;
            if (view == null) {
                view = new TaskListItemView(parent.getContext());
            }
            view.setTask(getItem(position), position)
                    .setOnEditTaskListenerListener(onEditTaskListenerListener)
                    .setOnRemoveTaskListener(onRemoveTaskListener);
            return view;
        }
    }

    public static ListTasksFragment instance(TaskGroup taskGroup) {
        ListTasksFragment listTasksFragment = new ListTasksFragment();
        listTasksFragment.setArguments(new BundleBuilder().putParcelable(ARG_TASK_GROUP, taskGroup).build());
        return listTasksFragment;
    }
}
