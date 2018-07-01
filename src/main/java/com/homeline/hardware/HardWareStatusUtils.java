package com.homeline.hardware;

import com.homeline.tool.CommandUtils;
import com.homeline.tool.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

public class HardWareStatusUtils {

    private static HardWareStatusUtils instance;
    private static String MEMUSED;
    private static String CPUUSED;
    private static String CPUTEMP;

    public static HardWareStatusUtils getInstance() {

        if (instance == null) {
            synchronized (HardWareStatusUtils.class) {
                if (instance == null) {
                    instance = new HardWareStatusUtils();
                }
            }
        }
        return instance;
    }

    HardWareStatusUtils() {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.HARDWARE, HardWareStatusUtils.class);
            CPUTEMP = prop.getProperty("CPUTEMP");
            CPUUSED = prop.getProperty("CPUUSED");
            MEMUSED = prop.getProperty("MEMUSED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getCPUTemp() {
        return Double.parseDouble(CommandUtils.exeCmd(CPUTEMP))/1000;
    }

    public String getRAMinfo() {
        return CommandUtils.exeCmd(MEMUSED);
    }

    public String getCPUuse() {
        return CommandUtils.exeCmd(CPUUSED);
    }

}
