package stydying.algo.com.algostudying.logic;

import android.support.annotation.NonNull;

import stydying.algo.com.algostudying.data.entities.stats.User;
import stydying.algo.com.algostudying.data.entities.tasks.TaskGroup;

/**
 * Created by Anton on 04.06.2016.
 */
public class TaskGroupsBusinessLogic {

    public static boolean hasTaskGroup(@NonNull User user, long taskGroupId) {
        for (TaskGroup taskGroup : user.getTaskGroups()) {
            if (taskGroup.getId() == taskGroupId) {
                return true;
            }
        }
        return false;
    }
}
