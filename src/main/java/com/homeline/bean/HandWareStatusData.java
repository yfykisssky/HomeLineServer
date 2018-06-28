package com.homeline.bean;

import com.homeline.tool.CommandUtils;
import com.homeline.tool.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

public class HandWareStatusData {

    private static HandWareStatusData instance;
    private static String MEMUSED;
    private static String CPUUSED;
    private static String CPUTEMP;

    public static HandWareStatusData getInstance() {

        if (instance == null) {
            synchronized (HandWareStatusData.class) {
                if (instance == null) {
                    instance = new HandWareStatusData();
                }
            }
        }
        return instance;
    }

    public HandWareStatusData() {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.HARDWARE, HandWareStatusData.class);
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
