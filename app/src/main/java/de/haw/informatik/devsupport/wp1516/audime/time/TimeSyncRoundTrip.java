package de.haw.informatik.devsupport.wp1516.audime.time;

import java.io.Serializable;

/**
 * Netzwerkobjekt fuer die Kapselung einer Zeitsynchronisationsabfrage
 * Created by Tim on 22.11.2015.
 */
public class TimeSyncRoundTrip implements Serializable {

    private long sendTime;
    private long serverTime;

    public TimeSyncRoundTrip(){

    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
