
		//	ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter2", "tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z");
package sr.ice.server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

public class IceServer {
	private void startServer(String[] args, String adapterName, String[] objectNames, String endpoint) {
		int status = 0;
		Communicator communicator = null;

		try {
			// 1. Inicjalizacja ICE - utworzenie communicatora
			communicator = Util.initialize(args);
			// 2. Konfiguracja adaptera
			ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(adapterName, endpoint);

			// 3. Utworzenie serwantów
			for (int i = 0; i < objectNames.length; i++) {
				adapter.add(createServant(objectNames[i]), new Identity(objectNames[i], ""));
			}

			// 4. Aktywacja adaptera
			adapter.activate();
			System.out.println(adapterName + " is activated...");

			// Pętla przetwarzania żądań
			communicator.waitForShutdown();

		} catch (Exception e) {
			e.printStackTrace(System.err);
			status = 1;
		}
		if (communicator != null) {
			try {
				communicator.destroy();
			} catch (Exception e) {
				e.printStackTrace(System.err);
				status = 1;
			}
		}
		System.exit(status);
	}
	private com.zeroc.Ice.Object createServant(String name) {
		if (name.startsWith("light")) {
			return new LightServant();
		} else if (name.startsWith("bulb")) {
			return new BulbulatorServant();
		} else if (name.startsWith("cam")) {
			return new CamerasServant();
		} else {
			throw new IllegalArgumentException("Unknown servant name: " + name);
		}
	}



	public static void main(String[] args) {
		sr.ice.server.IceServer app = new sr.ice.server.IceServer();

		// Konfiguracja dla pierwszego serwera
		String[] objectNames1 = {"light11", "bulb11", "cam11"};
		String endpoints1 = "tcp -h 127.0.0.1 -p 10000 -z : udp -h 127.0.0.1 -p 10000 -z";
		// Uruchomienie pierwszego serwera
		new Thread(() -> app.startServer(args, "Adapter1", objectNames1, endpoints1)).start();
		// Konfiguracja dla drugiego serwera
		String[] objectNames2 = {"light12", "bulb12", "cam12"};
		String endpoints2 = "tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z";
		// Uruchomienie drugiego serwera
		new Thread(() -> app.startServer(args, "Adapter2", objectNames2, endpoints2)).start();
	}
}
