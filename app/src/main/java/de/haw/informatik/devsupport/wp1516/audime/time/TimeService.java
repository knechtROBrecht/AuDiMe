package de.haw.informatik.devsupport.wp1516.audime.time;

/**
 * Created by Tim on 22.11.2015.
 */
public class TimeService implements ITimeService{
    private long offset;
    private int syncs;
    private double totaldeviation;

    public TimeService(){
        this.reset();
    }


    @Override
    public long getTime() {
        return System.currentTimeMillis() + offset;
    }

    @Override
    public void syncTime(TimeSyncRoundTrip roundTrip) {
        long time = this.getTime();
        long roundTripTime = time - roundTrip.getSendTime();
        long serverTime = roundTrip.getServerTime() - (roundTripTime/2);
        long calculatedoffset = serverTime - System.currentTimeMillis();
        //erst wenn eine bestimmte Anzahl an syncs erreicht ist, abweicher rauswerfen
        if (!(syncs > 10 && Math.abs(calculatedoffset - offset) > 10)){
            totaldeviation += calculatedoffset;
            this.offset = Math.round(totaldeviation/syncs);
            syncs++;
        }

    }

    @Override
    public void reset() {
        this.offset = 0;
        this.totaldeviation = 0;
        this.syncs = 0;
    }

    @Override
    public long getOffset() {
        return this.offset;
    }

    @Override
    public double getDeviation() {
        return this.totaldeviation;
    }

    @Override
    public void setOffset(long offset) {
        this.offset = offset;
    }
}
