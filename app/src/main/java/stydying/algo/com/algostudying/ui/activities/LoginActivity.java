package stydying.algo.com.algostudying.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.squareup.otto.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.OnClick;
import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.events.BusProvider;
import stydying.algo.com.algostudying.events.OperationErrorEvent;
import stydying.algo.com.algostudying.events.OperationSuccessEvent;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.operations.LoginOperation;
import stydying.algo.com.algostudying.operations.OperationProcessor;

public class LoginActivity extends BaseActivity {

    private static final long GO_HOME_DELAY = TimeUnit.SECONDS.toMillis(2);

    @Bind(R.id.login_button)
    ActionProcessButton loginButton;
    @Bind(R.id.input_login)
    EditText inputLogin;
    @Bind(R.id.input_pass)
    EditText inputPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_login);
        initLoginButton();
        if (LoginManager.getInstance(this).isLoggedIn()) {
            HomeActivity.startMe(LoginActivity.this);
        }
    }

    private void initLoginButton() {
        loginButton.setMode(ActionProcessButton.Mode.ENDLESS);
        loginButton.setProgress(0);
        loginButton.setEnabled(true);
    }

    @OnClick(R.id.login_button)
    protected void onClick() {
        String login = inputLogin.getText().toString();
        String pass = inputPass.getText().toString();
        OperationProcessor.executeOperation(this, new LoginOperation(login, pass));
        loginButton.setProgress(loginButton.getMaxProgress() / 2);
        setInputsEnabled(false);
    }

    private void setInputsEnabled(boolean isEnabled) {
        inputLogin.setEnabled(isEnabled);
        inputPass.setEnabled(isEnabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusProvider.bus().register(this);
    }

    @Override
    protected void onPause() {
        BusProvider.bus().unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onError(OperationErrorEvent event) {
        loginButton.setErrorText(getString(event.error.message()));
        loginButton.setProgress(loginButton.getMinProgress() - 1);
        setInputsEnabled(true);
    }

    @Subscribe
    public void onSuccess(OperationSuccessEvent event) {
        loginButton.setProgress(loginButton.getMaxProgress());
        loginButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.startMe(LoginActivity.this);
                initLoginButton();
                setInputsEnabled(true);
            }
        }, GO_HOME_DELAY);
        loginButton.setEnabled(false);
    }

    public static void startMe(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
