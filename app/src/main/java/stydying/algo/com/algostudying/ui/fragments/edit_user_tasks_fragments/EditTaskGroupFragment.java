package stydying.algo.com.algostudying.ui.fragments.edit_user_tasks_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.operations.LoadTaskGroupOperation;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.operations.edit_operations.EditTaskGroupOperation;
import stydying.algo.com.algostudying.ui.activities.EditUserTasksActivity;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.utils.ViewsUtils;

/**
 * Created by Anton on 03.03.2016.
 */
public class EditTaskGroupFragment extends BaseFragment {

    @Bind(R.id.input_title)
    TextInputLayout inputTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_edit_task_group);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            OperationProcessor.executeOperation(getContext(),
                    new LoadTaskGroupOperation(args.getLong(EditUserTasksActivity.ID_EXTRA)));
        }
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(LoadTaskGroupOperation.class)) {
            TaskGroup taskGroup = event.data();
            ViewsUtils.getEditText(inputTitle).setText(taskGroup.getTitle());
            return;
        }
        if (event.isOperation(EditTaskGroupOperation.class)) {
            Toast.makeText(getActivity(), R.string.message_edit_done, Toast.LENGTH_SHORT).show();
        }
    }

    private void done() {
        EditUserTasksActivity activity = (EditUserTasksActivity) getActivity();
        OperationProcessor.executeOperation(activity, new EditTaskGroupOperation());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_done: {
                done();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
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

    private TextInputLayout getEditText() {
        return (TextInputLayout) getView();
    }
}
