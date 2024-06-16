package sr.ice.server;

import Cameras.Cam;
import Cameras.OutOfBoundry;
import com.zeroc.Ice.Current;
import Cameras.came;

import java.util.ArrayList;

public class CamerasServant implements Cam {
    int numberOfCams = 3;
    ArrayList<came> cams = new ArrayList<>();

    // Inicjuj każdą kamerę zerami i dodaj do tablicy cams
    public CamerasServant() {
        // Inicjuj każdą kamerę zerami i dodaj do tablicy cams
        for (int i = 0; i < numberOfCams; i++) {
            came camera = new came();
            camera.On = 0;
            camera.P = 0;
            camera.T = 0;
            camera.Z = 0;
            camera.MaxZoom = i*10;
            cams.add(camera);
        }
    }
    int[] MaxZoom = {0,10,20};
    @Override
    public void _switch(int id, Current current) throws OutOfBoundry {
        if (id<0 || id>=numberOfCams) throw new OutOfBoundry();
        if (cams.get(id).On==0) cams.get(id).On=1;
        else cams.get(id).On=0;
    }

    @Override
    public came print(int id, Current current) throws OutOfBoundry {
        if (id<0 || id>=numberOfCams) throw new OutOfBoundry();
        return cams.get(id);
    }

    @Override
    public void turnVertical(int id, int val, Current current) throws OutOfBoundry {
        if (id<0 || id>=numberOfCams) throw new OutOfBoundry();
        if (cams.get(id).P+val>=-180 && cams.get(id).P+val<=180) cams.get(id).P+=val;
    }

    @Override
    public void turnHorizontal(int id, int val, Current current) throws OutOfBoundry {
        if (id<0 || id>=numberOfCams) throw new OutOfBoundry();
        if (cams.get(id).T+val>=-180 && cams.get(id).T+val<=180) cams.get(id).T+=val;
    }

    @Override
    public void zoom(int id, int val, Current current) throws OutOfBoundry {
        if (id<0 || id>=numberOfCams) throw new OutOfBoundry();
        if (cams.get(id).Z+val>=0 && cams.get(id).Z+val<=cams.get(id).MaxZoom) cams.get(id).Z+=val;
    }
}
