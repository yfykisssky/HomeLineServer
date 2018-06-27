package com.homeline;

import com.homeline.hardware.BlueToothUtils;

import javax.bluetooth.RemoteDevice;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

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

				if(blueToothUtils.checkSame(remoteDevice)){
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
