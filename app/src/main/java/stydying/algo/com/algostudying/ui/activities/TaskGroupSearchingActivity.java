package stydying.algo.com.algostudying.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.Select;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup_Table;

/**
 * Created by Anton on 28.02.2016.
 */
public class TaskGroupSearchingActivity extends BaseActivity {

    public static final String TASK_GROUP_EXTRA
            = "stydying.algo.com.algostudying.ui.activities.TaskGroupSearchingActivity.TASK_GROUP_EXTRA";
    public static final int REQUEST_CODE = 1319;

    @Bind(R.id.list)
    ListView listView;

    private SearchView searchView;
    private TasksGroupAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_task_group_searching);

        adapter = new TasksGroupAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onGroupSelected(adapter.getTaskGroup(position));
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void onGroupSelected(TaskGroup group) {
        Intent data = new Intent();
        data.putExtra(TASK_GROUP_EXTRA, group);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm_group, menu);
        MenuItem item = menu.findItem(R.id.search);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.query(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.query(newText);
                return true;
            }
        });
        searchView.setQueryHint(getString(R.string.world_setup_group_hint));
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        item.expandActionView();
        EditText editText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setTextColor(ContextCompat.getColor(this, R.color.white));
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.lightest_grey));
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_group:
                onGroupSelected(new TaskGroup(searchView.getQuery().toString()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void startMe(Activity activity, @Nullable View fromView) {
        Bundle options = null;
        if (fromView != null) {
            options = ActivityOptionsCompat.makeScaleUpAnimation(
                    fromView, 0, 0, fromView.getWidth(), fromView.getHeight()).toBundle();
        }
        Intent intent = new Intent(activity, TaskGroupSearchingActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivityForResult(intent, REQUEST_CODE, options);
        } else {
            activity.startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    private static class TasksGroupAdapter extends CursorAdapter {

        private TasksGroupAdapter(Context context) {
            super(context, new Select().from(TaskGroup.class).query(), true);
        }

        private void query(String searchText) {
            searchText = searchText.trim();
            Cursor newCursor;
            if (TextUtils.isEmpty(searchText)) {
                newCursor = new Select().from(TaskGroup.class).query();
            } else {
                newCursor = new Select().from(TaskGroup.class).where(
                        TaskGroup_Table.title.like(searchText + "%")).query();
            }
            swapCursor(newCursor).close();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            TextView textView = new TextView(context, null, R.style.ListItemStyle);
            textView.setLayoutParams(new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    context.getResources().getDimensionPixelSize(R.dimen.list_item_height_medium)));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.basic_list_item_sides_margin);
            textView.setPadding(padding, 0, padding, 0);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    context.getResources().getDimension(R.dimen.text_size_medium));
            return textView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view).setText(
                    cursor.getString(cursor.getColumnIndex(TaskGroup_Table.title.getContainerKey())));
        }

        private TaskGroup getTaskGroup(int position) {
            Cursor cursor = (Cursor) getItem(position);
            TaskGroup result = new TaskGroup(cursor.getString(cursor.getColumnIndex(TaskGroup_Table.title.getContainerKey())));
            result.setId(cursor.getLong(cursor.getColumnIndex(TaskGroup_Table._id.getContainerKey())));
            return result;
        }
    }
}
