package sr.ice.server;

import java.util.HashMap;
import java.util.Map;

public class MiddlewareManager {
    private Map<String, Middleware> middlewareMap = new HashMap<>();

    // Metoda dodająca middleware do mapy
    public void addMiddleware(String identity, Middleware middleware) {
        middlewareMap.put(identity, middleware);
    }

    // Metoda zwracająca middleware na podstawie identyfikatora
    public Middleware getMiddleware(String identity) {
        return middlewareMap.get(identity);
    }

    // Przykładowa implementacja klasy Middleware
    public static class Middleware {
        private String identity;

        public Middleware(String identity) {
            this.identity = identity;
        }

        public void doSomething() {
            System.out.println("Middleware " + identity + " is doing something.");
        }
    }
}
