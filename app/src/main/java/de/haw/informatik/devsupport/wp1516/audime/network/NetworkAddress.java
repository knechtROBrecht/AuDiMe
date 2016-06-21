package de.haw.informatik.devsupport.wp1516.audime.network;

/**
 * Created by Tim on 07.01.2016.
 */
public class NetworkAddress {

    private String hostname;
    private int port;

    public NetworkAddress(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "NetworkAddress{" +
                "hostname='" + hostname + '\'' +
                ", port=" + port +
                '}';
    }
}
