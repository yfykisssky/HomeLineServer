package com.homeline.hardware.gpio;

import com.homeline.hardware.HardWareStatusUtils;
import com.homeline.tool.PropertiesUtils;
import com.pi4j.wiringpi.Gpio;

import java.io.IOException;
import java.util.Properties;

public class FANUtils {

    private static int PIN;
    private static long CHECKTIME;
    private static int LIMITTEMP;
    private static long DELAYTIME;
    private boolean isRun = false;

    private static FANUtils instance;

    static {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.FAN, FANUtils.class);
            PIN = Integer.parseInt(prop.getProperty("PIN"));
            CHECKTIME = Integer.parseInt(prop.getProperty("CHECKTIME")) * 60 * 1000;
            LIMITTEMP = Integer.parseInt(prop.getProperty("LIMITTEMP"));
            DELAYTIME = Integer.parseInt(prop.getProperty("DELAYTIME")) * 60 * 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FANUtils getInstance() {

        if (instance == null) {
            synchronized (FANUtils.class) {
                if (instance == null) {
                    instance = new FANUtils();
                }
            }
        }

        return instance;
    }

    public void start() {

        Gpio.pinMode(PIN, Gpio.OUTPUT);

        if (isRun) {
            return;
        }

        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRun) {
                    double nowTemp = HardWareStatusUtils.getInstance().getCPUTemp();
                    if (nowTemp >= LIMITTEMP) {
                        Gpio.pinMode(PIN, Gpio.ALT5);
                        try {
                            Thread.sleep(DELAYTIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Gpio.pinMode(PIN, Gpio.LOW);
                        }
                        Gpio.pinMode(PIN, Gpio.LOW);
                        try {
                            Thread.sleep(CHECKTIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();

    }

    public void cancle() {
        isRun = false;
    }

}
