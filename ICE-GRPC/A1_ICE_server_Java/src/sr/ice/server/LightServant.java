package sr.ice.server;

import Lights.Light;
import Lights.OutOfBoundry;
import com.zeroc.Ice.Current;

public class LightServant implements Light {
    int[] array = {0, 0, 0};
    @Override
    public void _switch(int id, Current current) throws OutOfBoundry {
        if (id < 0 || id>=array.length) throw new OutOfBoundry();
        if (array[id]==0) array[id]=1;
        else array[id]=0;
    }

    @Override
    public int print(int id, Current current) throws OutOfBoundry {
        if (id < 0 || id>=array.length) throw new OutOfBoundry();
        return array[id];
    }

    @Override
    public int[] all(Current current) {
        return array;
    }
}
