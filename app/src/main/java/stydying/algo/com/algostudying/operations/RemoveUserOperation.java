package stydying.algo.com.algostudying.operations;

import android.content.Context;

import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.UsersService;

/**
 * Created by Anton on 07.02.2016.
 */
public class RemoveUserOperation implements OperationProcessor.Operation {

    private String login;

    public RemoveUserOperation() {
    }

    public RemoveUserOperation(String login) {
        this.login = login;
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        UsersService.removeUser(login);
        return login;
    }
}
