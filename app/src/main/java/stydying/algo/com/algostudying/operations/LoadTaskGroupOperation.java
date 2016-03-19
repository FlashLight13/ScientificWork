package stydying.algo.com.algostudying.operations;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.Select;

import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup_Table;
import stydying.algo.com.algostudying.errors.NetworkException;

/**
 * Created by Anton on 19.03.2016.
 */
public class LoadTaskGroupOperation implements OperationProcessor.Operation {

    private long id;

    public LoadTaskGroupOperation(long id) {
        this.id = id;
    }

    public LoadTaskGroupOperation() {
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        return new Select().from(TaskGroup.class).where(TaskGroup_Table._id.eq(id)).querySingle();
    }
}
