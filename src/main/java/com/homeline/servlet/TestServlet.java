package com.homeline.servlet;

import com.homeline.hardware.HandWareStatusUtils;
import com.homeline.hardware.gpio.SwitchUtils;
import com.pi4j.io.gpio.RaspiPin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String pin = req.getParameter("pin");
        String sw = req.getParameter("switch");

        String respStr = "";
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        SwitchUtils s = SwitchUtils.getInstance();

        s.on(Integer.parseInt(pin));

        if (sw.equals("on")) {
            s.on(Integer.parseInt(pin));
        } else {
            s.off(Integer.parseInt(pin));
        }

        HandWareStatusUtils h = HandWareStatusUtils.getInstance();

        respStr = h.getCPUTemp() + h.getCPUuse() + h.getRAMinfo();

        out.print(respStr);
        out.flush();
        out.close();
    }

}
