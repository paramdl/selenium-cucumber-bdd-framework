package com.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Reads config.properties; a -D system property or an environment variable wins over the file. */
public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("config.properties not found on the classpath");
            PROPERTIES.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String value = System.getProperty(key);
        if (isBlank(value)) value = System.getenv(key.replace('.', '_').toUpperCase());
        if (isBlank(value)) value = PROPERTIES.getProperty(key);
        if (isBlank(value)) throw new IllegalStateException("Property not found: " + key);
        return value.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static String getBaseUrl()          { return get("base.url"); }
    public static String getAdminUsername()    { return get("admin.username"); }
    public static String getAdminPassword()    { return get("admin.password"); }
    public static String getBrowser()          { return get("browser"); }
    public static boolean isHeadless()         { return Boolean.parseBoolean(get("headless")); }
    public static int getExplicitWait()        { return Integer.parseInt(get("explicit.wait")); }
    public static int getPageLoadTimeout()     { return Integer.parseInt(get("page.load.timeout")); }
    public static String getScreenshotsPath()  { return get("screenshots.path"); }
    public static boolean screenshotOnFailure() { return Boolean.parseBoolean(get("screenshot.on.failure")); }
}
