package com.homeline.hardware.gpio;

import com.homeline.tool.PropertiesUtils;
import com.pi4j.wiringpi.Gpio;

import java.io.IOException;
import java.util.Properties;

public class StepEngineUtils {

    private static int PIN1;
    private static int PIN2;
    private static int PIN3;
    private static int PIN4;
    private static long SPEEDSLEEP;
    private static int[] PINS;

    private static StepEngineUtils instance;

    static{
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.STEPENGINE, StepEngineUtils.class);
            PIN1 = Integer.parseInt(prop.getProperty("PIN1"));
            PIN2 = Integer.parseInt(prop.getProperty("PIN2"));
            PIN3 = Integer.parseInt(prop.getProperty("PIN3"));
            PIN4 = Integer.parseInt(prop.getProperty("PIN4"));
            PINS = new int[]{PIN1, PIN2, PIN3, PIN4};
            SPEEDSLEEP = (long)(Double.parseDouble(prop.getProperty("SPEEDSLEEP")) * 1000);
            for (int t = 0; t < PINS.length; t++) {
                Gpio.pinMode(PINS[t], Gpio.OUTPUT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StepEngineUtils getInstance() {

        if (instance == null) {
            synchronized (StepEngineUtils.class) {
                if (instance == null) {
                    instance = new StepEngineUtils();
                }
            }
        }

        return instance;
    }

    public void toLeft(int steps) {

        for (int t = 0; t < steps; t++) {
            for (int y = 0; y <= 3; y++) {
                boolean values[] = new boolean[4];
                values[y] = true;
                writePins(values);
            }
            Gpio.delayMicroseconds(SPEEDSLEEP);
        }

    }

    public void toRight(int steps) {

        for (int t = 0; t < steps; t++) {
            for (int y = 3; y >= 0; y--) {
                boolean values[] = new boolean[4];
                values[y] = true;
                writePins(values);
            }
            Gpio.delayMicroseconds(SPEEDSLEEP);
        }

    }

    private void writePins(boolean values[]) {

        for (int t = 0; t < PINS.length; t++) {
            if (values[t]) {
                Gpio.digitalWrite(PINS[t], Gpio.HIGH);
            } else {
                Gpio.digitalWrite(PINS[t], Gpio.LOW);
            }
            Gpio.delayMicroseconds(SPEEDSLEEP);
        }

    }

}
