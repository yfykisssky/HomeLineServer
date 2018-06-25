package com.homeline.runnable;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.bluetooth.RemoteDevice;
import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;

import com.homeline.bean.BlueToothInfo;
import com.homeline.gpio.BlueToothUtils;
import com.homeline.gpio.BlueToothUtils.GetBuleToothInfo;

import net.sf.json.JSONObject;

public class GetBlueToothRunnable implements Runnable {

	private AsyncContext asyncContext;

	public GetBlueToothRunnable(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
	}

	@Override
	public void run() {

		try {
			BlueToothUtils.findDevices(new GetBuleToothInfo() {

				@Override
				public void onCompleted(Set<RemoteDevice> devicesDiscovered) {

					try {

						List<BlueToothInfo> listData = new ArrayList<>();

						for (RemoteDevice value : devicesDiscovered) {
							String name = value.getFriendlyName(true);
							String address = value.getBluetoothAddress();
							BlueToothInfo blueToothInfo = new BlueToothInfo();
							blueToothInfo.setAddress(address);
							blueToothInfo.setName(name);
							listData.add(blueToothInfo);
						}

						ServletResponse resp = asyncContext.getResponse();
						resp.setCharacterEncoding("utf-8");
						PrintWriter out = resp.getWriter();
						out.write(JSONObject.fromObject(listData).toString());
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					asyncContext.complete();

				}

			});
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
