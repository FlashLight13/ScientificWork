package stydying.algo.com.algostudying.operations;

import android.content.Context;

import stydying.algo.com.algostudying.data.entities.stats.Stat;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.logic.managers.LoginManager;
import stydying.algo.com.algostudying.network.services.UsersService;

/**
 * Created by Anton on 03.02.2016.
 */
public class LoginOperation implements OperationProcessor.Operation<User> {

    private String login;
    private String pass;

    public LoginOperation() {
    }

    public LoginOperation(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    @Override
    public User loadFromNetwork(Context context) throws NetworkException {
        LoginManager loginManager = LoginManager.getInstance(context);
        User user;
        if (!loginManager.isLoggedIn()) {
            user = UsersService.login(login, pass);
            user.save();
            for (Stat stat : user.getStats()) {
                stat.setStatus(Stat.Status.SYNCED);
                stat.setUserForeignKey(user);
                stat.save();
            }
            loginManager.login(context, user);
        } else {
            user = loginManager.getCurrentUser();
        }
        return user;
    }

    @Override
    public User loadFromLocal(Context context) {
        return null;
    }

    @Override
    public OperationType type() {
        return OperationType.NETWORK_ONLY;
    }
}
