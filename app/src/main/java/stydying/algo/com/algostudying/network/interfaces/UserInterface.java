package stydying.algo.com.algostudying.network.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import stydying.algo.com.algostudying.data.entities.stats.Stat;
import stydying.algo.com.algostudying.data.entities.stats.User;

/**
 * Created by Anton on 19.03.2016.
 */
public interface UserInterface {

    @POST("/login")
    Call<User> login(@Header("Authentication") String header);

    @POST("/register")
    Call<User> register(@Header("Authentication") String header, @Body RegisterData json);

    @POST("/updateUser")
    Call<Void> updateUser(@Body NewUserData newUserData);

    @POST("/getStudents")
    Call<List<User>> getStudents();

    @POST("/removeUser")
    Call<Void> removeUser(@Body RemoveUserInfo info);

    @POST("/update_stats")
    Call<Stat> updateStat(@Header("Authentication") String header, @Body Stat stat);

    @POST("/load_stats")
    Call<List<Stat>> loadStats(@Body BaseUserRequest request);

    final class BaseUserRequest {
        public String login;

        public BaseUserRequest(String login) {
            this.login = login;
        }
    }

    final class RemoveUserInfo {
        private String login;

        public RemoveUserInfo() {
        }

        public RemoveUserInfo(String login) {
            this.login = login;
        }
    }

    final class RegisterData {

        private String type;
        private String name;

        public RegisterData() {
        }

        public RegisterData(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    final class NewUserData {
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
