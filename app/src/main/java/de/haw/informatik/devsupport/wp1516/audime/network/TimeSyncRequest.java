package de.haw.informatik.devsupport.wp1516.audime.network;

import java.io.Serializable;

/**
 * Startnachricht fuer Zeitsynchronisation
 * Created by Tim on 22.11.2015.
 */
public class TimeSyncRequest implements Serializable{

    private int count;

    public TimeSyncRequest(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
