package sr.ice.client;

import Bulbulator.BulbPrx;
import Cameras.CamPrx;
import Lights.LightPrx;
import Lights.NoInput;
import Lights.OutOfBoundry;
import Cameras.came;
import com.zeroc.Ice.*;

import java.io.IOException;
import java.lang.Exception;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class IceClient {
	public static void main(String[] args) {
		int status = 0;
		Communicator communicator = null;

		try {
			// 1. Inicjalizacja ICE
			communicator = Util.initialize(args);

			// 2. Uzyskanie referencji obiektu na podstawie linii w pliku konfiguracyjnym (wówczas aplikację należy uruchomić z argumentem --Ice.config=config.client)
			//ObjectPrx base1 = communicator.propertyToProxy("Calc1.Proxy");

			// 2. Uzyskanie referencji obiektu - to samo co powyżej, ale mniej ładnie
			ObjectPrx base1 = communicator.stringToProxy("light11:tcp -h 127.0.0.1 -p 10000 -z : udp -h 127.0.0.1 -p 10000 -z"); //opcja -z włącza możliwość kompresji wiadomości
			ObjectPrx base2 = communicator.stringToProxy("bulb11:tcp -h 127.0.0.1 -p 10000 -z : udp -h 127.0.0.1 -p 10000 -z"); //opcja -z włącza możliwość kompresji wiadomości
			ObjectPrx base3 = communicator.stringToProxy("cam11:tcp -h 127.0.0.1 -p 10000 -z : udp -h 127.0.0.1 -p 10000 -z"); //opcja -z włącza możliwość kompresji wiadomości
			ObjectPrx base4 = communicator.stringToProxy("light12:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z"); //opcja -z włącza możliwość kompresji wiadomości
			ObjectPrx base5 = communicator.stringToProxy("bulb12:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z"); //opcja -z włącza możliwość kompresji wiadomości
			ObjectPrx base6 = communicator.stringToProxy("cam12:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z"); //opcja -z włącza możliwość kompresji wiadomości

			// 3. Rzutowanie, zawężanie (do typu Calc)
			LightPrx obj1 = LightPrx.checkedCast(base1);
			BulbPrx obj2 = BulbPrx.checkedCast(base2);
			CamPrx obj3 = CamPrx.checkedCast(base3);

			//CalcPrx obj1 = CalcPrx.uncheckedCast(base1); //na czym polega różnica?
			if (obj1 == null||obj2==null||obj3==null) throw new Error("Invalid proxy");

			CompletableFuture<Long> cfl = null;
			String line = null;
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
			int id, val;
			// 4. Wywołanie zdalnych operacji i zmiana trybu proxy dla obiektu obj1
			do {
				try {
					System.out.print("==> ");
					boolean exit = false;
					line = in.readLine();
					switch (line) {

						case "lights":
							while (!exit) {
								System.out.print("managing lights ==> ");
								line = in.readLine();
								String[] tokens = line.split(" ");
								switch (tokens[0]) {
									case "exit":
										exit = true;
										break;
									case "switch":
										if (tokens.length < 2) throw new NoInput();
										obj1._switch(Integer.parseInt(tokens[1]));
										break;
									case "print":
										if (tokens.length < 2) throw new NoInput();
										if (tokens[1].equals("all")) {
											int[] list = obj1.all();
											for (int i = 0; i < list.length; i++) {
												if (list[i]==0) System.out.println("Light nr. " + i + " is switched off.");
												else System.out.println("Light nr. " + i + " is switched on.");
											}
										}
										else{
											id = Integer.parseInt(tokens[1]);
											int res = obj1.print(id);
											if (res == 0) System.out.println("Light nr. " + id + " is switched off.");
											else System.out.println("Light nr. " + id + " is switched on.");
										}
								}
							}
							break;
						case "cameras":
							while (!exit) {
								System.out.print("managing cameras ==> ");
								line = in.readLine();
								String[] tokens = line.split(" ");
								switch (tokens[0]) {
									case "exit":
										exit = true;
										break;
									case "switch":
										if (tokens.length < 2) throw new NoInput();
										obj3._switch(Integer.parseInt(tokens[1]));
										break;
									case "print":
										if (tokens.length < 2) throw new NoInput();
										id = Integer.parseInt(tokens[1]);
										came set = obj3.print(id);
										if (set.On == 0) System.out.println("Cam nr. " + id + " is switched off.");
										else System.out.println("Cam nr. " + id + " is switched on. " + set.P+", " + set.T+", " + set.Z);
										break;
									case "turn":
										if (tokens.length < 4) throw new NoInput();
										id = Integer.parseInt(tokens[2]);
										val = Integer.parseInt(tokens[3]);
										if (tokens[1].equals("left")) {obj3.turnVertical(id, -val);}
										if (tokens[1].equals("right")) {obj3.turnVertical(id, val);}
										if (tokens[1].equals("up")) {obj3.turnHorizontal(id,val);}
										if (tokens[1].equals("down")) {obj3.turnHorizontal(id,-val);}
										break;
									case "zoom":
										if (tokens.length < 3) throw new NoInput();
										id = Integer.parseInt(tokens[1]);
										val = Integer.parseInt(tokens[2]);
										obj3.zoom(id, val);
										break;
								}
							}
							break;
						case "bulbulator":
							String res = obj2.check();
							System.out.println(res);
							break;
						default:
							System.out.println("???");
					}
				} catch (IOException | TwowayOnlyException | NoInput | OutOfBoundry ex ) {
					ex.printStackTrace(System.err);
				}
			}
			while (!Objects.equals(line, "x"));


		} catch (LocalException e) {
			e.printStackTrace();
			status = 1;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			status = 1;
		}
		if (communicator != null) { //clean
			try {
				communicator.destroy();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				status = 1;
			}
		}
		System.exit(status);
	}

}