package com.homeline.ws;

import org.apache.http.util.TextUtils;

import com.homeline.data.TemKeyData;
import com.homeline.tool.AESHelper;

import net.sf.json.JSONObject;

public abstract class BaseWsHandler {

    abstract void doData(String postBody);

    private String aesKey = new String();

    private WsServer ws;

    public BaseWsHandler() {

    }

    protected void handleData(String fromData, WsServer ws) {

        this.ws = ws;

        try {

            JSONObject object = JSONObject.fromObject(fromData);

            String username = object.getString("username");

            String encryptdata = object.getString("encryptdata");

            String data = WsPool.handleAesData(username, encryptdata);

            object = JSONObject.fromObject(data);

            String token = object.getString("token");

            String temToken = TemKeyData.getTokenFromList(username);

            if (!TextUtils.isEmpty(temToken)) {
                if (temToken.equals(token)) {
                    WsPool.addUser(username, ws);
                    doData(data);
                }
            }

            JSONObject jsonRes = new JSONObject();
            jsonRes.put("tokenstate", false);
            sendData(jsonRes.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void sendData(String data) {

        try {
            JSONObject jsonRes = JSONObject.fromObject(data);
            jsonRes.put("tokenstate", true);
            data = jsonRes.toString();
            data = AESHelper.encryptByBase64(data, aesKey);
            ws.sendData(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
