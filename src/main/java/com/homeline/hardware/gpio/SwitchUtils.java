package com.homeline.hardware.gpio;

import com.homeline.hardware.BlueToothUtils;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;

public class SwitchUtils extends BaseGpioUtils {

    //private int pin;

    private static SwitchUtils instance;

   /* public SwitchUtils(int pin, boolean state) {
        this.pin = pin;
        int pinState;
        if (state) {
            pinState = Gpio.HIGH;
        } else {
            pinState = Gpio.LOW;
        }
        Gpio.digitalWrite(pin, pinState);
    }*/

    public static SwitchUtils getInstance() {

        if (instance == null) {
            synchronized (SwitchUtils.class) {
                if (instance == null) {
                    instance = new SwitchUtils();
                }
            }
        }

        return instance;
    }

    public void on(int pin) {
        Gpio.pinMode(pin,Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.HIGH);
    }

    public void off(int pin) {
        Gpio.pinMode(pin,Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.LOW);
    }

    public boolean isHigh(int pin) {
        int value = Gpio.digitalRead(pin);
        if (value == Gpio.HIGH) {
            return true;
        } else if (value == Gpio.LOW) {
            return false;
        }
        return false;
    }

}
