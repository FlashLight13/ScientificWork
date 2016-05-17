package stydying.algo.com.algostudying.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import stydying.algo.com.algostudying.R;

/**
 * Created by Anton on 04.05.2016.
 */
public class StatsTableRow extends TableRow {

    private TextView textTaskGroup;
    private TextView textTaskTitle;
    private TextView timeSpent;
    private TextView textTriesCount;
    private TextView textCommandsCount;

    public StatsTableRow(Context context) {
        super(context);
        init();
    }

    public StatsTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textTaskGroup = new TextView(getContext(), null, R.style.TableRow_TableRowText);
        textTaskGroup.setLayoutParams(layoutParams);
        textTaskGroup.setGravity(Gravity.RIGHT);
        addView(textTaskGroup);
        textTaskTitle = new TextView(getContext(), null, R.style.TableRow_TableRowText);
        textTaskTitle.setLayoutParams(layoutParams);
        textTaskTitle.setGravity(Gravity.RIGHT);
        addView(textTaskTitle);
        timeSpent = new TextView(getContext(), null, R.style.TableRow_TableRowText);
        timeSpent.setLayoutParams(layoutParams);
        timeSpent.setGravity(Gravity.RIGHT);
        addView(timeSpent);
        textTriesCount = new TextView(getContext(), null, R.style.TableRow_TableRowText);
        textTriesCount.setLayoutParams(layoutParams);
        textTriesCount.setGravity(Gravity.RIGHT);
        addView(textTriesCount);
        textCommandsCount = new TextView(getContext(), null, R.style.TableRow_TableRowText);
        textCommandsCount.setLayoutParams(layoutParams);
        textCommandsCount.setGravity(Gravity.RIGHT);
        addView(textCommandsCount);
    }

    public StatsTableRow setStats(TableEntity stat) {
        textTaskGroup.setText(stat.taskGroupName);
        textTaskTitle.setText(stat.taskName);
        timeSpent.setText(String.valueOf(stat.timeSpent));
        textCommandsCount.setText(String.valueOf(stat.commandsCount));
        textTriesCount.setText(String.valueOf(stat.triesCount));
        return this;
    }

    public static final class TableEntity {

        private String taskGroupName;
        private String taskName;
        private long timeSpent = 0;
        private int triesCount = 0;
        private int commandsCount = Integer.MAX_VALUE;

        public TableEntity(String taskGroupName, String taskName) {
            this.taskGroupName = taskGroupName;
            this.taskName = taskName;
        }

        public void incTimeSpend(long timeSpent) {
            this.timeSpent += timeSpent;
        }

        public void setCommandsCountIfNeeded(int commandsCount) {
            if (this.commandsCount > commandsCount) {
                this.commandsCount = commandsCount;
            }
        }

        public void incTriesCount() {
            this.triesCount++;
        }
    }
}
