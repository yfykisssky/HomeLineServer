package com.homeline.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.homeline.tool.NetTool;
import net.sf.json.JSONObject;

public abstract class BaseServlet extends HttpServlet{

	abstract void doPost(Map parameterMap,String postBody);
	
	abstract void doGet(Map parameterMap);

	private HttpServletResponse resp;
	
	protected String msg = "";

	protected boolean isSuccess = false;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		this.resp = resp;

		String data = NetTool.ToStr(req.getInputStream());
		
		doPost(req.getParameterMap(),data);

	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		this.resp = resp;
		
		doGet(req.getParameterMap());
		
	}

	protected void respData(String data) {

		try {
			
			JSONObject jsonRes = JSONObject.fromObject(data);
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
