import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Config INSTANCE = new Config();
    private final Properties properties = new Properties();

    private Config() {
        loadProperties();
        printConfig();
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private void printConfig() {
        System.out.println("BASE_URL: " + getProperty("BASE_URL"));
        System.out.println("ADD_API_URL: " + getProperty("ADD_API_URL"));
        System.out.println("LIST_API_URL: " + getProperty("LIST_API_URL"));
        System.out.println("ELEMENT_TIMEOUT: " + getProperty("ELEMENT_TIMEOUT") + " seconds");
        System.out.println("LOG_LEVEL: " + getProperty("LOG_LEVEL"));
        System.out.println("NAME: " + getProperty("NAME"));
        System.out.println("PRICE: " + getProperty("PRICE"));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return getProperty("BASE_URL");
    }

    public String getAddApiUrl() {
        return getProperty("ADD_API_URL");
    }

    public String getListApiUrl() {
        return getProperty("LIST_API_URL");
    }

    public int getElementTimeout() {
        return Integer.parseInt(getProperty("ELEMENT_TIMEOUT"));
    }

    public String getLogLevel() {
        return getProperty("LOG_LEVEL");
    }

    public String getAdminLogin() {
        return getProperty("ADMIN_LOGIN");
    }

    public String getAdminPassword() {
        return getProperty("ADMIN_PASSWORD");
    }

    public String getGoodName() {
        return getProperty("NAME");
    }

    public double getGoodPrice() {
        return Double.parseDouble(getProperty("PRICE"));
    }
}