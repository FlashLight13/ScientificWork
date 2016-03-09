package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.commands.CommandBlock;
import stydying.algo.com.algostudying.game.commands.MoveCommand;
import stydying.algo.com.algostudying.game.commands.TurnLeftCommand;
import stydying.algo.com.algostudying.game.commands.TurnRightCommand;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.views.IndicatedListItemView;

/**
 * Created by Anton on 18.07.2015.
 */
public class AboutFragment extends BaseFragment {
    @Bind(R.id.list)
    protected ListView commandsListView;
    @Bind(R.id.description_view)
    protected TextView descriptionView;

    private Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_about);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Command> commands = new ArrayList<>();
        commands.add(new MoveCommand());
        commands.add(new TurnLeftCommand());
        commands.add(new TurnRightCommand());
        commands.add(new CommandBlock());
        adapter = new Adapter(getActivity(), commands);
        commandsListView.setAdapter(adapter);
        commandsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCommandSelected(position);
            }
        });
        onCommandSelected(0);
    }

    private void onCommandSelected(int position) {
        commandsListView.setItemChecked(position, true);
        descriptionView.setText((adapter.getItem(position)).getDescriptionId());
    }

    public class Adapter extends BaseAdapter {

        private ArrayList<Command> commands;
        private Context context;

        public Adapter(Context context, ArrayList<Command> commands) {
            this.context = context;
            this.commands = commands;
        }

        public Command getItem(int position) {
            return commands.get(position);
        }

        @Override
        public View getView(int groupPosition, View convertView,
                            ViewGroup parent) {
            IndicatedListItemView view = (IndicatedListItemView) convertView;
            if (view == null) {
                view = new IndicatedListItemView(context);
            }
            final Command command = getItem(groupPosition);
            view.setTitle(command.getTitleId());
            view.setIcon(command.getIconId());
            return view;
        }

        @Override
        public int getCount() {
            return commands.size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
