package com.homeline.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.homeline.data.UserDao;
import com.homeline.push.SMSUtils;

import net.sf.json.JSONObject;

public class SendCodeServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String username = req.getParameter("username");

		UserDao userDao;

		boolean isSuccess = false;

		JSONObject jsonData = new JSONObject();

		String msg = new String();

		try {

			userDao = new UserDao();

			if (userDao.checkUserExit(username)) {
				
				int result = SMSUtils.checkSMSOutTime(username);

				switch (result) {
				case 0:
					msg = SMSUtils.sendCode(username, (int) ((Math.random() * 9 + 1) * 100000));
					isSuccess = true;
					break;
				case 1:
					msg = "NO_RETRY";
					break;
				}

			} else {
				msg = "NO_USER";
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}

		jsonData.put("msg", msg);
		jsonData.put("flag", isSuccess);

		resp.setContentType("text/html;charset=utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.print(jsonData.toString());
		out.close();

	}

}
