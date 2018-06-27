package com.homeline.hardware;

import com.homeline.tool.PropertiesUtils;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.Properties;

public class BlueToothUtils {

    private String NAME;
    private String ADDRESS;
    private String RETRYTIME;
    private String errorMessage;
    private String status;
    private static BlueToothUtils instance;
    private OutputStream outputStream;
    private InputStream inputStream;
    private BlueToothInter blueToothInter;
    private StreamConnection streamConnection;
    private boolean isRun;
    private ReadRunnable readRunnable;

    public interface BlueToothInter {

        void onDataReceive(String data);

        void onError(String error);

        void onDiscovered(RemoteDevice remoteDevice) throws IOException;

        void onCompleted();

        void onServicesDiscovered();

    }

    public BlueToothUtils() {
        try {
            Properties prop = PropertiesUtils.loadProperty(PropertiesUtils.BLUETOOTH, this);
            NAME = prop.getProperty("NAME");
            ADDRESS = prop.getProperty("ADDRESS");
            RETRYTIME = prop.getProperty("RETRYTIME");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BlueToothUtils getInstance() {

        if (instance == null) {
            synchronized (BlueToothUtils.class) {
                if (instance == null) {
                    instance = new BlueToothUtils();
                }
            }
        }

        return instance;

    }

    private class ReadRunnable implements Runnable {

        @Override
        public void run() {

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                while (isRun) {
                    if (reader.ready()) {
                        String data = reader.readLine();
                        blueToothInter.onDataReceive(data);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                blueToothInter.onError(e.getMessage());
            }

            closeAll();

        }

    }

    private void closeAll() {

        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (streamConnection != null) {
                streamConnection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean checkSame(RemoteDevice remoteDevice) throws IOException {
        if(remoteDevice.getFriendlyName(true).equals(NAME)
                &&remoteDevice.getBluetoothAddress().equals(ADDRESS)){
           return true;
        }else{
            return false;
        }
    }

    public void closeConnect() {
        isRun = false;
    }

    public void startReadData() {
        if (readRunnable == null) {
            readRunnable = new ReadRunnable();
            new Thread(readRunnable).start();
        }
    }

    public void sendData(String data) {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        try {
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeAll();
        }
    }

    public void connectAndOpen(RemoteDevice remoteDevice) throws IOException {
        streamConnection = (StreamConnection) Connector.open("btspp://" + remoteDevice.getBluetoothAddress() + ":1");
        inputStream = streamConnection.openDataInputStream();
        outputStream = streamConnection.openOutputStream();
        startReadData();
    }

    public void searchDevices(BlueToothInter blueToothInter) {

        this.blueToothInter = blueToothInter;

        final Object inquiryCompletedEvent = new Object();

        DiscoveryListener listener = new DiscoveryListener() {

            public void inquiryCompleted(int discType) {
                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notifyAll();
                }
            }

            @Override
            public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
                try {
                    blueToothInter.onDiscovered(remoteDevice);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void serviceSearchCompleted(int arg0, int arg1) {
                blueToothInter.onCompleted();
            }

            @Override
            public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
                blueToothInter.onServicesDiscovered();
            }

        };

        synchronized (inquiryCompletedEvent) {

            boolean started = false;

            try {
                started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
                if (started) {
                    inquiryCompletedEvent.wait();
                    LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(listener);
                }
            } catch (BluetoothStateException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
