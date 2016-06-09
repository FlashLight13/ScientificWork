package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Delete;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.stats.User_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.UsersService;

/**
 * Created by Anton on 07.02.2016.
 */
public class RemoveUserOperation implements OperationProcessingService.Operation {

    private String login;

    public RemoveUserOperation() {
    }

    public RemoveUserOperation(String login) {
        this.login = login;
    }

    @Override
    public Object loadFromNetwork(Context context) throws NetworkException {
        UsersService.removeUser(login);
        new Delete().from(User.class).where(User_Table.login.eq(login)).execute();
        return login;
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
