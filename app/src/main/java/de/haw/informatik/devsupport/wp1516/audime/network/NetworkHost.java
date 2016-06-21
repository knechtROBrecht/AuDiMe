package de.haw.informatik.devsupport.wp1516.audime.network;

/**
 * Created by Tim on 07.01.2016.
 */
public class NetworkHost {

    private Object device;

    public  NetworkHost(Object device){
        this.device = device;
    }

    public Object getDevice() {
        return device;
    }

    public void setDevice(Object device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "NetworkHost{" +
                "device=" + device +
                '}';
    }
}
