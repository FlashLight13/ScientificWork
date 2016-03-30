package stydying.algo.com.algostudying.ui.views.game_controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.commands.CommandBlock;

/**
 * Created by Anton on 14.07.2015.
 */
public class CommandView extends RelativeLayout {

    public interface OnRemoveListener {
        void onRemove(int position);
    }

    public enum State {
        FULL, ONLY_ICON
    }

    @Bind(R.id.icon)
    protected ImageView icon;
    @Bind(R.id.title)
    protected TextView titleView;
    @Bind(R.id.arrow)
    protected View arrow;
    @Bind(R.id.ic_remove)
    protected View icRemove;

    private State state;
    private Command command;
    private OnRemoveListener onRemoveListener;

    public CommandView(Context context) {
        super(context);
        init(context);
    }

    public CommandView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CommandView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommandView(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        super(context, attributeSet, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.v_command_view, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ic_remove)
    protected void onRemoveClicked() {
        if (onRemoveListener != null) {
            onRemoveListener.onRemove((int) getTag(R.integer.position_key));
        }
    }

    public void moveToState(State state) {
        this.state = state;
        switch (state) {
            case FULL:
                this.icon.setVisibility(View.VISIBLE);
                this.titleView.setVisibility(View.VISIBLE);
                this.icRemove.setVisibility(VISIBLE);
                break;
            case ONLY_ICON:
                this.icon.setVisibility(View.VISIBLE);
                this.titleView.setVisibility(View.GONE);
                this.icRemove.setVisibility(GONE);
                break;
        }
    }

    public CommandView setData(Command command, int position) {
        this.command = command;
        this.icon.setImageResource(command.getIconId());
        this.titleView.setText(command.getTitleId());
        if (command instanceof CommandBlock && state == State.FULL) {
            arrow.setVisibility(View.VISIBLE);
        } else {
            arrow.setVisibility(View.GONE);
        }
        setTag(R.integer.position_key, position);
        return this;
    }

    public CommandView setOnRemoveListener(OnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
        return this;
    }

    public Command getCommand() {
        return command;
    }
}
