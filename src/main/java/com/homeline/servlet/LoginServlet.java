package com.homeline.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.TextUtils;

import com.homeline.data.TemKeyData;
import com.homeline.data.UserDao;
import com.homeline.push.SMSUtils;
import com.homeline.tool.AESHelper;
import com.homeline.tool.MD5Helper;
import com.homeline.tool.NetTool;
import com.homeline.tool.RSAHelper;

import net.sf.json.JSONObject;

public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = req.getParameter("username");

		String data = NetTool.ToStr(req.getInputStream());

		String priKey = TemKeyData.getPriKey(username);

		String msg = new String();

		boolean isSuccess = false;

		JSONObject jsonData = new JSONObject();
		if (!TextUtils.isEmpty(priKey)) {
			try {

				data = RSAHelper.RSAUtils.dePriData(data, priKey);

				JSONObject jsonobject = JSONObject.fromObject(data);
				String pswd = jsonobject.getString("pswd");
				int verCode = jsonobject.getInt("verfycode");
				String pubKey = jsonobject.getString("pubkey");

				UserDao userDao = new UserDao();

				if (userDao.checkUserExit(username)) {
					
					int checkResult = userDao.checkPswd(username, pswd);

					if (checkResult == 0) {

						switch (SMSUtils.checkSMSCode(username, verCode)) {

						case 0:

							String aeskey = new String(AESHelper.toHex(AESHelper.initKey()));

							String tokenKey = username + pswd + String.valueOf(System.currentTimeMillis());

							tokenKey = MD5Helper.stringToMD5(tokenKey);

							TemKeyData.updateAesKey(username, aeskey);

							jsonData.put("aeskey", aeskey);

							jsonData.put("token", tokenKey);

							String enData = RSAHelper.RSAUtils.enPubData(jsonData.toString(), pubKey);

							jsonData.clear();

							jsonData.put("data", enData);

							isSuccess = true;

							msg = "SUCCESS";
							break;
						case 1:
							msg = "NO_VERCODE";
							break;
						case 2:
							msg = "OUTTIME_VERCODE";
							break;
						case 3:
							msg = "ERROR_VERCODE";
							break;

						}

					} else if (checkResult == 2) {
						msg = "PSWD_ERROR";
					}
				} else {
					msg = "NO_USER";
				}

			} catch (Exception e) {
				e.printStackTrace();
				msg = "ERROR:" + e.getMessage();
			}

		}

		jsonData.put("msg", msg);
		jsonData.put("flag", isSuccess);

		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.print(jsonData.toString());
		out.close();

		if (isSuccess) {
			TemKeyData.removePriFromList(username);
		} else {
			TemKeyData.removeFromList(username);
		}

	}

}
