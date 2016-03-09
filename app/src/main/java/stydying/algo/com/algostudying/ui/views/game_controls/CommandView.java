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
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.commands.CommandBlock;

/**
 * Created by Anton on 14.07.2015.
 */
public class CommandView extends RelativeLayout {

    public enum State {
        FULL, ONLY_ICON
    }

    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.title)
    TextView titleView;
    @Bind(R.id.arrow)
    View arrow;

    private State state;
    private Command command;

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

    public void moveToState(State state) {
        this.state = state;
        switch (state) {
            case FULL:
                this.icon.setVisibility(View.VISIBLE);
                this.titleView.setVisibility(View.VISIBLE);
                break;
            case ONLY_ICON:
                this.icon.setVisibility(View.VISIBLE);
                this.titleView.setVisibility(View.GONE);
                break;
        }
    }

    public void setData(Command command) {
        this.command = command;
        this.icon.setImageResource(command.getIconId());
        this.titleView.setText(command.getTitleId());
        if (command instanceof CommandBlock && state == State.FULL) {
            arrow.setVisibility(View.VISIBLE);
        } else {
            arrow.setVisibility(View.GONE);
        }
    }

    public Command getCommand() {
        return command;
    }
}
