package com.homeline.ws;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.homeline.data.TemKeyData;
import com.homeline.tool.AESHelper;

import net.sf.json.JSONObject;

@ServerEndpoint("/websocket")
public class WsServer extends BaseWsHandler{
	
	protected Session session;

	public WsServer() {
 
	}

	@OnOpen
	public void onOpen(Session session) throws IOException {
		
	}

	@OnMessage
	public void onMessage(String message,Session session) {
		
		handleData(message,this);
		
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
		System.out.println(postBody);
	}

}
