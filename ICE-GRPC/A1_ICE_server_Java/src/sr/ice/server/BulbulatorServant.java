package sr.ice.server;

import Bulbulator.Bulb;
import Bulbulator.Bulbs;
import com.zeroc.Ice.Current;

public class BulbulatorServant implements Bulb {
    String noises = Bulbs.value;
    @Override
    public String check(Current current) {
        return noises;
    }
}
