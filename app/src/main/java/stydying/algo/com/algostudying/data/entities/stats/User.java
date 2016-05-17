package stydying.algo.com.algostudying.data.entities.stats;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import stydying.algo.com.algostudying.data.Database;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup_Table;

/**
 * Created by Anton on 25.01.2016.
 */
@Table(database = Database.class)
public class User extends BaseModel implements Parcelable {

    public enum Type {
        ADMIN, STUDENT
    }

    @PrimaryKey
    String login;
    @Column
    String pass;
    @Column
    String name;
    @Column
    Type type;

    List<Stat> stats;
    List<TaskGroup> taskGroups;

    public User() {

    }

    public User(String login, String pass, List<Stat> stats, String name, Type type) {
        super();
        this.login = login;
        this.pass = pass;
        this.stats = stats;
        this.name = name;
        this.type = type;
    }

    public User(String login, String pass) {
        super();
        this.login = login;
        this.pass = pass;
    }

    @SuppressWarnings("unchecked")
    private User(Parcel parcel) {
        login = parcel.readString();
        pass = parcel.readString();
        stats = parcel.readArrayList(Stat.class.getClassLoader());
        name = parcel.readString();
        type = Type.valueOf(parcel.readString());
    }

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "stats")
    public List<Stat> getStats() {
        if (stats == null) {
            stats = SQLite.select()
                    .from(Stat.class)
                    .where(Stat_Table.userLogin.eq(login))
                    .queryList();
        }
        return stats;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @OneToMany(methods = OneToMany.Method.ALL, variableName = "stats")
    public List<TaskGroup> getTaskGroups() {
        if (taskGroups == null) {
            taskGroups = SQLite.select()
                    .from(TaskGroup.class)
                    .where(TaskGroup_Table.userForeignKeyContainer_login.eq(login))
                    .queryList();
            if (taskGroups == null) {
                taskGroups = new ArrayList<>();
            }
        }
        return taskGroups;
    }

    public void setTaskGroups(List<TaskGroup> taskGroups) {
        this.taskGroups = taskGroups;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeString(pass);
        dest.writeList(stats);
        dest.writeString(name);
        dest.writeString(type.name());
    }

    public static final class ByTypeComparator implements Comparator<User> {

        @Override
        public int compare(User lhs, User rhs) {
            if (lhs.getType() == rhs.getType()) {
                return 0;
            } else {
                if (lhs.getType() == Type.ADMIN && rhs.getType() == Type.STUDENT) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }
}
