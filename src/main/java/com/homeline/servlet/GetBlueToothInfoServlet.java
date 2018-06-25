package com.homeline.servlet;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.homeline.runnable.GetBlueToothRunnable;

public class GetBlueToothInfoServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		AsyncContext asyncContext = req.startAsync();
		GetBlueToothRunnable runnable=new GetBlueToothRunnable(asyncContext);
		asyncContext.start(runnable);
		asyncContext.setTimeout(1000000);
		asyncContext.addListener(new AsyncListener() {

            @Override
            public void onTimeout(AsyncEvent arg0) throws IOException {
                System.out.println("onTimeout...");
            }

            @Override
            public void onStartAsync(AsyncEvent arg0) throws IOException {
                System.out.println("onStartAsync...");
            }

            @Override
            public void onError(AsyncEvent arg0) throws IOException {
                System.out.println("onError...");
            }

            @Override
            public void onComplete(AsyncEvent arg0) throws IOException {
                System.out.println("onComplete...");
            }
        });
	/*	resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.print(jsonData.toString());
		out.close();*/
	}
	
	
	
}
