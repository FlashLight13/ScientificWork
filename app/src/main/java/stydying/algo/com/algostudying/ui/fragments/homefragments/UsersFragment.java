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

import com.baoyz.widget.PullRefreshLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
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
                User chosenUser = ((PlayersAdapter) parent.getAdapter()).getItem(position);
                UserViewActivity.startMe(view, UsersFragment.this.getActivity(), chosenUser);
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
        listUsers.setEnabled(false);
        btnAdd.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                listUsers.setEnabled(true);
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
            Snackbar.make(content, event.error.getMessageRes(), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        if (event.isOperation(LoadUsersOperation.class)) {
            refreshLayout.setRefreshing(false);
            errorPlaceholder.success();
            List<User> users = event.data();
            List<User> resultUsers = new ArrayList<>(users.size());
            for (User current : users) {
                if (current.getType() == User.Type.STUDENT) {
                    resultUsers.add(current);
                }
            }
            adapter.refill(resultUsers);
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
                new RemoveUserOperation(adapter.getItem(pos).getLogin()));
    }

    private static class PlayersAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<User> data = new ArrayList<>();
        private UsersListItemView.OnUserDeleted listener;

        public PlayersAdapter(Context context, UsersListItemView.OnUserDeleted listener) {
            this.context = context;
            this.listener = listener;
        }

        private void remove(String login) {
            Iterator iterator = data.iterator();
            while (iterator.hasNext()) {
                User current = (User) iterator.next();
                if (current.getLogin().equals(login)) {
                    iterator.remove();
                    notifyDataSetChanged();
                    return;
                }
            }
        }

        private void refill(List<User> newData) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public User getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UsersListItemView view;
            if (convertView == null || !(convertView instanceof UsersListItemView)) {
                view = new UsersListItemView(context);
            } else {
                view = (UsersListItemView) convertView;
            }
            view.setData(position, getItem(position));
            view.onUserDeletedListener(listener);
            return view;
        }
    }
}
