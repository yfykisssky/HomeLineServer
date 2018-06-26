package com.homeline.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GpioUtils {
	
	public void test() throws InterruptedException {
		  //获取全局Gpio 引脚控制器对象
	    final GpioController gpio = GpioFactory.getInstance();
  
	    //定义编号为0的引脚为数字输出引脚，初始化为低电平
        GpioPinDigitalOutput myLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "" ,PinState.LOW);
        
        while(true)
        {
            
            myLed.high();
            Thread.sleep(500);
            myLed.low();
            Thread.sleep(500);
            
        } 
	}

}
