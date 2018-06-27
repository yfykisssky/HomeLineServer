package com.homeline.hardware.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class BaseGpioUtils {

    GpioController gpio = GpioFactory.getInstance();

    void shutdowm() {
        gpio.shutdown();
    }

}
