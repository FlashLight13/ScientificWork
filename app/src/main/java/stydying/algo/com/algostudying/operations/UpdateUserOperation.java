package stydying.algo.com.algostudying.operations;

import android.content.Context;
import android.text.TextUtils;

import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.network.services.UsersService;

/**
 * Created by Anton on 06.02.2016.
 */
public class UpdateUserOperation implements OperationProcessor.Operation {

    private String name;
    private String pass;
    private String login;

    public UpdateUserOperation() {
    }

    public UpdateUserOperation(String name, String pass, String login) {
        this.name = name;
        this.pass = pass;
        this.login = login;
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        UsersService.updateUser(login, pass, name);
        LoginManager loginManager = LoginManager.getInstance(context);
        if (TextUtils.equals(loginManager.getCurrentUser().getLogin(), login)) {
            LoginManager.getInstance(context).updateCurrentUserDetails(name, pass);
        }
        return null;
    }
}
