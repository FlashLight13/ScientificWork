package stydying.algo.com.algostudying.network.services;

import android.util.Base64;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.CallsProcessor;
import stydying.algo.com.algostudying.network.interfaces.UserInterface;

/**
 * Created by Anton on 02.02.2016.
 */
public class UsersService {

    private static UserInterface api;

    public static User login(String login, String pass) throws NetworkException {
        return new CallsProcessor<User>().executeCall(api().login(createAuthHeaders(login, pass)));
    }

    public static User register(String login, String pass, String name, User.Type type) throws NetworkException {
        return new CallsProcessor<User>().executeCall(api().register(
                createAuthHeaders(login, pass),
                new UserInterface.RegisterData(type.name(), name)));
    }

    public static void removeUser(String userToRemoveLogin) throws NetworkException {
        new CallsProcessor<>().executeCall(api()
                .removeUser(new UserInterface.RemoveUserInfo(userToRemoveLogin)));
    }

    public static void updateUser(String login, String pass, String name) throws NetworkException {
        new CallsProcessor<>().executeCall(api().updateUser(
                new UserInterface.NewUserData(pass, name, login)));
    }

    public static List<User> getUsers() throws NetworkException {
        return new CallsProcessor<List<User>>().executeCall(api().getUsers());
    }

    private static UserInterface api() {
        if (api == null) {
            api = CallsProcessor.retrofit().create(UserInterface.class);
        }
        return api;
    }

    private static String createAuthHeaders(String login, String pass) {
        return Base64.encodeToString((login + "%" + pass).getBytes(), Base64.DEFAULT).trim();
    }
}
