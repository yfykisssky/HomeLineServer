package com.homeline.servlet;

import net.sf.json.JSONObject;

public class GetDataServlet extends AesBasePostServlet{

	@Override
	void doPost(String postBody) {
		// TODO Auto-generated method stub
		
		System.out.println(postBody);
		
		JSONObject jsonData=new JSONObject();
		
		jsonData.put("123456","cccccc");
		
		respData(jsonData.toString());
		
	}

}
