package stydying.algo.com.algostudying.operations.edit_operations;

import android.content.Context;

import java.util.List;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.errors.NetworkException;
import stydying.algo.com.algostudying.network.services.TasksService;

/**
 * Created by Anton on 19.03.2016.
 */
public class EditTaskGroupOperation extends BaseEditOperation {

    private String title;
    private long id;

    public EditTaskGroupOperation() {
    }

    public EditTaskGroupOperation(long id, String newTitle, List<User> users) {
        super(users);
        this.title = newTitle;
        this.id = id;
    }

    @Override
    public Object execute(Context context) throws NetworkException {
        return TasksService.updateTaskGroup(title, id, userIds);
    }
}
