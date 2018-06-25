package com.homeline.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.TextUtils;

import com.homeline.data.TemKeyData;
import com.homeline.data.UserDao;
import com.homeline.tool.RSAHelper;

import net.sf.json.JSONObject;

public class GetPubKeyServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = req.getParameter("username");

		String msg = new String();

		boolean isSuccess = false;

		JSONObject jsonData = new JSONObject();

		if (TextUtils.isEmpty(username)) {

			msg = "ERROR";

		} else {

			UserDao userDao;
			try {
				userDao = new UserDao();

				if (userDao.checkUserExit(username)) {

					String data = new String();

					try {

						Map<String, Object> genKey = RSAHelper.RSAUtils.genKeyPair();
						String publicKey = RSAHelper.RSAUtils.getPublicKey(genKey);
						String privateKey = RSAHelper.RSAUtils.getPrivateKey(genKey);

						TemKeyData.addToKeyList(username, privateKey);

						isSuccess = true;
						data = publicKey;
						msg = "SUCCESS";

					} catch (Exception e) {
						e.printStackTrace();
						msg = e.getMessage();
					}

					jsonData.put("data", data);

				} else {
					msg = "NO_USER";
				}
			} catch (SQLException | ClassNotFoundException e1) {
				e1.printStackTrace();
				msg = e1.getMessage();
			}

		}

		jsonData.put("flag", isSuccess);
		jsonData.put("msg", msg);

		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.print(jsonData.toString());
		out.close();
	}

}
