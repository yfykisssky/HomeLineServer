package com.homeline.ws;

import com.homeline.Debug;
import org.apache.http.util.TextUtils;

import com.homeline.data.TemKeyData;
import com.homeline.tool.AESHelper;

import net.sf.json.JSONObject;

public abstract class BaseWsHandler {

    abstract void doData(String postBody);

    private String aesKey;

    private WsServer ws;

    public BaseWsHandler() {

    }

    protected void handleData(String fromData, WsServer ws) {

        this.ws = ws;

        try {

            JSONObject object = JSONObject.fromObject(fromData);

            if (!Debug.debug) {

                String username = object.getString("username");
                String encryptdata = object.getString("encryptdata");

                aesKey = TemKeyData.getAesKey(username);
                String data = AESHelper.decryptByBase64(encryptdata, aesKey);

                object = JSONObject.fromObject(data);
                String token = object.getString("token");
                String temToken = TemKeyData.getTokenFromList(username);

                if (!TextUtils.isEmpty(temToken)) {
                    if (temToken.equals(token)) {
                        WsPool.addUser(username, ws);
                        doData(object.getString("data"));
                    }
                } else {
                    JSONObject jsonRes = new JSONObject();
                    jsonRes.put("tokenstate", false);
                    sendData(jsonRes.toString());
                }

            }else{
                doData(object.getString("data"));
            }

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
