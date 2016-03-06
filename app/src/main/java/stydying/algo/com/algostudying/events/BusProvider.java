package stydying.algo.com.algostudying.events;

import com.squareup.otto.Bus;

/**
 * Created by Anton on 03.02.2016.
 */
public class BusProvider {

    private static BusProvider instance;

    private Bus bus;

    public BusProvider() {
        this.bus = new Bus();
    }

    public void unregister(Object o) {
        bus.unregister(o);
    }

    public void register(Object o) {
        bus.register(o);
    }

    public void post(Object event) {
        bus.post(event);
    }

    public static BusProvider bus() {
        if (instance == null) {
            instance = new BusProvider();
        }
        return instance;
    }
}
