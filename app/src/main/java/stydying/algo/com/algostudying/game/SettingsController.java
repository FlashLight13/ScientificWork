package stydying.algo.com.algostudying.game;

/**
 * Created by Anton on 04.05.2016.
 */
public class SettingsController {

    private static final long BETWEEN_OPERATIONS_DELAY = 500;

    private static SettingsController instance;

    public SettingsController() {
    }

    public long getBetweenOperationsDelay() {
        return BETWEEN_OPERATIONS_DELAY;
    }

    synchronized public static SettingsController getInstance() {
        if (instance == null) {
            instance = new SettingsController();
        }
        return instance;
    }
}
