package stydying.algo.com.algostudying.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import stydying.algo.com.algostudying.data.entities.stats.User;

/**
 * Created by Anton on 10.02.2016.
 */
public class Preferences {

    // TODO rework this amazing-super-mega code using db table
    public static final class UserPreferences {

        private static final String USER_KEY
                = "stydying.algo.com.algostudying.logic.UserPreferences.USER_KEY";

        public static void login(Context context, User user) {
            SharedPreferences sharedPreferences = getPrefs(context);
            sharedPreferences.edit().putString(USER_KEY, new Gson().toJson(user)).apply();
        }

        public static User getLoggedInUser(Context context) {
            SharedPreferences sharedPreferences = getPrefs(context);
            String userJson = sharedPreferences.getString(USER_KEY, null);
            if (!TextUtils.isEmpty(userJson)) {
                return new Gson().fromJson(userJson, User.class);
            }
            return null;
        }

        public static void logout(Context context) {
            SharedPreferences sharedPreferences = getPrefs(context);
            sharedPreferences.edit().putString(USER_KEY, "").apply();
        }

        private static SharedPreferences getPrefs(Context context) {
            return context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        }
    }
}
