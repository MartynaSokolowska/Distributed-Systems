import Ice, sys
import lights_ice, bulbulator_ice, cameras_ice

class IceClient:
    def __init__(self):
        # Inicjalizacja ICE
        self.communicator = Ice.initialize(sys.argv)
        self.obj1 = None
        self.obj2 = None
        self.obj3 = None
        self.obj4 = None
        self.obj5 = None
        self.obj6 = None

    def connect_to_servers(self):
        # Uzyskanie referencji obiektu dla świateł
        base1 = self.communicator.stringToProxy(
            "light11:tcp -h 127.0.0.1 -p 10000 -z : udp -h 127.0.0.1 -p 10000 -z")
        self.obj1 = lights_ice._M_Lights.LightPrx.checkedCast(base1)

        # Uzyskanie referencji obiektu dla żarówki
        base2 = self.communicator.stringToProxy(
            "bulb11:tcp -h 127.0.0.1 -p 10000 -z : udp -h 127.0.0.1 -p 10000 -z")
        self.obj2 = bulbulator_ice._M_Bulbulator.BulbPrx.checkedCast(base2)

        # Uzyskanie referencji obiektu dla kamer
        base3 = self.communicator.stringToProxy(
            "cam11:tcp -h 127.0.0.1 -p 10000 -z : udp -h 127.0.0.1 -p 10000 -z")
        self.obj3 = cameras_ice._M_Cameras.CamPrx.checkedCast(base3)

        base4 = self.communicator.stringToProxy(
            "light12:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z")
        self.obj4 = lights_ice._M_Lights.LightPrx.checkedCast(base4)

        # Uzyskanie referencji obiektu dla żarówki
        base5 = self.communicator.stringToProxy(
            "bulb12:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z")
        self.obj5 = bulbulator_ice._M_Bulbulator.BulbPrx.checkedCast(base5)

        # Uzyskanie referencji obiektu dla kamer
        base6 = self.communicator.stringToProxy(
            "cam12:tcp -h 127.0.0.2 -p 10000 -z : udp -h 127.0.0.2 -p 10000 -z")
        self.obj6 = cameras_ice._M_Cameras.CamPrx.checkedCast(base6)

        if not self.obj1 or not self.obj2 or not self.obj3 or not self.obj4 or not self.obj5 or not self.obj6:
            raise RuntimeError("Nie można uzyskać referencji do obiektu zdalnego")

    def manage_lights(self):
        while True:
            try:
                print("managing lights")
                line = input("==> ").strip()
                tokens = line.split(" ")

                if tokens[0] == "x":
                    break
                elif tokens[0] == "switch":
                    if len(tokens) < 2:
                        raise lights_ice._M_Lights.NoInput()
                    self.obj1.switch(int(tokens[1]))
                elif tokens[0] == "print":
                    if len(tokens) < 2:
                        raise lights_ice._M_Lights.NoInput()
                    if tokens[1] == "all":
                        lights = self.obj1.all()
                        for i, state in enumerate(lights):
                            if state == 0:
                                print(f"Light nr. {i} is switched off.")
                            else:
                                print(f"Light nr. {i} is switched on.")
                    else:
                        id = int(tokens[1])
                        state = self.obj1._print(id)
                        if state == 0:
                            print(f"Light nr. {id} is switched off.")
                        else:
                            print(f"Light nr. {id} is switched on.")
                sys.stdin.flush()  # Wyczyść bufor wejściowy
            except (IOError, lights_ice._M_Lights.NoInput, lights_ice._M_Lights.OutOfBoundry) as ex:
                print(ex)

    def check_bulbulator(self):
        res = self.obj2.check()
        print(res)

    def manage_cameras(self):
        while True:
            try:
                print("managing cameras")
                line = input("==> ").strip()
                tokens = line.split(" ")

                if tokens[0] == "x":
                    break
                elif tokens[0] == "switch":
                    if len(tokens) < 2:
                        raise cameras_ice._M_Cameras.NoInput()
                    self.obj6.switch(int(tokens[1]))
                elif tokens[0] == "print":
                    if len(tokens) < 2:
                        raise cameras_ice._M_Cameras.NoInput()
                    id = int(tokens[1])
                    set = self.obj6._print(id)
                    if set.On == 0:
                        print(f"Cam nr. {id} is switched off.")
                    else:
                        print(f"Cam nr. {id} is switched on. Pan:{set.P}, Tilt:{set.T}, Zoom:{set.Z}.")
                elif tokens[0] == "turn":
                    if len(tokens) < 4:
                        raise cameras_ice._M_Cameras.NoInput()
                    id = int(tokens[2])
                    val = int(tokens[3])
                    if tokens[1] == "left":
                        self.obj6.turnVertical(id, -val)
                    if tokens[1] == "right":
                        self.obj6.turnVertical(id, val)
                    if tokens[1] == "up":
                        self.obj6.turnHorizontal(id, val)
                    if tokens[1] == "down":
                        self.obj6.turnHorizontal(id, -val)
                elif tokens[0] == "zoom":
                    if len(tokens) < 3:
                        raise cameras_ice._M_Cameras.NoInput()
                    id = int(tokens[1])
                    val = int(tokens[2])
                    self.obj6.zoom(id, val)
                sys.stdin.flush()  # Wyczyść bufor wejściowy
            except (IOError, cameras_ice._M_Cameras.NoInput, cameras_ice._M_Cameras.OutOfBoundry) as ex:
                print(ex)

    def run(self):
        try:
            self.connect_to_servers()
            while True:
                print("Select action: 'lights', 'bulbulator', 'cameras' or 'x' to exit")
                action = input("==> ").strip()
                if action == "lights":
                    self.manage_lights()
                elif action == "bulbulator":
                    self.check_bulbulator()
                elif action == "cameras":
                    self.manage_cameras()
                elif action == "x":
                    break
                else:
                    print("???")
        except (Ice.LocalException, Exception) as e:
            print(e)
        finally:
            if self.communicator:
                self.communicator.destroy()


if __name__ == "__main__":
    client = IceClient()
    client.run()
