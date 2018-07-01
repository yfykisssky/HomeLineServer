package com.homeline.hardware.gpio;

import com.pi4j.wiringpi.Gpio;

public class SwitchUtils {

    private static SwitchUtils instance;

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
        Gpio.pinMode(pin, Gpio.OUTPUT);
        Gpio.digitalWrite(pin, Gpio.HIGH);
    }

    public void setOutV(int pin, int valueV) {
        Gpio.pinMode(pin, Gpio.OUTPUT);
        switch (valueV) {
            case 0:
                valueV = Gpio.ALT0;
                break;
            case 1:
                valueV = Gpio.ALT1;
                break;
            case 2:
                valueV = Gpio.ALT2;
                break;
            case 3:
                valueV = Gpio.ALT3;
                break;
            case 4:
                valueV = Gpio.ALT4;
                break;
            case 5:
                valueV = Gpio.ALT5;
                break;
        }
        Gpio.digitalWrite(pin, valueV);
    }

    public int getOutV(int pin) {
        int value = Gpio.digitalRead(pin);
        int valueV = 0;
        switch (value) {
            case Gpio.ALT0:
                valueV = 0;
                break;
            case Gpio.ALT1:
                valueV = 1;
                break;
            case Gpio.ALT2:
                valueV = 2;
                break;
            case Gpio.ALT3:
                valueV = 3;
                break;
            case Gpio.ALT4:
                valueV = 4;
                break;
            case Gpio.ALT5:
                valueV = 5;
                break;
        }
        return valueV;
    }

    public void off(int pin) {
        Gpio.pinMode(pin, Gpio.OUTPUT);
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
