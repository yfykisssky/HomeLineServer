package com.homeline.hardware;

import com.homeline.tool.CommandUtils;
import com.homeline.tool.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

public class HandWareStatusUtils {

    private static HandWareStatusUtils instance;
    private static String MEMUSED;
    private static String CPUUSED;
    private static String CPUTEMP;

    public static HandWareStatusUtils getInstance() {

        if (instance == null) {
            synchronized (HandWareStatusUtils.class) {
                if (instance == null) {
                    instance = new HandWareStatusUtils();
                }
            }
        }
        return instance;
    }

    HandWareStatusUtils() {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.HARDWARE, HandWareStatusUtils.class);
            CPUTEMP = prop.getProperty("CPUTEMP");
            CPUUSED = prop.getProperty("CPUUSED");
            MEMUSED = prop.getProperty("MEMUSED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCPUTemp() {
        return CommandUtils.exeCmd(CPUTEMP);
    }

    public String getRAMinfo() {
        return CommandUtils.exeCmd(MEMUSED);
    }

    public String getCPUuse() {
        return CommandUtils.exeCmd(CPUUSED);
    }

}
