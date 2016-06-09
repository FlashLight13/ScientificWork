package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.UsersService;

/**
 * Created by Anton on 05.02.2016.
 */
public class LoadUsersOperation implements OperationProcessingService.Operation<List<User>> {

    public LoadUsersOperation() {

    }

    @Override
    public List<User> loadFromNetwork(Context context) throws NetworkException {
        List<User> users = UsersService.getUsers();
        for (User user : users) {
            if (user.exists()) {
                user.save();
            } else {
                user.update();
            }
        }
        return users;
    }

    @Override
    public List<User> loadFromLocal(Context context) {
        return new Select().from(User.class).queryList();
    }

    @Override
    public OperationType type() {
        return OperationType.CACHE;
    }
}
