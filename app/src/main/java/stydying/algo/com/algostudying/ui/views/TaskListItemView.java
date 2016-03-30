package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.logic.managers.LoginManager;

/**
 * Created by Anton on 03.03.2016.
 */
public class TaskListItemView extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.text_title)
    protected TextView textTitle;
    @Bind(R.id.text_description)
    protected TextView textDescription;
    @Bind(R.id.img)
    protected ImageView img;
    @Bind(R.id.ic_edit)
    protected View icEdit;
    @Bind(R.id.ic_remove)
    protected View icRemove;

    private OnEditTaskListener onEditTaskListenerListener;
    private OnRemoveTaskListener onRemoveTaskListener;

    public interface OnEditTaskListener {
        void onEditTaskClicked(int position);
    }

    public interface OnRemoveTaskListener {
        void onRemoveTaskClicked(int position);
    }

    public TaskListItemView(Context context) {
        super(context);
        init();
    }

    public TaskListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TaskListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        ButterKnife.bind(inflate(getContext(), R.layout.v_task, this));
        icEdit.setOnClickListener(this);
        icRemove.setOnClickListener(this);

        User.Type type = LoginManager.getInstance(getContext()).getCurrentType();
        icRemove.setVisibility(type == User.Type.ADMIN ? VISIBLE : GONE);
        icEdit.setVisibility(type == User.Type.ADMIN ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_edit:
                if (onEditTaskListenerListener != null) {
                    onEditTaskListenerListener.onEditTaskClicked((int) getTag(R.integer.position_key));
                }
                break;
            case R.id.ic_remove:
                if (onRemoveTaskListener != null) {
                    onRemoveTaskListener.onRemoveTaskClicked((int) getTag(R.integer.position_key));
                }
                break;
        }
    }

    public TaskListItemView setOnEditTaskListenerListener(OnEditTaskListener listener) {
        this.onEditTaskListenerListener = listener;
        return this;
    }

    public TaskListItemView setOnRemoveTaskListener(OnRemoveTaskListener onRemoveTaskListener) {
        this.onRemoveTaskListener = onRemoveTaskListener;
        return this;
    }

    public TaskListItemView setTask(Task task, int position) {
        this.textTitle.setText(task.getTitle());
        this.textDescription.setText(task.getDescription());
        // todo implement task image
        img.setVisibility(GONE);
        setTag(R.integer.position_key, position);
        return this;
    }
}
