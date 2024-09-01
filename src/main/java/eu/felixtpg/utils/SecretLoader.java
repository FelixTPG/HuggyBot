package eu.felixtpg.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class SecretLoader {

    private static final HashMap<String, String> env = new HashMap<>();
    private static boolean loaded = false;

    public static void load() {
        loaded = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    env.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Loaded environment variables from .env file. (" + env.size() + " variables)");
    }

    public static String getEnv(String key) {
        if (!loaded) load();
        String value = env.get(key);
        return value;
    }

}
