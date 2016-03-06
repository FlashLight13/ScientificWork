package stydying.algo.com.algostudying.ui.views.game_controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.ui.interfaces.ControlListener;

/**
 * Created by Anton on 25.01.2016.
 */
public class GameFieldSelectControl extends RelativeLayout {
    protected View left;
    protected View top;
    protected View right;
    protected View bottom;

    private ControlListener controlListener;

    public GameFieldSelectControl(Context context) {
        super(context);
        init(context);
    }

    public GameFieldSelectControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameFieldSelectControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameFieldSelectControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.v_game_field_control, this);

        initViews();
        initListeners();
    }

    private void initViews() {
        left = findViewById(R.id.left);
        top = findViewById(R.id.top);
        right = findViewById(R.id.right);
        bottom = findViewById(R.id.bottom);
    }

    private void initListeners() {
        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlListener != null) {
                    controlListener.moveSelectionLeft();
                }
            }
        });
        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlListener != null) {
                    controlListener.moveSelectionTop();
                }
            }
        });
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlListener != null) {
                    controlListener.moveSelectionRight();
                }
            }
        });
        bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlListener != null) {
                    controlListener.moveSelectionBottom();
                }
            }
        });
    }

    public GameFieldSelectControl setControlListener(ControlListener controlListener) {
        this.controlListener = controlListener;
        return this;
    }
}
