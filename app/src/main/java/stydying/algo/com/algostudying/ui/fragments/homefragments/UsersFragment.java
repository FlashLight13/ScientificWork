package stydying.algo.com.algostudying.ui.fragments.homefragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.operations.LoadUsersOperation;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.operations.RemoveUserOperation;
import stydying.algo.com.algostudying.ui.activities.RegisterActivity;
import stydying.algo.com.algostudying.ui.activities.UserViewActivity;
import stydying.algo.com.algostudying.ui.fragments.BaseFragment;
import stydying.algo.com.algostudying.ui.views.LoadingPlaceholderView;
import stydying.algo.com.algostudying.ui.views.UsersListItemView;

/**
 * Created by Anton on 01.02.2016.
 */
public class UsersFragment extends BaseFragment implements UsersListItemView.OnUserDeleted {

    @Bind(R.id.refresh_layout)
    PullRefreshLayout refreshLayout;
    @Bind(R.id.content)
    CoordinatorLayout content;
    @Bind(R.id.error_placeholder)
    LoadingPlaceholderView errorPlaceholder;
    @Bind(R.id.list_users)
    ListView listUsers;
    @Bind(R.id.btn_add)
    FloatingActionButton btnAdd;

    private PlayersAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_users);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PlayersAdapter(getContext(), this);
        listUsers.setAdapter(adapter);
        listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User chosenUser = ((PlayersAdapter) parent.getAdapter()).getUser(position);
                UserViewActivity.startMe(UsersFragment.this.getActivity(), chosenUser);
            }
        });

        OperationProcessor.executeOperation(getContext(), new LoadUsersOperation());
        errorPlaceholder.setOnRetryListener(new LoadingPlaceholderView.OnRetryListener() {
            @Override
            public void onRetry() {
                errorPlaceholder.loading();
                OperationProcessor.OperationsManager.get(UsersFragment.this.getContext())
                        .resetDelayForOperation(LoadUsersOperation.class);
                OperationProcessor.executeOperation(getContext(), new LoadUsersOperation());
            }
        });
        errorPlaceholder.loading();
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OperationProcessor.OperationsManager.get(UsersFragment.this.getContext())
                        .resetDelayForOperation(LoadUsersOperation.class);
                OperationProcessor.executeOperation(getContext(), new LoadUsersOperation());
                refreshLayout.setRefreshing(true);
            }
        });
    }

    @OnClick(R.id.btn_add)
    public void onAdd() {
        btnAdd.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                super.onHidden(fab);
                RegisterActivity.startMe(getActivity());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        btnAdd.show();
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        BusProvider.bus().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        if (event.isOperation(LoadUsersOperation.class)) {
            refreshLayout.setRefreshing(false);
            errorPlaceholder.error(event.error);
            return;
        }
        if (event.isOperation(RemoveUserOperation.class)) {
            for (int i = 0; i < listUsers.getChildCount(); i++) {
                listUsers.getChildAt(i).setClickable(true);
            }
            Snackbar.make(content, event.error.message(), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(LoadUsersOperation.class)) {
            refreshLayout.setRefreshing(false);
            errorPlaceholder.success();
            List<User> users = event.data();
            Collections.sort(users, new User.ByTypeComparator());
            adapter.refill(users, Arrays.asList(R.string.header_teachers, R.string.header_students));
            return;
        }
        if (event.isOperation(RemoveUserOperation.class)) {
            adapter.remove((String) event.data());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            if (requestCode == RegisterActivity.REQUEST_CODE
                    || requestCode == UserViewActivity.REQUEST_CODE) {
                OperationProcessor.executeOperation(getContext(), new LoadUsersOperation());
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onUserDeleted(int pos) {
        View view = listUsers.getChildAt(pos);
        if (view != null) {
            view.setClickable(false);
        }
        OperationProcessor.executeOperation(getContext(),
                new RemoveUserOperation(adapter.getUser(pos).getLogin()));
    }

    private static class PlayersAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<ListItem> data = new ArrayList<>();
        private UsersListItemView.OnUserDeleted listener;

        public PlayersAdapter(Context context, UsersListItemView.OnUserDeleted listener) {
            this.context = context;
            this.listener = listener;
        }

        private void remove(String login) {
            Iterator iterator = data.iterator();
            while (iterator.hasNext()) {
                ListItem current = (ListItem) iterator.next();
                if (current.type == ListItem.ITEM) {
                    if (((User) current.data).getLogin().equals(login)) {
                        iterator.remove();
                        notifyDataSetChanged();
                        return;
                    }
                }
            }
        }

        private void refill(List<User> newData, List<Integer> headers) {
            data.clear();
            data.ensureCapacity(newData.size() + headers.size());
            Iterator<Integer> headersIterator = headers.iterator();
            for (int i = 0; i < newData.size(); i++) {
                if (i == 0) {
                    data.add(new ListItem(ListItem.HEADER, headersIterator.next()));
                } else {
                    if (newData.get(i).getType() != newData.get(i - 1).getType()) {
                        data.add(new ListItem(ListItem.HEADER, headersIterator.next()));
                    }
                }
                data.add(new ListItem(ListItem.ITEM, newData.get(i)));
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        public User getUser(int position) {
            switch (getItemViewType(position)) {
                case ListItem.HEADER:
                    return (User) getItem(position + 1).data;
                case ListItem.ITEM:
                    return (User) getItem(position).data;
                default:
                    throw new IllegalStateException("Unknown item type");
            }
        }

        @Override
        public ListItem getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case ListItem.HEADER:
                    View header;
                    if (convertView == null || !(convertView instanceof TextView)) {
                        header = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.v_list_header, parent, false);
                    } else {
                        header = convertView;
                    }
                    ((TextView) header.findViewById(R.id.text)).setText((Integer) getItem(position).data);
                    return header;
                case ListItem.ITEM:
                    UsersListItemView view;
                    if (convertView == null || !(convertView instanceof UsersListItemView)) {
                        view = new UsersListItemView(context);
                    } else {
                        view = (UsersListItemView) convertView;
                    }
                    view.setData(position, (User) getItem(position).data);
                    view.onUserDeletedListener(listener);
                    return view;
                default:
                    throw new IllegalStateException("Unknown item type");
            }
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public int getViewTypeCount() {
            return ListItem.TYPES_COUNT;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) != ListItem.HEADER;
        }

        private static final class ListItem {
            public static final int TYPES_COUNT = 2;

            public static final int HEADER = 0;
            public static final int ITEM = 1;

            public final int type;
            public final Object data;

            public ListItem(int type, Object data) {
                this.type = type;
                this.data = data;
            }
        }
    }
}
