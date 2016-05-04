package stydying.algo.com.algostudying.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableRow;
import android.widget.TextView;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.Stats;

/**
 * Created by Anton on 04.05.2016.
 */
public class StatsTableRow extends TableRow {

    private TextView textTaskGroup;
    private TextView textTaskTitle;
    private TextView textAverageTime;
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
        textTaskGroup = new TextView(getContext(), null, R.style.TableRowText);
        addView(textTaskGroup);
        textTaskTitle = new TextView(getContext(), null, R.style.TableRowText);
        addView(textTaskTitle);
        textAverageTime = new TextView(getContext(), null, R.style.TableRowText);
        addView(textAverageTime);
        textTriesCount = new TextView(getContext(), null, R.style.TableRowText);
        addView(textTriesCount);
        textCommandsCount = new TextView(getContext(), null, R.style.TableRowText);
        addView(textCommandsCount);
    }

    public StatsTableRow setStats(Stats stats) {
        textTaskGroup.setText(stats.getGroupName());
        textTaskTitle.setText(stats.getTaskName());
        textAverageTime.setText(String.valueOf(stats.getAverageTime()));
        textTriesCount.setText(stats.getTriesCount());
        textCommandsCount.setText(stats.getCommandsCount());
        return this;
    }
}
