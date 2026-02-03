package api;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoad {
    private static ConfigLoad configLoad;
    private static final Properties properties = new Properties();

    private ConfigLoad() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")){
            if(input != null) {
                properties.load(input);
            } else {
                throw new RuntimeException("Khong tim thay file config");
            }
        } catch (Exception e) {
            throw new RuntimeException("Loi tai file config: " + e.getMessage());
        }
    }

    public static ConfigLoad getInstance() {
        if(configLoad == null) {
            synchronized (ConfigLoad.class) {
                if (configLoad == null) {
                    configLoad = new ConfigLoad();
                }
            }
        }
        return configLoad;
    }

    public String getProperty(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    public String getBaseUrl() {
        return getProperty("baseUrl");
    }

    public String getEPLogin() {
        return getProperty("epLogin");
    }

    public String getEmail() {
        return getProperty("email");
    }

    public String getPassword() {
        return getProperty("password");
    }

    public String getEPRegister() {
        return getProperty("epRegister");
    }
}
