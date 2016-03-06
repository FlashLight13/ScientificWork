package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.activities.TaskGroupSearchingActivity;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.views.SwipeNumberPicker;
import stydying.algo.com.algostudying.utils.ViewsUtils;

/**
 * Created by Anton on 12.02.2016.
 */
public class SetupNewTaskFragment extends BaseFragment {

    public static final String INITIAL_TASK_ARG = SetupNewTaskFragment.class.getName() + "INITIAL_TASK_ARG";

    @Bind(R.id.input_title)
    TextInputLayout inputTitle;
    @Bind(R.id.input_description)
    TextInputLayout inputDescription;
    @Bind(R.id.input_group)
    TextView inputGroup;
    @Bind(R.id.picker_width)
    SwipeNumberPicker pickerWidth;
    @Bind(R.id.picker_height)
    SwipeNumberPicker pickerHeight;

    private TaskGroup selectedGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_setup_new_task);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        Task task = null;
        if (args != null) {
            task = args.getParcelable(INITIAL_TASK_ARG);
        }
        setTask(task);
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

    private void setTask(@Nullable Task task) {
        if (task == null) {
            pickerHeight.setText(String.valueOf(10));
            pickerWidth.setText(String.valueOf(10));
            ViewsUtils.getEditText(inputTitle).setText("");
            ViewsUtils.getEditText(inputDescription).setText("");
            inputGroup.setText(R.string.world_setup_group_press);
            selectedGroup = null;
        } else {
            pickerHeight.setText(String.valueOf(10));
            pickerWidth.setText(String.valueOf(10));
            ViewsUtils.getEditText(inputTitle).setText(task.getTitle());
            ViewsUtils.getEditText(inputDescription).setText(task.getDescription());
            inputGroup.setText(
                    getString(R.string.world_setup_group_title, task.getTaskGroup().getTitle()));
            selectedGroup = null;
        }
    }

    @SuppressWarnings("unchecked")
    public void setTaskGroups(List<TaskGroup> taskGroups) {
        ArrayAdapter<TaskGroup> adapter
                = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, taskGroups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (TaskGroupSearchingActivity.REQUEST_CODE == requestCode) {
                selectedGroup = data.getParcelableExtra(TaskGroupSearchingActivity.TASK_GROUP_EXTRA);
                if (selectedGroup != null) {
                    inputGroup.setText(getString(R.string.world_setup_group_title, selectedGroup.getTitle()));
                    inputGroup.setError(null);
                } else {
                    inputGroup.setText(R.string.world_setup_group_press);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.input_group)
    public void onClickGroup() {
        TaskGroupSearchingActivity.startMe(getActivity(), inputGroup);
    }

    @OnClick(R.id.btn_create)
    public void onClickCreate() {
        if (!hasErrors()) {
            if (!selectedGroup.exists()) {
                selectedGroup.save();
            }
            GameFieldActivity.startMe(getContext(), new Task(
                            ViewsUtils.getEditText(inputTitle).getText().toString(),
                            selectedGroup,
                            ViewsUtils.getEditText(inputDescription).getText().toString(),
                            pickerWidth.getValue(),
                            pickerHeight.getValue())
                            .saveMap(getContext()),
                    GameFieldActivity.Mode.EDIT);
            setTask(null);
        }
    }

    private boolean hasErrors() {
        if (TextUtils.isEmpty(ViewsUtils.getEditText(inputTitle).getText().toString())) {
            inputTitle.setError(getString(R.string.error_world_name_empty));
            return true;
        }
        if (selectedGroup == null) {
            inputGroup.setError(getString(R.string.error_world_group_empty));
            return true;
        }
        inputTitle.setError(null);
        inputGroup.setError(null);
        return false;
    }
}
