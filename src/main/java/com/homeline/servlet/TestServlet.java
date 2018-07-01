package com.homeline.servlet;

import com.homeline.hardware.gpio.StepEngineUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //String pin = req.getParameter("pin");
        String sw = req.getParameter("switch");
        String range = req.getParameter("range");

        String respStr = "succesas";
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        StepEngineUtils stepEngineUtils=StepEngineUtils.getInstance();

        if (sw.equals("on")) {
            stepEngineUtils.toLeft(Integer.parseInt(range));
        } else {
            stepEngineUtils.toRight(Integer.parseInt(range));
        }

       /* SwitchUtils s = SwitchUtils.getInstance();

        s.on(Integer.parseInt(pin));

        if (sw.equals("on")) {
            s.on(Integer.parseInt(pin));
        } else {
            s.off(Integer.parseInt(pin));
        }
        AppContextListener.toRange(Integer.parseInt(range));*/

        //HandWareStatusUtils h = HandWareStatusUtils.getInstance();

        //respStr = h.getCPUTemp() + h.getCPUuse() + h.getRAMinfo();

        out.print(respStr);
        out.flush();
        out.close();
    }

}
