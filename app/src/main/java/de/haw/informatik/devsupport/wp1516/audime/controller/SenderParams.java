package de.haw.informatik.devsupport.wp1516.audime.controller;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import de.haw.informatik.devsupport.wp1516.audime.network.NetworkHost;

/**
 * Kapsellung fuer die Parameter, die der AsyncSenderTask benoetigt
 * -Ausgewaehlte Bluetoothgeraete
 * -Anzahl Zeitsynchronisationen
 * Created by Tim on 22.11.2015.
 */
public class SenderParams {

    private List<NetworkHost> devices;
    private int countTimeSync;

    public int getCountTimeSync() {
        return countTimeSync;
    }

    public void setCountTimeSync(int countTimeSync) {
        this.countTimeSync = countTimeSync;
    }

    public List<NetworkHost> getDevices() {
        return devices;
    }

    public void setDevices(List<NetworkHost> devices) {
        this.devices = devices;
    }

    public SenderParams(List<NetworkHost> devices, int countTimeSync) {

        this.devices = devices;
        this.countTimeSync = countTimeSync;
    }
}
