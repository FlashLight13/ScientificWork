package stydying.algo.com.algostudying.ui.fragments.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.operations.OperationProcessingService;
import stydying.algo.com.algostudying.operations.UpdateUserOperation;
import stydying.algo.com.algostudying.ui.activities.UserViewActivity;
import stydying.algo.com.algostudying.ui.fragments.homefragments.ProfileFragment;
import stydying.algo.com.algostudying.ui.interfaces.PagerController;
import stydying.algo.com.algostudying.ui.views.LoadingPlaceholderView;
import stydying.algo.com.algostudying.utils.ViewsUtils;

/**
 * Created by Anton on 06.02.2016.
 */
public class UserDetailsFragment extends ProfileFragment.BaseProfileFragment {

    private enum Mode {
        EDIT(R.drawable.ic_check_white_24dp),
        VIEW(R.drawable.ic_mode_edit_white_24dp);

        int btnDrawable;

        Mode(int btnDrawable) {
            this.btnDrawable = btnDrawable;
        }
    }

    @Bind(R.id.input_pass)
    protected TextInputLayout inputPass;
    @Bind(R.id.input_confirm_pass)
    protected TextInputLayout inputConfirmPass;
    @Bind(R.id.input_name)
    protected TextInputLayout inputName;
    @Bind(R.id.error_placeholder)
    protected LoadingPlaceholderView placeholderView;

    private FloatingActionButton btnAction = null;
    private PagerController pagerController = null;
    private Mode mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, R.layout.f_user_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setMode(Mode.VIEW);
        initFields();
        placeholderView.success();
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    public void onPause() {
        BusProvider.bus().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        Toast.makeText(getContext(), R.string.message_details_updated, Toast.LENGTH_SHORT).show();
        if (btnAction != null) {
            btnAction.setEnabled(true);
        }
        getActivity().setResult(UserViewActivity.RESULT_OK);
        placeholderView.success();
        setMode(Mode.VIEW);
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        Toast.makeText(getContext(), event.error.getMessageRes(), Toast.LENGTH_SHORT).show();
        if (btnAction != null) {
            btnAction.setEnabled(true);
        }
        initFields();
        placeholderView.success();
    }

    private User currentUser() {
        Bundle args = getArguments();
        if (args != null) {
            return args.getParcelable(ProfileFragment.USER_ARG);
        }
        return LoginManager.getInstance(getContext()).getCurrentUser();
    }

    @Override
    public void onButtonPressed() {
        switch (mode) {
            case EDIT:
                if (btnAction != null) {
                    placeholderView.loading();
                    if (isPassCorrect(inputConfirmPass, inputPass)) {
                        btnAction.setEnabled(false);
                        OperationProcessingService.executeOperation(getContext(), new UpdateUserOperation(
                                ViewsUtils.getEditText(inputName).getText().toString(),
                                ViewsUtils.getEditText(inputPass).getText().toString(),
                                currentUser().getLogin()));
                    }
                }
                break;
            case VIEW:
                setMode(Mode.EDIT);
                break;
        }
    }

    @Override
    public void setButtonAction(FloatingActionButton btn) {
        this.btnAction = btn;
    }

    private void initFields() {
        User user = currentUser();
        ViewsUtils.getEditText(inputName).setText(user.getName());
        ViewsUtils.getEditText(inputPass).setText(user.getPass());
        ViewsUtils.getEditText(inputConfirmPass).setText(user.getPass());
    }

    private boolean isPassCorrect(TextInputLayout inputPass, TextInputLayout inputConfirm) {
        String pass = ViewsUtils.getEditText(inputPass).getText().toString();
        String confirm = ViewsUtils.getEditText(inputConfirm).getText().toString();
        boolean hasError = false;
        if (TextUtils.isEmpty(pass)) {
            inputPass.setError(getString(R.string.error_empty_not_allowed));
            hasError = true;
        }
        if (TextUtils.isEmpty(confirm)) {
            inputConfirm.setError(getString(R.string.error_empty_not_allowed));
            hasError = true;
        }
        if (hasError) {
            return false;
        }
        if (!TextUtils.equals(ViewsUtils.getEditText(inputPass).getText(),
                ViewsUtils.getEditText(inputConfirm).getText())) {
            inputPass.setError(getString(R.string.error_password_not_confirmed));
            inputConfirm.setError(getString(R.string.error_password_not_confirmed));
            return false;
        } else {
            inputPass.setError(null);
            inputConfirm.setError(null);
            return true;
        }
    }

    private void setMode(Mode mode) {
        switch (mode) {
            case EDIT:
                if (pagerController != null) {
                    pagerController.hideTabs();
                }
                inputConfirmPass.setVisibility(View.VISIBLE);
                ViewsUtils.getEditText(inputName).setFocusableInTouchMode(true);
                ViewsUtils.getEditText(inputConfirmPass).setFocusableInTouchMode(true);
                ViewsUtils.getEditText(inputPass).setFocusableInTouchMode(true);
                break;
            case VIEW:
                if (pagerController != null) {
                    pagerController.showTabs();
                }
                inputConfirmPass.setVisibility(View.GONE);
                ViewsUtils.getEditText(inputName).setFocusable(false);
                ViewsUtils.getEditText(inputConfirmPass).setFocusable(false);
                ViewsUtils.getEditText(inputPass).setFocusable(false);
                ViewsUtils.getEditText(inputName).setError(null);
                ViewsUtils.getEditText(inputConfirmPass).setError(null);
                ViewsUtils.getEditText(inputPass).setError(null);

                break;
        }
        setButtonMode(mode);
        this.mode = mode;
        pagerController.setPagingEnabled(mode == Mode.VIEW);
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_cancel:
                setMode(Mode.VIEW);
                initFields();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_item_cancel).setVisible(mode == Mode.EDIT);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_edit_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setButtonMode(final Mode mode) {
        if (btnAction != null) {
            btnAction.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    if (btnAction != null) {
                        btnAction.setImageDrawable(ContextCompat.getDrawable(getContext(), (mode.btnDrawable)));
                        btnAction.show();
                    }
                }
            });
        }
    }

    public void setPagerController(PagerController pagerController) {
        this.pagerController = pagerController;
    }

    @Override
    public boolean shouldUseButton() {
        return true;
    }
}