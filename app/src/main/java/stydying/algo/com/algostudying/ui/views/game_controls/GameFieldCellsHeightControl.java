package stydying.algo.com.algostudying.ui.views.game_controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.ui.interfaces.HeightControlListener;

/**
 * Created by Anton on 09.02.2016.
 */
public class GameFieldCellsHeightControl extends LinearLayout {

    private HeightControlListener listener;

    public GameFieldCellsHeightControl(Context context) {
        super(context);
        init(context);
    }

    public GameFieldCellsHeightControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameFieldCellsHeightControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameFieldCellsHeightControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.v_game_field_cell_height_contol, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_up)
    public void onUp() {
        if (listener != null) {
            listener.increaseHeightOfTheSelectedPosition();
        }
    }

    @OnClick(R.id.img_bot)
    public void onBot() {
        if (listener != null) {
            listener.decreaseHeightOfTheSelectedPosition();
        }
    }

    public void setControlListener(final HeightControlListener listener) {
        this.listener = listener;
    }
}
