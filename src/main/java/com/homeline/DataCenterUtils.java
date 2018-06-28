package com.homeline;

import com.homeline.bean.EnvironmentData;
import com.homeline.bean.HandWareStatusData;

public class DataCenterUtils {

    private static EnvironmentData environmentData = new EnvironmentData();

    public static EnvironmentData getEnvironmentData() {
        synchronized (environmentData) {
            return environmentData;
        }
    }

    public static void setEnvironmentData(EnvironmentData environmentData) {
        synchronized (environmentData) {
            DataCenterUtils.environmentData = environmentData;
        }
    }

    private static HandWareStatusData handWareStatusData = new HandWareStatusData();

    public static HandWareStatusData getHandWareStatusData() {
        synchronized (handWareStatusData) {
            return handWareStatusData;
        }
    }

    public static void setHandWareStatusData(HandWareStatusData handWareStatusData) {
        synchronized (handWareStatusData) {
            DataCenterUtils.handWareStatusData = handWareStatusData;
        }
    }
}
