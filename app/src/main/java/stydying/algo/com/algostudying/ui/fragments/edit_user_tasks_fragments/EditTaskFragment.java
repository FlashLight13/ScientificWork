package stydying.algo.com.algostudying.ui.fragments.edit_user_tasks_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.operations.GetTaskOperation;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.ui.activities.EditUserTasksActivity;
import stydying.algo.com.algostudying.ui.fragments.homefragments.ConfigureTaskFragment;
import stydying.algo.com.algostudying.utils.ViewsUtils;

/**
 * Created by Anton on 03.03.2016.
 */
public class EditTaskFragment extends ConfigureTaskFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pickerHeightBar.setVisibility(View.GONE);
        pickerWidthBar.setVisibility(View.GONE);
        btnCreate.setVisibility(View.GONE);
        Bundle args = getArguments();
        if (args != null) {
            OperationProcessor.executeOperation(getContext(),
                    new GetTaskOperation(args.getLong(EditUserTasksActivity.ID_EXTRA),
                            OperationProcessor.Operation.OperationType.LOCAL_ONLY));
        }
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(GetTaskOperation.class)) {
            Task task = event.data();
            updateGroup(task);
            ViewsUtils.getEditText(inputTitle).setText(task.getTitle());
            ViewsUtils.getEditText(inputDescription).setText(task.getDescription());
        }
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        if (event.isOperation(GetTaskOperation.class)) {
            Toast.makeText(getContext(), R.string.error_failed_to_load_task, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickCreate() {
        super.onClickCreate();
    }
}
