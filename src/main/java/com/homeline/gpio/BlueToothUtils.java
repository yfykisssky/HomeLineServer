package com.homeline.gpio;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class BlueToothUtils {
	
	public interface GetBuleToothInfo{
		void onCompleted(Set<RemoteDevice> devicesDiscovered);
		//void onDiscovered();
	}
	
	private final static Set<RemoteDevice> devicesDiscovered = new HashSet<RemoteDevice>();

	public static void findDevices(GetBuleToothInfo interInfo) throws IOException, InterruptedException {

	    final Object inquiryCompletedEvent = new Object();

	    devicesDiscovered.clear();

	    DiscoveryListener listener = new DiscoveryListener() {
	        public void inquiryCompleted(int discType) {
	            synchronized (inquiryCompletedEvent) {
	                inquiryCompletedEvent.notifyAll();
	            }
	        }

	        @Override
	        public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
	        	
	            devicesDiscovered.add(remoteDevice);

	        }
	

	        @Override
	        public void serviceSearchCompleted(int arg0, int arg1) {
	        	interInfo.onCompleted(devicesDiscovered);
	        }

			@Override
			public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
				// TODO Auto-generated method stub
				
			}
	    };

	    synchronized (inquiryCompletedEvent) {

	        boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC,listener);

	        if (started) {
	            inquiryCompletedEvent.wait();
	            LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(listener);
	        }
	    }
	}
}
