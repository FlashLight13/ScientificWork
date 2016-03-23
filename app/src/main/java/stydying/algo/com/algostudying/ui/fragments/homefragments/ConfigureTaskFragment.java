package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
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
import stydying.algo.com.algostudying.logic.creation.GameFieldCreationController;
import stydying.algo.com.algostudying.ui.activities.GameFieldActivity;
import stydying.algo.com.algostudying.ui.activities.TaskGroupSearchingActivity;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.views.SwipeNumberPicker;
import stydying.algo.com.algostudying.utils.ViewsUtils;

/**
 * Created by Anton on 12.02.2016.
 */
public class ConfigureTaskFragment extends BaseFragment {

    public static final String INITIAL_TASK_ARG = ConfigureTaskFragment.class.getName() + "INITIAL_TASK_ARG";

    @Bind(R.id.input_title)
    protected TextInputLayout inputTitle;
    @Bind(R.id.input_description)
    protected TextInputLayout inputDescription;
    @Bind(R.id.input_group)
    protected TextView inputGroup;
    @Bind(R.id.picker_width)
    protected SwipeNumberPicker pickerWidth;
    @Bind(R.id.picker_height)
    protected SwipeNumberPicker pickerHeight;

    @Bind(R.id.picker_width_bar)
    protected View pickerWidthBar;
    @Bind(R.id.btn_create)
    protected FloatingActionButton btnCreate;
    @Bind(R.id.picker_height_bar)
    protected View pickerHeightBar;

    private TaskGroup selectedGroup;
    private boolean isBtnVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_configure_task);
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
            selectedGroup = null;
        } else {
            pickerHeight.setText(String.valueOf(10));
            pickerWidth.setText(String.valueOf(10));
            ViewsUtils.getEditText(inputTitle).setText(task.getTitle());
            ViewsUtils.getEditText(inputDescription).setText(task.getDescription());
            selectedGroup = null;
        }
        updateGroup(task);
    }

    protected void updateGroup(@Nullable Task task) {
        if (task == null || task.getTaskGroup() == null) {
            inputGroup.setText(R.string.world_setup_group_press);
        } else {
            inputGroup.setText(
                    getString(R.string.world_setup_group_title, task.getTaskGroup().getTitle()));
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setBtnVisibility(isVisibleToUser);
    }

    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            setBtnVisibility(true);
        } else {
            ViewsUtils.hideKeyboard(getActivity());
            setBtnVisibility(false);
        }
    }

    private void setBtnVisibility(boolean isVisible) {
        if (isVisible == isBtnVisible) {
            return;
        }
        if (btnCreate != null) {
            if (isVisible) {
                btnCreate.show();
            } else {
                btnCreate.hide();
            }
            this.isBtnVisible = isVisible;
        }
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
            Task task = new Task(
                    ViewsUtils.getEditText(inputTitle).getText().toString(),
                    selectedGroup,
                    ViewsUtils.getEditText(inputDescription).getText().toString(),
                    pickerWidth.getValue(),
                    pickerHeight.getValue());
            GameFieldCreationController.getInstance(getContext()).init(getContext(), task);
            GameFieldActivity.startMe(getContext(), null, GameFieldActivity.Mode.EDIT);
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
