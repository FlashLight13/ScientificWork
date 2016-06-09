package stydying.algo.com.algostudying.ui.fragments.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.Stat;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.operations.OperationProcessingService;
import stydying.algo.com.algostudying.operations.stats.LoadStatsOperation;
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
        String currentUserLogin = LoginManager.getInstance(getContext()).getCurrentUser().getLogin();
        User openedUser = getArguments().getParcelable(ProfileFragment.USER_ARG);
        String openedUserLogin = openedUser == null ? "" : openedUser.getLogin();
        placeholder.setText(TextUtils.equals(currentUserLogin, openedUserLogin)
                ? R.string.stats_empty
                : R.string.stats_empty_admin);
        OperationProcessingService.executeOperation(getContext(),
                new LoadStatsOperation(LoginManager.getInstance(getContext()).getCurrentUser().getLogin()));
    }

    private void updateStats(List<Stat> statList) {
        if (statList == null || statList.isEmpty()) {
            placeholder.setVisibility(View.VISIBLE);
        } else {
            placeholder.setVisibility(View.GONE);
            // remove all views except header
            tableStats.removeViews(1, tableStats.getChildCount() - 1);
            for (StatsTableRow.TableEntity tableEntity : StatsTableProcessor.processStats(statList)) {
                tableStats.addView(new StatsTableRow(getContext()).setStats(tableEntity));
            }
        }
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        if (event.isOperation(LoadStatsOperation.class)) {
            updateStats(LoginManager.getInstance(getContext()).getCurrentUser().getStats());
        }
    }

    @SuppressWarnings("unchecked")
    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(LoadStatsOperation.class)) {
            updateStats((List<Stat>) event.data());
        }
    }

    @Override
    public void onPause() {
        BusProvider.bus().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onButtonPressed() {
        // there is no button on this fragment
    }

    @Override
    public void setButtonAction(FloatingActionButton btn) {
        // there is no button on this fragment
    }

    @Override
    public boolean shouldUseButton() {
        return false;
    }

    public void setPagerController(PagerController pagerController) {
    }

    private static final class StatsTableProcessor {

        @NonNull
        public static Iterable<StatsTableRow.TableEntity> processStats(@Nullable List<Stat> stats) {
            if (stats == null || stats.size() == 0) {
                return new ArrayList<>(0);
            }
            Map<MapKey, StatsTableRow.TableEntity> map = new HashMap<>();
            MapKey key;
            for (Stat stat : stats) {
                key = new MapKey(stat.getTaskGroupId(), stat.getTaskId());
                StatsTableRow.TableEntity entity;
                if (map.containsKey(key)) {
                    entity = map.get(key);
                } else {
                    entity = new StatsTableRow.TableEntity(stat.getGroupName(), stat.getTaskName());
                }
                entity.incTimeSpend(stat.getTime());
                entity.setCommandsCountIfNeeded(stat.getCommandsCount());
                entity.incTriesCount();
                map.put(key, entity);
            }
            return map.values();
        }


        private static final class MapKey {
            private long groupId;
            private long taskId;

            public MapKey(long groupId, long taskId) {
                this.groupId = groupId;
                this.taskId = taskId;
            }

            @Override
            public int hashCode() {
                int hash = 1;
                hash = hash * 31 + ((Long) taskId).hashCode();
                hash = hash * 31 + ((Long) groupId).hashCode();
                return hash;
            }

            @Override
            public boolean equals(Object o) {
                return o instanceof MapKey && groupId == ((MapKey) o).groupId && taskId == ((MapKey) o).taskId;
            }
        }
    }
}
