package com.homeline.ws;

import com.homeline.hardware.gpio.SteeringEngineUtils;
import com.homeline.hardware.gpio.StepEngineUtils;
import net.sf.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WsServer extends BaseWsHandler {

    protected Session session;

    public WsServer() {

    }

    @OnOpen
    public void onOpen(Session session) {

    }

    @OnMessage
    public void onMessage(String message, Session session) {

        handleData(message, this);

    }

    @OnError
    public void onError(Throwable t) {
        WsPool.removeUser(this);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        WsPool.removeUser(this);
    }

    @Override
    void doData(String postBody) {
        handleData(postBody);
    }

    public void sendToData(String postBody) {
        super.sendData(postBody);
    }

    void handleData(String data) {
        JSONObject jsonObject = JSONObject.fromObject(data);
        String kind = jsonObject.getString("kind");
        String value = jsonObject.getString("value");
        switch (kind) {
            case "camera_up":
                int rangeUp = Integer.parseInt(value);
                rangeUp = SteeringEngineUtils.getInstance().getNowDegree() + rangeUp;
                SteeringEngineUtils.getInstance().toRange(rangeUp);
                break;
            case "camera_down":
                int rangeDown = Integer.parseInt(value);
                rangeDown = SteeringEngineUtils.getInstance().getNowDegree() - rangeDown;
                SteeringEngineUtils.getInstance().toRange(rangeDown);
                break;
            case "camera_left":
                int stepLeft = Integer.parseInt(value);
                StepEngineUtils.getInstance().toLeft(stepLeft);
                break;
            case "camera_right":
                int stepRight = Integer.parseInt(value);
                StepEngineUtils.getInstance().toRight(stepRight);
                break;
        }
    }

}
