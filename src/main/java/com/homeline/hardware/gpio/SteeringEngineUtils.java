package com.homeline.hardware.gpio;

import com.homeline.tool.PropertiesUtils;
import com.pi4j.wiringpi.Gpio;

import java.io.IOException;
import java.util.Properties;

public class SteeringEngineUtils {

    private int PIN;
    private int CYCLE;
    private double STARTCYCLE;
    private double ENDCYCLE;
    private double BETWEENCYCLE;
    private int DEGREERANGE;
    private int ONEDEGREETIMECOUNT;
    private int oneDegreeTimeCount = 0;
    private Integer nowDegree;
    private Integer toDegree;
    private boolean isRun = false;
    private boolean completeRun = false;

    SteeringEngineUtils() {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.STEERINGENGINE, SteeringEngineUtils.class);
            PIN = Integer.parseInt(prop.getProperty("PIN"));
            CYCLE = Integer.parseInt(prop.getProperty("CYCLE"));
            STARTCYCLE = Double.parseDouble(prop.getProperty("STARTCYCLE"));
            ENDCYCLE = Double.parseDouble(prop.getProperty("ENDCYCLE"));
            BETWEENCYCLE = Double.parseDouble(prop.getProperty("BETWEENCYCLE"));
            DEGREERANGE = Integer.parseInt(prop.getProperty("DEGREERANGE"));
            ONEDEGREETIMECOUNT = Integer.parseInt(prop.getProperty("ONEDEGREETIMECOUNT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toRange(int degree) {

        if (completeRun) {
            toDegree = degree;
        }

    }


    public void start() {

        if (isRun) {
            return;
        }

        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRun) {
                    completeRun = false;
                    if (nowDegree > toDegree) {
                        nowDegree--;
                    } else if (nowDegree < toDegree) {
                        nowDegree++;
                    } else {
                        nowDegree = toDegree;
                    }
                    Gpio.digitalWrite(PIN, Gpio.HIGH);
                    double highSleepTime = STARTCYCLE * 1000 + nowDegree / DEGREERANGE * BETWEENCYCLE * 1000;
                    Gpio.delayMicroseconds((long) highSleepTime);
                    Gpio.digitalWrite(PIN, Gpio.LOW);
                    double lowSleepTime = (CYCLE * 1000 - highSleepTime);
                    Gpio.delayMicroseconds((long) lowSleepTime);
                    oneDegreeTimeCount++;
                    if (oneDegreeTimeCount == ONEDEGREETIMECOUNT) {
                        oneDegreeTimeCount = 0;
                        completeRun = true;
                    }
                }

            }
        }).run();

    }

    public void cancle() {
        isRun = false;
    }

}
