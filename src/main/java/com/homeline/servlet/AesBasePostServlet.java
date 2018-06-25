package com.homeline.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.TextUtils;

import com.homeline.Debug;
import com.homeline.data.TemKeyData;
import com.homeline.tool.AESHelper;
import com.homeline.tool.NetTool;

import net.sf.json.JSONObject;

public abstract class AesBasePostServlet extends HttpServlet {

	abstract void doPost(String postBody);

	private String aesKey;

	private HttpServletResponse resp;
	
	protected String msg = new String();

	protected boolean isSuccess = false;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		this.resp = resp;

		String username = req.getParameter("username");

		String data = NetTool.ToStr(req.getInputStream());

		try {

			if (!Debug.debug) {

				aesKey = TemKeyData.getAesKey(username);

				data = AESHelper.decryptByBase64(data, aesKey);
				
				JSONObject json = JSONObject.fromObject(data);

				String token = json.getString("token");

				String temToken = TemKeyData.getTokenFromList(username);

				if (!TextUtils.isEmpty(temToken)) {
					if (temToken.equals(token)) {
						doPost(json.getString("data"));
					}
				}else {
					TemKeyData.removeFromList(username);
					JSONObject jsonRes = new JSONObject();
					jsonRes.put("tokenstate", false);
					respData(jsonRes.toString());
				}

			}else {
				JSONObject json = JSONObject.fromObject(data);
				doPost(json.getString("data"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess=false;
			msg=e.getMessage();
			respData("");
		}

	}

	protected void respData(String data) {

		try {

			JSONObject jsonRes = JSONObject.fromObject(data);
			jsonRes.put("tokenstate", true);
			data = jsonRes.toString();

			if (!Debug.debug) {

				data = AESHelper.encryptByBase64(data, aesKey);

			}

			jsonRes.put("flag", isSuccess);
			jsonRes.put("msg", msg);
			resp.setContentType("text/html;charset=utf-8");
			resp.setCharacterEncoding("utf-8");
			PrintWriter out = resp.getWriter();
			out.print(data);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
