package de.haw.informatik.devsupport.wp1516.audime;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import de.haw.informatik.devsupport.wp1516.audime.network.INetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkHost;
import de.haw.informatik.devsupport.wp1516.audime.network.OkResponse;
import de.haw.informatik.devsupport.wp1516.audime.network.StartRequest;
import de.haw.informatik.devsupport.wp1516.audime.network.TimeSyncRequest;
import de.haw.informatik.devsupport.wp1516.audime.time.TimeSyncRoundTrip;

/**
 * Created by Tim on 07.01.2016.
 */
public class NetworkServiceMock implements INetworkService {
    private TimeSyncRoundTrip nextObject;
    private Queue<Object> messages;

    public NetworkServiceMock(){
        this.messages = new LinkedList<Object>();
        this.messages.add(new StartRequest());
        this.messages.add(new TimeSyncRequest(100));
    }
    @Override
    public boolean accept() throws IOException {
        return true;
    }

    @Override
    public void send(Object obj) throws IOException {
        if (obj instanceof TimeSyncRoundTrip)
            this.nextObject = (TimeSyncRoundTrip)obj;
    }

    @Override
    public <T> T receive(Class<T> returnClass) throws IOException, ClassNotFoundException {
        if (nextObject != null) {
            nextObject.setServerTime(System.currentTimeMillis() + 50);
            Object obj = nextObject;
            this.nextObject = null;
            return (T) obj;
        }
        else {
            return (T)messages.poll();
        }
    }

    @Override
    public boolean connect(NetworkHost device) throws IOException {
        return true;
    }

    @Override
    public boolean disconnect() {
        this.nextObject = null;
        return true;
    }
}
