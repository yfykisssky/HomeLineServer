package com.homeline.servlet;

import com.homeline.data.TemKeyData;
import com.homeline.ws.WsPool;

import net.sf.json.JSONObject;

public class LoginOutServlet extends AesBasePostServlet{
	
	@Override
	void doPost(String postBody) {
		
		JSONObject jsonData=JSONObject.fromObject(postBody);
		
		String user=jsonData.getString("username");
		
		if(TemKeyData.getUserByName(user)!=null) {
			TemKeyData.removeFromList(user);
			WsPool.removeUser(WsPool.getWsByUser(user));
			isSuccess=true;
			msg="SUCCESS";
		}else {
			isSuccess=false;
			msg="USER_NOT_EXIST";
		}
		
		respData(jsonData.toString());
		
	}

}
