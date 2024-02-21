package org.joshi.gyj;

import org.joshi.gyj.cache.LruCache;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

public class App {
    public static void main( String[] args ) {
        String host = "localhost";
        String port = "5000";
        String max = "100";

        // Set Spark host and port
        ipAddress(host);
        port(Integer.parseInt(port));

        // Initialize LruCache instance
        LruCache<String, String> cache = new LruCache<>(Integer.parseInt(max));

        // Define routes
        get("/api/v1/stats", (request, response) -> cache.toString());
        get("/api/v1/cache/:key", (request, response) -> getValue(request, response, cache));
        post("/api/v1/cache/:key/:value", (request, response) -> putValue(request, response, cache));
        delete("/api/v1/cache/:key", (request, response) -> removeValue(request, response, cache));
        put("/api/v1/cache/:key/:newValue", (request, response) -> updateValue(request, response, cache));
    }

    private static String getValue(Request request, Response response, LruCache<String, String> cache) {
        String key = request.params(":key");
        String value = cache.getValue(key);
        if (value != null) {
            return value;
        } else {
            response.status(404);
            return "Key not found";
        }
    }

    private static String putValue(Request request, Response response, LruCache<String, String> cache) {
        String key = request.params(":key");
        String value = request.params(":value");
        cache.putEntry(key, value);
        response.status(201);
        return "Key-value pair added to cache";
    }

    private static String removeValue(Request request, Response response, LruCache<String, String> cache) {
        String key = request.params(":key");
        boolean removed = cache.removeValue(key);
        if (removed) {
            return "Key removed from cache";
        } else {
            response.status(404);
            return "Key not found";
        }
    }

    private static String updateValue(Request request, Response response, LruCache<String, String> cache) {
        String key = request.params(":key");
        String newValue = request.params(":newValue");
        LruCache.Data<String, String> updatedData = cache.updateValue(key, newValue);
        if (updatedData != null) {
            return "Value updated in cache";
        } else {
            response.status(404);
            return "Key not found";
        }
    }
}
