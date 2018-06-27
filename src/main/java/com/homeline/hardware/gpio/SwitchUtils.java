package com.homeline.hardware.gpio;

import com.pi4j.io.gpio.*;

public class SwitchUtils extends BaseGpioUtils {

    private GpioPinDigitalOutput switchs;

    SwitchUtils(Pin pin, String name, boolean state) {
        PinState pinState;
        if (state) {
            pinState = PinState.HIGH;
        } else {
            pinState = PinState.LOW;
        }
        switchs = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "", pinState);
    }

    void on() {
        switchs.high();
    }

    void off() {
        switchs.low();
    }

}
