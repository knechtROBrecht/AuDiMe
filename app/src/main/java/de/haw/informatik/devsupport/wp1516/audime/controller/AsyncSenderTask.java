package de.haw.informatik.devsupport.wp1516.audime.controller;

import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import de.haw.informatik.devsupport.wp1516.audime.BluetoothSenderActivity;
import de.haw.informatik.devsupport.wp1516.audime.SenderActivity;
import de.haw.informatik.devsupport.wp1516.audime.actorcommunication.IAgentCommunicationService;
import de.haw.informatik.devsupport.wp1516.audime.audio.AudioMeasureRequest;
import de.haw.informatik.devsupport.wp1516.audime.audio.IAudioService;
import de.haw.informatik.devsupport.wp1516.audime.audio.SignalFound;
import de.haw.informatik.devsupport.wp1516.audime.audio.SignalPlayed;
import de.haw.informatik.devsupport.wp1516.audime.network.INetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkHost;
import de.haw.informatik.devsupport.wp1516.audime.network.OkResponse;
import de.haw.informatik.devsupport.wp1516.audime.network.StartRequest;
import de.haw.informatik.devsupport.wp1516.audime.network.TimeSyncRequest;
import de.haw.informatik.devsupport.wp1516.audime.time.ITimeService;
import de.haw.informatik.devsupport.wp1516.audime.time.PTPDelayRequest;
import de.haw.informatik.devsupport.wp1516.audime.time.PTPDelayResponse;
import de.haw.informatik.devsupport.wp1516.audime.time.PTPFollowUp;
import de.haw.informatik.devsupport.wp1516.audime.time.PTPTimeSync;
import de.haw.informatik.devsupport.wp1516.audime.time.TimeSyncRoundTrip;

/**
 * Created by Tim on 22.11.2015.
 */
public class AsyncSenderTask extends AsyncTask<SenderParams, String, String> {


    private final INetworkService networkService;
    private final SenderActivity senderActivity;
    private final IAgentCommunicationService agentCommunicationService;
    private final ITimeService timeService;
    private final boolean ptp;
    private final IAudioService audioService;


    public AsyncSenderTask(INetworkService networkService, ITimeService timeService, IAgentCommunicationService agentCommunicationService, SenderActivity senderActivity, IAudioService audioService, boolean ptp){
        this.networkService = networkService;
        this.senderActivity = senderActivity;
        this.timeService = timeService;
        this.agentCommunicationService  = agentCommunicationService;
        this.ptp = ptp;
        this.audioService = audioService;

    }

    @Override
    protected String doInBackground(SenderParams... params) {
        actAsSender(params[0].getDevices(), params[0].getCountTimeSync());
        return "finished";
    }

    /**
     * F�hrt die Schritte des Senders aus
     * @param list
     * @param count
     */
    private void actAsSender(List<NetworkHost> list, int count){
        try {
            this.timeService.reset();
            this.startReceiver(list);
            this.publishProgress("Start messages send to Receivers");
            if (ptp)
                this.ptpTimeSyncSender(list, count);
            else
                this.timeSyncSender(list, count);
            this.publishProgress("Time Sync with Receivers finished");
            this.sendAudioSignal(list);
        } catch (IOException e) {
            e.printStackTrace();
            this.publishProgress("Error: " + e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.publishProgress("Error: " + e.getLocalizedMessage());
        }
    }

    private void sendAudioSignal(List<NetworkHost> list) throws IOException, ClassNotFoundException {
        for (NetworkHost host: list){
            boolean connected = false;
            while (!connected){
                try {
                    this.networkService.connect(host);
                    connected = true;
                } catch (IOException ex){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            //this.networkService.connect(host);
            this.networkService.send(new AudioMeasureRequest());
            this.networkService.receive(OkResponse.class);
            this.networkService.disconnect();
        }
        this.audioService.playSignal();
        this.audioService.stopSignal();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (NetworkHost host: list){
            this.networkService.connect(host);
            this.networkService.send(new SignalPlayed());
            SignalFound found = this.networkService.receive(SignalFound.class);
            double distance = (found.getTime()  - this.audioService.getSignalPlayedTimestamp()) * 0.343;
            this.publishProgress("Host: " + host + " Entfernung: " + distance);
            //Fancy agent Communication
            this.agentCommunicationService.sendMessage("Host: " + host + " Entfernung: " + distance);
            this.networkService.disconnect();
        }

    }

    /**
     * F�hrt f�r alle ausgew�hlten Bluetoothdevices die Zeitsynchronisation durch
     * @param list
     * @param count
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void timeSyncSender(List<NetworkHost> list, int count) throws IOException, ClassNotFoundException {
        for (NetworkHost device : list){
            this.publishProgress("Syncing Time with: " + device);
            this.networkService.connect(device);
            this.networkService.send(new TimeSyncRequest(count));
            for (int i = 0; i < count; i++){
                TimeSyncRoundTrip roundTrip = this.networkService.receive(TimeSyncRoundTrip.class);
                roundTrip.setServerTime(this.timeService.getTime());
                this.networkService.send(roundTrip);
            }
            this.networkService.disconnect();
        }
    }

    private void ptpTimeSyncSender(List<NetworkHost> list, int count) throws IOException, ClassNotFoundException {
        for (NetworkHost device : list){
            this.publishProgress("PTPSyncing Time with: " + device);
            boolean connected = false;
            while (!connected){
                try {
                    this.networkService.connect(device);
                    connected = true;
                } catch (IOException ex){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
            this.networkService.send(new TimeSyncRequest(count));
            for (int i = 0; i < count; i++){
                long time = this.timeService.getTime();
                this.networkService.send(new PTPTimeSync(time));
                this.networkService.send(new PTPFollowUp(time));
            }
            for (int i = 0; i < count; i++){
                this.networkService.receive(PTPDelayRequest.class);
                long time = this.timeService.getTime();
                this.networkService.send(new PTPDelayResponse(time));
            }
            this.networkService.disconnect();
        }
    }

    /**
     * Sendet an alle ausgew�hlten Devices einen StartRequest und wartet auf die Antwort
     * @param list
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void startReceiver(List<NetworkHost> list) throws IOException, ClassNotFoundException {
        for (NetworkHost device : list){
            this.publishProgress("Send Start Message To: " + device);
            boolean connect = this.networkService.connect(device);
            if (connect){
                this.publishProgress("Connected To: " + device);
            }
            this.networkService.send(new StartRequest());
            this.networkService.receive(OkResponse.class);
            this.networkService.disconnect();
        }

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        for (String s: values){
            this.senderActivity.appendToConsole(s);
        }
    }
}
