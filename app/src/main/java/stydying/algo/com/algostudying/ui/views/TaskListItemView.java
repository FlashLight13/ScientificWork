package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.tasks.Task;

/**
 * Created by Anton on 03.03.2016.
 */
public class TaskListItemView extends LinearLayout {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_description)
    TextView textDescription;
    @Bind(R.id.img)
    ImageView img;

    private OnEditTaskListener onEditTaskListenerListener;

    public interface OnEditTaskListener {
        void onEditTaskClicked(int position);
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

        final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_24dp);
        textTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        textTitle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int rightDrawableRight = textTitle.getWidth();
                final int rightDrawableLeft = rightDrawableRight - drawable.getIntrinsicWidth();
                if (event.getX() > rightDrawableLeft && event.getX() < rightDrawableRight) {
                    if (onEditTaskListenerListener != null) {
                        onEditTaskListenerListener.onEditTaskClicked((int) getTag());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setOnEditTaskListenerListener(OnEditTaskListener listener) {
        this.onEditTaskListenerListener = listener;
    }

    public void setTask(Task task) {
        this.textTitle.setText(task.getTitle());
        this.textDescription.setText(task.getDescription());
        // todo implement task image
        img.setVisibility(GONE);
    }
}
