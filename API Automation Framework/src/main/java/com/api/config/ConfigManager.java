package com.api.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads config.properties once and exposes typed getters.
 *
 * This is a tiny "singleton" style helper: the file is read a single time
 * when the class is first used, and every test reuses the same values.
 */
public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    // 'static' block runs once, the first time this class is referenced.
    static {
        try (InputStream input =
                     ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found on the classpath");
            }
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    // Private constructor: this is a utility class, never instantiate it.
    private ConfigManager() {
    }

    /** Returns a value as text, or throws if the key is missing. */
    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing config key: " + key);
        }
        return value.trim();
    }

    /** Returns a value parsed as an int. */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /** Returns a value parsed as a boolean ("true"/"false"). */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    // Convenience accessors for the values we use most.
    public static String baseUrl() {
        return get("base.url");
    }

    public static boolean logAll() {
        return getBoolean("log.all");
    }
}
