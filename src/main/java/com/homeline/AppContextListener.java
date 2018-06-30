package com.homeline;

import com.homeline.hardware.BlueToothUtils;
import com.homeline.tool.PropertiesUtils;
import com.pi4j.wiringpi.Gpio;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

public class AppContextListener implements ServletContextListener {

    //private BlueToothUtils blueToothUtils;

    private static Logger logger;

    static{
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
        logger.error("error test");
        logger.debug("debug test");
        initBlueTooth();
        initGpio();
    }

    private void initGpio() {
        Gpio.wiringPiSetupSys();
    }

    private void initBlueTooth() {
/*
        blueToothUtils = BlueToothUtils.getInstance();

        blueToothUtils.searchDevices(new BlueToothUtils.BlueToothInter() {
            @Override
            public void onDataReceive(String data) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onDiscovered(RemoteDevice remoteDevice) throws IOException {

                if (blueToothUtils.checkSame(remoteDevice)) {
                    blueToothUtils.connectAndOpen(remoteDevice);
                }

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onServicesDiscovered() {

            }
        });*/

    }

}
