package com.homeline;

import com.homeline.gpio.BlueToothUtils;

import javax.bluetooth.RemoteDevice;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

public class StartListener implements ServletContextListener{

	private BlueToothUtils blueToothUtils;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		initBlueTooth();
	}

	private void initBlueTooth() {

		blueToothUtils=BlueToothUtils.getInstance();

		blueToothUtils.searchDevices(new BlueToothUtils.BlueToothInter() {
			@Override
			public void onDataReceive(String data) {

			}

			@Override
			public void onError(String error) {

			}

			@Override
			public void onDiscovered(RemoteDevice remoteDevice) throws IOException {

				if(remoteDevice.getFriendlyName(true).equals("")
						&&remoteDevice.getBluetoothAddress().equals("")){
					blueToothUtils.connectAndOpen(remoteDevice);
				}

			}

			@Override
			public void onCompleted() {

			}

			@Override
			public void onServicesDiscovered() {

			}
		});

	}

}
