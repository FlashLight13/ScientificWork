package stydying.algo.com.algostudying.ui.views.game_controls;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.commands.Command;
import stydying.algo.com.algostudying.game.commands.CommandBlock;
import stydying.algo.com.algostudying.game.commands.CycleCommandBlock;
import stydying.algo.com.algostudying.game.commands.MoveCommand;
import stydying.algo.com.algostudying.game.commands.MoveUpCommand;
import stydying.algo.com.algostudying.game.commands.TurnLeftCommand;
import stydying.algo.com.algostudying.game.commands.TurnRightCommand;

/**
 * Created by Anton on 13.07.2015.
 */
// TODO REFACTOR ALL THIS MEAT!!!
public class GameNavigationDrawerView extends FrameLayout {

    private static final int NAVIGATION_MAX_PERMITTED_WIDTH_CHILDS = 2;

    @Bind(R.id.current_commands)
    protected RecyclerView availableCommandsView;
    @Bind(R.id.bottom_divider)
    protected View bottomDivider;
    @Bind(R.id.commands_container)
    protected LinearLayout commandsContainer;
    @Bind(R.id.commands_scroll)
    protected HorizontalScrollView commandsScroll;

    private CommandsAdapter availableCommandsAdapter;

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

        availableCommandsAdapter = new CommandsAdapter(
                Arrays.asList(new MoveCommand(),
                        new MoveUpCommand(),
                        new TurnLeftCommand(),
                        new TurnRightCommand(),
                        new CycleCommandBlock())
                , getContext(), false).setOnClickListener(new CommandsAdapter.OnItemClick() {
            @Override
            public void onItemClicked(View parent, View view, int position) {
                final Command selectedCommand = availableCommandsAdapter.get(position).cloneCommand();
                final RecyclerView currentColumn = getCurrentColumn();
                CommandsAdapter adapter = (CommandsAdapter) (currentColumn).getAdapter();
                adapter.add(selectedCommand);
                adapter.notifyDataSetChanged();
                updateParentTaskIfNeeded(currentColumn, selectedCommand);
            }

            private void updateParentTaskIfNeeded(RecyclerView currentColumn, Command selectedCommand) {
                final int childCount = commandsContainer.getChildCount();
                if (childCount - 2 >= 0) {
                    RecyclerView parentView = (RecyclerView) commandsContainer.getChildAt(childCount - 2);
                    int rootPosition = (int) currentColumn.getTag(R.integer.root_command_position_key);
                    if (rootPosition >= 0) {
                        ((CommandBlock) ((CommandsAdapter) parentView.getAdapter()).get(rootPosition))
                                .addCommand(selectedCommand);
                    }
                }
            }
        });
        availableCommandsView.setHasFixedSize(true);
        availableCommandsView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        availableCommandsView.setAdapter(availableCommandsAdapter);
        addCommandsView(null, -1);
        updateCommandsSelection();
        updateWidthIfNeeded();
    }

    private void addCommandsView(@Nullable List<Command> commands, int rootPosition) {
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width),
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setHasFixedSize(true);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setAdapter(new CommandsAdapter(commands, getContext(), true)
                .setOnClickListener(new CommandsAdapter.OnItemClick() {
                    @Override
                    public void onItemClicked(View parent, View view, int position) {
                        int columnPosition = (int) parent.getTag(R.integer.position_key);
                        if (columnPosition == commandsContainer.getChildCount() - 2
                                && (int) getCurrentColumn().getTag(R.integer.root_command_position_key) == position) {
                            closeCommandsView(columnPosition + 1);
                            ((CommandsAdapter) ((RecyclerView) parent).getAdapter()).clearSelection();
                        } else {
                            ((CommandsAdapter) ((RecyclerView) parent).getAdapter()).select(position);
                            Command command = ((CommandView) view).getCommand();
                            if (command instanceof CommandBlock) {
                                CommandBlock commandBlock = (CommandBlock) command;
                                closeCommandsView(columnPosition + 1);
                                addCommandsView(new ArrayList<>(commandBlock.getCommands()), position);
                            }
                        }
                        getCurrentColumn().post(new Runnable() {
                            @Override
                            public void run() {
                                getCurrentColumn().invalidate();
                            }
                        });
                        postUpdatedSelections();
                        updateWidthIfNeeded();
                    }

                    private void postUpdatedSelections() {
                        commandsScroll.post(new Runnable() {
                            @Override
                            public void run() {
                                updateCommandsSelection();
                                commandsScroll.fullScroll(ScrollView.FOCUS_RIGHT);
                            }
                        });
                    }
                })
                .setOnRemoveListener(new CommandView.OnRemoveListener() {
                    @Override
                    public void onRemove(CommandView view, int position) {
                        closeCommandsView((int) view.getTag(R.integer.position_key));
                        updateWidthIfNeeded();
                        updateCommandsSelection();
                    }
                }));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setTag(R.integer.position_key, commandsContainer.getChildCount());
        recyclerView.setTag(R.integer.root_command_position_key, rootPosition);
        recyclerView.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.bg_game_navigation_column));
        commandsContainer.addView(recyclerView);
    }

    /**
     * Updates yellow border selection to last open commands block
     */
    private void updateCommandsSelection() {
        if (commandsContainer.getChildCount() > 1) {
            commandsContainer.getChildAt(commandsContainer.getChildCount() - 2).setActivated(false);
        }
        getCurrentColumn().setActivated(true);
    }

    private void updateWidthIfNeeded() {
        int childCount = commandsContainer.getChildCount();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            final int singleNavigationWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width);
            final int newWidth = Math.min(
                    NAVIGATION_MAX_PERMITTED_WIDTH_CHILDS * singleNavigationWidth,
                    childCount * singleNavigationWidth);
            if (layoutParams.width != newWidth) {
                layoutParams.width = newWidth;
                setLayoutParams(layoutParams);
            }
        }
    }

    private RecyclerView getCurrentColumn() {
        if (commandsContainer.getChildCount() - 1 < 0) {
            // should never happen
            throw new IllegalStateException("There is no active column");
        }
        return (RecyclerView) commandsContainer.getChildAt(commandsContainer.getChildCount() - 1);
    }

    private void closeCommandsView(int startPosition) {
        if (startPosition < commandsContainer.getChildCount()) {
            int count = commandsContainer.getChildCount() - startPosition;
            commandsContainer.removeViews(startPosition, count);
        }
    }

    public List<Command> getCommands() {
        return ((CommandsAdapter) ((RecyclerView) commandsContainer.getChildAt(0)).getAdapter()).commands;
    }

    private static class CommandsAdapter extends RecyclerView.Adapter<CommandsAdapter.ViewHolder>
            implements CommandView.OnRemoveListener {
        private List<Command> commands;
        private Context context;
        private boolean isFullCommands;

        private OnItemClick onClickListener;
        private CommandView.OnRemoveListener onRemoveListener;
        private int selectedItemIndex = -1;

        public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
            private CommandView commandView;

            public ViewHolder(CommandView v) {
                super(v);
                v.setClickable(true);
                this.commandView = v;
                this.commandView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Object tag = v.getTag(R.integer.position_key);
                if (tag != null) {
                    if (onClickListener != null) {
                        onClickListener.onItemClicked((View) v.getParent(), v, (int) tag);
                    }
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

        public CommandsAdapter setOnClickListener(OnItemClick onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public CommandsAdapter setOnRemoveListener(CommandView.OnRemoveListener onRemoveListener) {
            this.onRemoveListener = onRemoveListener;
            return this;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.commandView.setTag(R.integer.position_key, position);
            holder.commandView.setData(commands.get(position), position).setOnRemoveListener(this);
            holder.commandView.setSelected(position == selectedItemIndex);
        }

        @Override
        public int getItemCount() {
            return commands.size();
        }

        public CommandsAdapter add(Command command) {
            commands.add(command);
            return this;
        }

        public void select(int position) {
            this.selectedItemIndex = position;
            notifyDataSetChanged();
        }

        public void clearSelection() {
            this.selectedItemIndex = -1;
            notifyDataSetChanged();
        }

        @Override
        public void onRemove(CommandView commandView, int position) {
            commands.remove(position);
            if (onRemoveListener != null) {
                onRemoveListener.onRemove(commandView, position);
            }
            notifyDataSetChanged();
        }

        interface OnItemClick {
            void onItemClicked(View parent, View view, int position);
        }
    }

    public static GameNavigationDrawerView getInstance(Context context) {
        GameNavigationDrawerView view = new GameNavigationDrawerView(context);
        DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.navigation_drawer_width), ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.START;
        view.setLayoutParams(params);
        return view;
    }
}
