package stydying.algo.com.algostudying.operations;

import android.content.Context;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.Select;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.stats.User_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.network.services.UsersService;

/**
 * Created by Anton on 06.02.2016.
 */
public class UpdateUserOperation implements OperationProcessingService.Operation {

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
    public Object loadFromNetwork(Context context) throws NetworkException {
        UsersService.updateUser(login, pass, name);
        LoginManager loginManager = LoginManager.getInstance(context);
        if (TextUtils.equals(loginManager.getCurrentUser().getLogin(), login)) {
            LoginManager.getInstance(context).updateCurrentUserDetails(name, pass);
        } else {
            User existedUser = new Select().from(User.class).where(User_Table.login.eq(login)).querySingle();
            existedUser.setPass(pass);
            existedUser.setName(name);
            existedUser.save();
        }
        return null;
    }

    @Override
    public Object loadFromLocal(Context context) {
        return null;
    }

    @Override
    public OperationType type() {
        return OperationType.NETWORK_ONLY;
    }
}
