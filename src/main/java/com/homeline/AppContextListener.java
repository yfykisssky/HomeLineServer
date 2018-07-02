package com.homeline;

import com.homeline.hardware.BlueToothUtils;
import com.homeline.hardware.gpio.SteeringEngineUtils;
import com.homeline.tool.PropertiesUtils;
import com.pi4j.wiringpi.Gpio;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Properties;

public class AppContextListener implements ServletContextListener {

    private static Logger logger;

    static {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.LOG4J, AppContextListener.class);
            PropertyConfigurator.configure(prop);
            logger = Logger.getLogger(AppContextListener.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        initGpio();
        initControlPlatForm();
        initDataCenter();
    }

    private void initDataCenter() {
        DataCenterUtils.getInstance().startWsWorkThread();
    }

    private void initControlPlatForm() {
        SteeringEngineUtils.getInstance().start();
    }

    private void initGpio() {
       Gpio.wiringPiSetup();
    }

}
