package com.homeline.hardware.gpio;

import com.homeline.hardware.HardWareStatusUtils;
import com.homeline.tool.PropertiesUtils;
import com.pi4j.wiringpi.Gpio;

import java.io.IOException;
import java.util.Properties;

public class LaserUtils {

    private static int PIN;

    private static LaserUtils instance;

    static {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.FAN, LaserUtils.class);
            PIN = Integer.parseInt(prop.getProperty("PIN"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LaserUtils getInstance() {

        if (instance == null) {
            synchronized (LaserUtils.class) {
                if (instance == null) {
                    instance = new LaserUtils();
                }
            }
        }

        return instance;
    }

    public void on() {


    }

    public void off() {
       //isRun = false;
    }

}
