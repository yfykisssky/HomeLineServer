package com.homeline.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    public static final String FOLDER = "/config/";
    public static final String BLUETOOTH = "bluetooth.properties";
    public static final String JDBC = "jdbc.properties";
    public static final String HARDWARE = "hardware.properties";
    public static final String STEERINGENGINE = "steeringengine.properties";

    public static Properties loadProperty(String FILE_NAME,Class classC) throws IOException {
        Properties prop = new Properties();
        InputStream is = classC.getClassLoader().getResourceAsStream(FOLDER+FILE_NAME);
        prop.load(is);
        return prop;
    }

}
