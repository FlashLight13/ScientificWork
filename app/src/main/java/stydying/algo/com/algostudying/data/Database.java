package stydying.algo.com.algostudying.data;

import com.raizlabs.android.dbflow.sql.language.Delete;

import stydying.algo.com.algostudying.data.entities.stats.Stat;
import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.tasks.Task;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;

/**
 * Created by Anton on 22.02.2016.
 */
@com.raizlabs.android.dbflow.annotation.Database(name = Database.NAME, version = Database.VERSION)
public class Database {

    public static final String NAME = "AlgoDataBase";
    public static final int VERSION = 1;

    public static void clear() {
        new Delete().from(User.class).execute();
        new Delete().from(Task.class).execute();
        new Delete().from(TaskGroup.class).execute();
        new Delete().from(Stat.class).execute();
    }
}
