package stydying.algo.com.algostudying.network.services;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.NetworkProvider;
import stydying.algo.com.algostudying.network.parsers.EntityParser;

/**
 * Created by Anton on 02.02.2016.
 */
public class UsersService {

    public static User login(String login, String pass) throws NetworkException {
        return NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/login"),
                RequestBody.create(null, ""),
                createAuthHeaders(login, pass),
                new EntityParser<>(User.class));
    }

    public static User register(String login, String pass, String name, User.Type type) throws NetworkException {
        return NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/register"),
                RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(new RegisterData(type.name(), name))),
                createAuthHeaders(login, pass),
                new EntityParser<>(User.class));
    }

    public static void updateUser(String login, String pass, String name) throws NetworkException {
        NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/update_user"),
                RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(new NewUserData(pass, name, login))),
                null);
    }

    public static List<User> getUsers() throws NetworkException {
        return NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/get_users"),
                RequestBody.create(null, ""),
                null,
                new EntityParser<>(new TypeToken<List<User>>() {
                }));
    }

    public static void removeUser(String userToRemoveLogin) throws NetworkException {
        NetworkProvider.getInstance().executePost(
                NetworkProvider.createUrl("/remove_user"),
                RequestBody.create(MediaType.parse("application/json"),
                        new Gson().toJson(new RemoveUserInfo(userToRemoveLogin))),
                null);
    }

    private static Headers createAuthHeaders(String login, String pass) {
        return new Headers.Builder()
                .add("Authentication", Base64.encodeToString((login + "%" + pass).getBytes(), Base64.DEFAULT).trim())
                .build();
    }

    private static final class RemoveUserInfo {
        private String login;

        public RemoveUserInfo() {
        }

        public RemoveUserInfo(String login) {
            this.login = login;
        }
    }

    private static final class RegisterData {

        private String type;
        private String name;

        public RegisterData() {
        }

        public RegisterData(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    private static final class NewUserData {
        private String pass;
        private String name;
        private String login;

        public NewUserData() {
        }

        public NewUserData(String pass, String name, String login) {
            this.pass = pass;
            this.name = name;
            this.login = login;
        }
    }

}
