package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("config.properties not found in classpath!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null && !systemProperty.trim().isEmpty()) {
            return systemProperty;
        }
        String value = properties.getProperty(key);
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String inner = value.substring(2, value.length() - 1);
            String envVar = inner;
            String defaultValue = "";
            if (inner.contains(":")) {
                String[] parts = inner.split(":", 2);
                envVar = parts[0];
                defaultValue = parts[1];
            }
            String envVal = System.getenv(envVar);
            return (envVal != null && !envVal.isEmpty()) ? envVal : defaultValue;
        }
        if ("admin.username".equals(key)) {
            String envUser = System.getenv("ADMIN_USER");
            if (envUser != null && !envUser.isEmpty()) return envUser;
        } else if ("admin.password".equals(key)) {
            String envPass = System.getenv("ADMIN_PASS");
            if (envPass != null && !envPass.isEmpty()) return envPass;
        }
        return value != null ? value : "";
    }

    public static int getIntProperty(String key, int defaultValue) {
        String val = getProperty(key);
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String val = getProperty(key);
        if (val == null || val.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(val);
    }
}
