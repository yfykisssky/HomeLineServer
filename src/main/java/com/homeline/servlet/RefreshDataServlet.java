package com.homeline.servlet;

import com.homeline.DataCenterUtils;

public class RefreshDataServlet extends AesBasePostServlet{

	@Override
	void doPost(String postBody) {
		
		respData(DataCenterUtils.getInstance().getAllDataToJsonString());
		
	}

}
