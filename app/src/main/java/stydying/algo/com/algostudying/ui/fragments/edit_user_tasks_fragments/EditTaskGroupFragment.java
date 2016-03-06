package stydying.algo.com.algostudying.ui.fragments.edit_user_tasks_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;

/**
 * Created by Anton on 03.03.2016.
 */
public class EditTaskGroupFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_edtit_task_group);
    }

    private TextInputLayout getEditText() {
        return (TextInputLayout) getView();
    }
}
