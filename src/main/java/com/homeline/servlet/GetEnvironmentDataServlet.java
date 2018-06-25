package com.homeline.servlet;

import com.homeline.bean.EnvironmentData;
import com.homeline.data.TemKeyData;
import com.homeline.ws.WsPool;

import net.sf.json.JSONObject;

public class GetEnvironmentDataServlet extends AesBasePostServlet{
	
	@Override
	void doPost(String postBody) {
		
		EnvironmentData enData=new EnvironmentData();
		
		enData.setDegrees(1.290);
		
		enData.setRelativeHumidity(1.1);
		
		respData(JSONObject.fromObject(enData).toString());
		
	}

}
