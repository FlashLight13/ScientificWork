package stydying.algo.com.algostudying.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.ui.activities.NavigationDrawerActivity;

/**
 * Created by Anton on 18.07.2015.
 */
public class NavigationDrawerItemView extends LinearLayout {

    @Bind(R.id.indicator)
    View indicator;
    @Bind(R.id.title)
    TextView title;

    public NavigationDrawerItemView(Context context) {
        super(context, null);
        init(context);
    }

    public NavigationDrawerItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public NavigationDrawerItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavigationDrawerItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        ButterKnife.bind(inflate(context, R.layout.v_navigation_drawer_item, this));
    }

    public void setSelected(boolean isSelected) {
        indicator.setSelected(isSelected);
    }

    public void setNavigationTab(NavigationDrawerActivity.NavigationTab tab) {
        this.title.setText(tab.getTitleRes());
        int color = getResources().getColor(tab.isMain() ? R.color.black : R.color.grey);
        title.setTextColor(color);
    }
}
