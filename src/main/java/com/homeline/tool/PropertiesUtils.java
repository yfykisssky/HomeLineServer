package com.homeline.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    public static final String BLUETOOTH = "/bluetooth.properties";

    public static Properties loadProperty(String FILE_PATH,Object object) throws IOException {
        Properties prop = new Properties();
        InputStream is = object.getClass().getResourceAsStream(FILE_PATH);
        prop.load(is);
        return prop;
    }

}
