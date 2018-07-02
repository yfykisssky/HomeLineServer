package com.homeline;

import com.homeline.bean.EnvironmentData;
import com.homeline.bean.HardWareStatusData;
import com.homeline.ws.WsPool;
import com.homeline.ws.WsServer;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataCenterUtils {

    private static DataCenterUtils instance;
    private EnvironmentData environmentData = new EnvironmentData();
    private HardWareStatusData hardWareStatusData = new HardWareStatusData();
    private List<String> userNames = new ArrayList<>();
    private boolean isRun;

    public EnvironmentData getEnvironmentData() {
        return environmentData;
    }

    public HardWareStatusData getHardWareStatusData() {
        return hardWareStatusData;
    }

    public String getAllDataToJsonString() {
        JSONObject jsonData = new JSONObject();
        jsonData.put("environment", JSONObject.fromObject(environmentData));
        jsonData.put("hardWareStatus", JSONObject.fromObject(hardWareStatusData));
        return jsonData.toString();
    }

    public static DataCenterUtils getInstance() {

        if (instance == null) {
            synchronized (DataCenterUtils.class) {
                if (instance == null) {
                    instance = new DataCenterUtils();
                }
            }
        }

        return instance;
    }

    public void startWsWorkThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    sendToWsUsers();

                }
            }
        }).start();

    }

    private void stopWsWorkThread() {
        isRun = false;
    }

    private void sendToWsUsers() {
        synchronized (userNames) {
            for (int x = 0; x < userNames.size(); x++) {
                WsServer wsServer = WsPool.getWsByUser(userNames.get(x));
                if (wsServer != null) {
                    wsServer.sendToData("");
                }
            }
        }
    }

    public void sendWsUserAdd(String userName) {
        synchronized (userNames) {
            for (int d = 0; d < userNames.size(); d++) {
                if (userNames.get(d).equals(userName)) {
                    return;
                }
            }
            userNames.add(userName);
        }
    }

    public void sendWsUserRemove(String userName) {
        synchronized (userNames) {
            userNames.remove(userName);
        }
    }

    public void handleRFData(String data) {

        String kind = data.substring(0, data.indexOf(":"));
        String value = data.substring(data.indexOf(":"), data.length());

        synchronized (environmentData) {
            switch (kind) {
                case "":
                    environmentData.setRelativeHumidity(Double.parseDouble(value));
                    break;
            }
        }

    }

    public void handleHardWareStatusData() {

    }

}
