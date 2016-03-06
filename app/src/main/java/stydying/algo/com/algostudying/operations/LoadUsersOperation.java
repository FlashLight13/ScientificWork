package stydying.algo.com.algostudying.operations;

import android.content.Context;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.UsersService;

/**
 * Created by Anton on 05.02.2016.
 */
public class LoadUsersOperation implements OperationProcessor.Operation {

    public LoadUsersOperation() {

    }

    @Override
    public Object execute(Context context) throws NetworkException {
        List<User> users = UsersService.getUsers();
        for (User user : users) {
            if (user.exists()) {
                user.save();
            } else {
                user.insert();
            }
        }
        return users;
    }
}
