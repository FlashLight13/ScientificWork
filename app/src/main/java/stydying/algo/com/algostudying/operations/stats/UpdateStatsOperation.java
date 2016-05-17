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
public class UpdateStatsOperation implements OperationProcessor.Operation<Stat> {

    private Stat stat;

    private String login;
    private String pass;

    public UpdateStatsOperation() {
    }

    public UpdateStatsOperation(Stat stat, String login, String pass) {
        this.stat = stat;
        this.login = login;
        this.pass = pass;
    }

    @Override
    public Stat loadFromNetwork(Context context) throws NetworkException {
        performSync(context, stat, login, pass);
        List<Stat> unsyncedStats = new Select()
                .from(Stat.class)
                .where(Stat_Table.userLogin.eq(login))
                .and(Stat_Table.status.eq(Stat.Status.PENDING))
                .queryList();
        if (unsyncedStats != null && unsyncedStats.size() > 0) {
            for (Stat unsyncedStat : unsyncedStats) {
                performSync(context, unsyncedStat, login, pass);
            }
        }
        OperationProcessor.OperationsManager.get(context).resetDelayForOperation(LoadStatsOperation.class);
        return null;
    }

    private void performSync(Context context, Stat stat, String login, String pass) throws NetworkException {
        stat = UsersService.updateStat(login, pass, stat);
        stat.setStatus(Stat.Status.SYNCED);
        loadFromLocal(context);
    }

    @Override
    public Stat loadFromLocal(Context context) {
        stat.setUserForeignKey(login);
        stat.save();
        return null;
    }

    @Override
    public OperationType type() {
        return OperationType.CACHE;
    }
}
