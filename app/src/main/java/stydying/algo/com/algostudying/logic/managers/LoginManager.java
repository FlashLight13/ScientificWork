package stydying.algo.com.algostudying.logic.managers;

import android.content.Context;

import stydying.algo.com.algostudying.AlgoApplication;
import stydying.algo.com.algostudying.data.Database;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.logic.Preferences;
import stydying.algo.com.algostudying.operations.OperationProcessingService;

/**
 * Created by Anton on 04.02.2016.
 */
public class LoginManager {

    private User currentUser;

    public LoginManager(Context context) {
        this.currentUser = Preferences.UserPreferences.getLoggedInUser(context);
    }

    public void login(Context context, User user) {
        this.currentUser = user;
        Preferences.UserPreferences.login(context, user);
    }

    public void logout(Context context) {
        this.currentUser = null;
        OperationProcessingService.OperationsManager.get(context).onLogout();
        Database.clear();
        Preferences.UserPreferences.logout(context);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * @return current player instance or null in case of logouted user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    public User.Type getCurrentType() {
        return currentUser.getType();
    }

    public void updateCurrentUserDetails(String name, String pass) {
        this.currentUser.setName(name);
        this.currentUser.setPass(pass);
    }

    public static LoginManager getInstance(Context context) {
        AlgoApplication application = (AlgoApplication) context.getApplicationContext();
        return application.getLoginManager();
    }
}
