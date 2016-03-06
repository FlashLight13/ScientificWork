package stydying.algo.com.algostudying.ui.views.game_controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.commands.CommandBlock;
import stydying.algo.com.algostudying.game.commands.CycleCommandBlock;
import stydying.algo.com.algostudying.game.commands.MoveCommand;

/**
 * Created by Anton on 13.07.2015.
 */
public class GameNavigationDrawerView extends FrameLayout {

    @Bind(R.id.commands_list)
    RecyclerView commandsListView;
    @Bind(R.id.current_commands)
    RecyclerView currentCommandsView;
    @Bind(R.id.function_commands_container)
    RecyclerView functionCommandsContainerView;
    @Bind(R.id.bottom_divider)
    View bottomDivider;
    //@ViewById(R.id.horizontal_view)
    //protected HorizontalScrollView horizontalScrollView;

    private List<Command> currentCommandsList;
    private List<Command> commandsList;

    public GameNavigationDrawerView(Context context) {
        super(context, null);
        init(context);
    }

    public GameNavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public GameNavigationDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameNavigationDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(Context context) {
        inflate(context, R.layout.v_navigation_drawer_layout, this);
        ButterKnife.bind(this);
        commandsList = new ArrayList<>();
        currentCommandsList = new ArrayList<>();

        commandsList.add(new MoveCommand());
        commandsList.add(new MoveCommand());
        commandsList.add(new MoveCommand());
        commandsList.add(new MoveCommand());
        commandsList.add(new MoveCommand());
        CycleCommandBlock cycle = new CycleCommandBlock();
        cycle.addCommand(new MoveCommand());
        cycle.addCommand(new MoveCommand());
        cycle.addCommand(new MoveCommand());
        commandsList.add(cycle);

        commandsListView.setHasFixedSize(true);
        commandsListView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommandsAdapter adapter = new CommandsAdapter(commandsList, getContext(), true);
        adapter.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Command command = ((CommandsAdapter) commandsListView.getAdapter()).get(position);
                if (command instanceof CommandBlock) {
                    FunctionDataContainer tag = (FunctionDataContainer) functionCommandsContainerView.getTag(R.integer.position_key);
                    if (tag != null && tag.position == position && tag.isOpened) {
                        tag.isOpened = false;
                        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) GameNavigationDrawerView.this.getLayoutParams();
                        params.width = params.width - getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width);
                        GameNavigationDrawerView.this.setLayoutParams(params);
                        functionCommandsContainerView.setVisibility(GONE);
                    } else {
                        functionCommandsContainerView.setAdapter(new CommandsAdapter(((CommandBlock) command).getCommands(), getContext(), true));
                        if (tag == null) {
                            tag = new FunctionDataContainer();
                        }
                        tag.isOpened = true;
                        tag.position = position;
                        functionCommandsContainerView.setTag(R.integer.position_key, tag);
                        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) GameNavigationDrawerView.this.getLayoutParams();
                        params.width = params.width + getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width);
                        GameNavigationDrawerView.this.setLayoutParams(params);
                        functionCommandsContainerView.setVisibility(VISIBLE);
                    }
                }
            }
        });
        commandsListView.setAdapter(adapter);


        currentCommandsList.add(new MoveCommand());
        currentCommandsList.add(new MoveCommand());
        currentCommandsList.add(new MoveCommand());
        currentCommandsList.add(new MoveCommand());
        currentCommandsList.add(new MoveCommand());
        currentCommandsList.add(new MoveCommand());
        currentCommandsView.setHasFixedSize(true);
        currentCommandsView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        currentCommandsView.setAdapter(new CommandsAdapter(currentCommandsList, getContext(), false));

        functionCommandsContainerView.setHasFixedSize(true);
        functionCommandsContainerView.setLayoutManager(new LinearLayoutManager(getContext()));
        functionCommandsContainerView.setAdapter(new CommandsAdapter(null, getContext(), true));
    }

    public void setCommandsList(List<Command> commandsList) {
        this.commandsList = commandsList;
    }

    public void setCurrentCommandsList(List<Command> currentCommandsList) {
        this.currentCommandsList = currentCommandsList;
    }

    private static class CommandsAdapter extends RecyclerView.Adapter<CommandsAdapter.ViewHolder> {
        private List<Command> commands;
        private Context context;
        private boolean isFullCommands;

        private AdapterView.OnItemClickListener onClickListener;

        public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
            private CommandView commandView;

            public ViewHolder(CommandView v) {
                super(v);
                this.commandView = v;
                this.commandView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Object tag = v.getTag(R.integer.position_key);
                if (tag != null) {
                    onClickListener.onItemClick(null, v, (int) tag, 0);
                }
            }
        }

        public CommandsAdapter(List<Command> commands, Context context, boolean isFullCommands) {
            this.commands = commands;
            if (this.commands == null) {
                this.commands = new ArrayList<>();
            }
            this.context = context;
            this.isFullCommands = isFullCommands;
        }

        @Override
        public CommandsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CommandView commandView = new CommandView(context);
            commandView.moveToState(isFullCommands ? CommandView.State.FULL : CommandView.State.ONLY_ICON);
            return new ViewHolder(commandView);
        }

        public Command get(int position) {
            return commands.get(position);
        }

        public void setOnClickListener(AdapterView.OnItemClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.commandView.setTag(R.integer.position_key, position);
            holder.commandView.setData(commands.get(position));
        }

        @Override
        public int getItemCount() {
            return commands.size();
        }
    }

    private class FunctionDataContainer {
        int position;
        boolean isOpened;
    }

    public static GameNavigationDrawerView getInstance(Context context) {
        GameNavigationDrawerView view = new GameNavigationDrawerView(context);
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width), ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.START;
        view.setLayoutParams(params);
        return view;
    }
}
