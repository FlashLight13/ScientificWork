package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 18.07.2015.
 */
public class IndicatedListItemView extends LinearLayout {

    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.indicator)
    View indicator;
    @Bind(R.id.title)
    TextView title;

    public IndicatedListItemView(Context context) {
        super(context, null);
        init(context);
    }

    public IndicatedListItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public IndicatedListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IndicatedListItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        ButterKnife.bind(inflate(context, R.layout.v_indicated_list_item, this));
    }

    public void setSelected(boolean isSelected) {
        indicator.setSelected(isSelected);
    }

    public void setTitle(@StringRes int title) {
        this.title.setText(title);
    }

    public void setIcon(@DrawableRes int iconRes) {
        if (iconRes > 0) {
            icon.setVisibility(VISIBLE);
            icon.setImageDrawable(ContextCompat.getDrawable(getContext(), iconRes));
        } else {
            icon.setVisibility(GONE);
            icon.setImageDrawable(null);
        }
    }
}
