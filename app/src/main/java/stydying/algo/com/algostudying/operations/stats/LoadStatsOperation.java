package stydying.algo.com.algostudying.operations.stats;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.Stat;
import stydying.algo.com.algostudying.data.entities.stats.Stat_Table;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.UsersService;
import stydying.algo.com.algostudying.operations.OperationProcessor;

/**
 * Created by Anton on 15.05.2016.
 */
public class LoadStatsOperation implements OperationProcessor.Operation<List<Stat>> {

    private String login;

    public LoadStatsOperation() {
    }

    public LoadStatsOperation(String login) {
        this.login = login;
    }

    @Override
    public List<Stat> loadFromNetwork(Context context) throws NetworkException {
        List<Stat> loadedStats = UsersService.loadStats(login);
        for (Stat loadedStat : loadedStats) {
            loadedStat.setStatus(Stat.Status.SYNCED);
            loadedStat.setUserForeignKey(login);
            loadedStat.save();
        }
        return loadFromLocal(context);
    }

    @Override
    public List<Stat> loadFromLocal(Context context) {
        return new Select()
                .from(Stat.class)
                .where(Stat_Table.userLogin.eq(login))
                .queryList();
    }

    @Override
    public OperationType type() {
        return OperationType.CACHE;
    }
}
