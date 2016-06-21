package de.haw.informatik.devsupport.wp1516.audime.audio;

import java.io.Serializable;

public class SignalFound implements Serializable{


    private long time;

    public SignalFound(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
