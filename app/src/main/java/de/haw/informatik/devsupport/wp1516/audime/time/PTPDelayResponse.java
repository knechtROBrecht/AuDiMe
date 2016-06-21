package de.haw.informatik.devsupport.wp1516.audime.time;

import java.io.Serializable;

/**
 * Created by Tim on 12.01.2016.
 */
public class PTPDelayResponse implements Serializable {
    private long time;

    public PTPDelayResponse(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
