package stydying.algo.com.algostudying.ui.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.Stats;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.ui.fragments.homefragments.ProfileFragment;
import stydying.algo.com.algostudying.ui.interfaces.PagerController;
import stydying.algo.com.algostudying.ui.views.StatsTableRow;

/**
 * Created by Anton on 06.02.2016.
 */
public class StatsFragment extends ProfileFragment.BaseProfileFragment {

    @Bind(R.id.table_stats)
    protected TableLayout tableStats;
    @Bind(R.id.placeholder)
    protected TextView placeholder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_stats);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateStats();
    }

    private void updateStats() {
        final User currentUser = LoginManager.getInstance(getContext()).getCurrentUser();
        final List<Stats> statsList = currentUser.getStats();
        if (statsList == null || statsList.isEmpty()) {
            placeholder.setVisibility(View.VISIBLE);
        } else {
            placeholder.setVisibility(View.GONE);
            // remove all views except header
            tableStats.removeViews(1, tableStats.getChildCount() - 1);
            for (Stats stats : statsList) {
                tableStats.addView(new StatsTableRow(getContext()).setStats(stats));
            }
        }
    }

    @Override
    public void onButtonPressed() {

    }

    @Override
    public void setButtonAction(FloatingActionButton btn) {

    }

    @Override
    public boolean shouldUseButton() {
        return false;
    }

    public void setPagerController(PagerController pagerController) {
    }
}
