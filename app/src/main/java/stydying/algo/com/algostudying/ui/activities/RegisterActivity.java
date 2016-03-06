package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.operations.OperationProcessor;
import stydying.algo.com.algostudying.operations.RegisterOperation;
import stydying.algo.com.algostudying.utils.ViewsUtils;

/**
 * Created by Anton on 05.02.2016.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.input_name)
    TextInputLayout inputName;
    @Bind(R.id.input_login)
    TextInputLayout inputLogin;
    @Bind(R.id.input_pass)
    TextInputLayout inputPass;
    @Bind(R.id.input_confirm_pass)
    TextInputLayout inputConfirmPass;

    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.btn_add)
    FloatingActionButton addButton;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_register);
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setTitle(R.string.title_register);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addButton.show();
        BusProvider.bus().register(this);
    }

    @Override
    protected void onPause() {
        BusProvider.bus().unregister(this);
        addButton.hide();
        super.onPause();
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        addButton.hide();
        Snackbar.make(coordinatorLayout, R.string.message_user_registered, Snackbar.LENGTH_SHORT)
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        finish();
                    }
                })
                .show();
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        Snackbar.make(coordinatorLayout, event.error.message(), Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_add)
    public void onAdd() {
        boolean hasError = hasError(inputLogin);
        hasError |= !isPassCorrect(inputConfirmPass, inputPass);
        if (!hasError) {
            OperationProcessor.executeOperation(this,
                    new RegisterOperation(
                            ViewsUtils.getEditText(inputLogin).getText().toString(),
                            ViewsUtils.getEditText(inputPass).getText().toString(),
                            ViewsUtils.getEditText(inputName).getText().toString(),
                            obtainType()));
        }
    }

    private User.Type obtainType() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_admin:
                return User.Type.ADMIN;
            case R.id.radio_student:
                return User.Type.STUDENT;
            default:
                throw new IllegalStateException("Unknown id");
        }
    }

    private boolean hasError(TextInputLayout input) {
        if (TextUtils.isEmpty(ViewsUtils.getEditText(input).getText().toString())) {
            input.setError(getString(R.string.error_empty_not_allowed));
            return true;
        } else {
            input.setError(null);
            return false;
        }
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

    public static void startMe(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }
}
