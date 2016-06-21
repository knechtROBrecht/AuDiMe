package de.haw.informatik.devsupport.wp1516.audime.controller;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.haw.informatik.devsupport.wp1516.audime.ReceiverActivity;
import de.haw.informatik.devsupport.wp1516.audime.actorcommunication.IAgentCommunicationService;
import de.haw.informatik.devsupport.wp1516.audime.audio.AudioMeasureRequest;
import de.haw.informatik.devsupport.wp1516.audime.audio.IAudioService;
import de.haw.informatik.devsupport.wp1516.audime.audio.SignalFound;
import de.haw.informatik.devsupport.wp1516.audime.audio.SignalPlayed;
import de.haw.informatik.devsupport.wp1516.audime.network.INetworkService;
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
 *
 * Taks der mit dem Startbutton f�r den Empf�nger ausgef�hrt wird
 * Created by Tim on 23.11.2015.
 */
public class AsyncReceiverTask extends AsyncTask<Void, String, String> {

    private final INetworkService networkService;
    private final ITimeService timeService;
    private final ReceiverActivity activity;
    private final boolean ptp;

    private final IAgentCommunicationService agentCommunicationService;
    private final IAudioService audioService;

    private List<Integer> offsets;


    public AsyncReceiverTask(INetworkService networkService, ITimeService timeService, IAgentCommunicationService agentCommunicationService, ReceiverActivity activity, IAudioService audioService, boolean ptp){
        this.networkService = networkService;
        this.timeService = timeService;
        this.activity = activity;
        this.agentCommunicationService = agentCommunicationService;
        this.ptp = ptp;
        this.audioService = audioService;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        if (activity != null){
            for (String s: values){
                this.activity.appendToConsole(s);
            }
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        this.actAsReceiver();
        return "finished";
    }

    /**
     * F�hrt die Aktionen des Receivers nacheinander aus
     */
    public void actAsReceiver(){

        try {
                this.publishProgress(new File("test").getAbsolutePath());
                this.timeService.reset();
                this.publishProgress("Waiting for Connection");
                boolean connected = this.networkService.accept();
                this.publishProgress("Connection established. Waiting for Start Message");
                this.waitForStartMessage();
                this.publishProgress("Waiting for Timesync");
                this.networkService.accept();
                this.publishProgress("Timesync starting...");
                if (ptp)
                    this.ptpTimeSyncReceiver();
                else
                    this.timeSyncReceiver();
                this.publishProgress("Timesync finished!");
                this.publishProgress("Time offset: " + (System.currentTimeMillis() - this.timeService.getTime()));
                this.networkService.accept();
                this.doAudioMeasuring();
                this.publishProgress("AudioMeasurement finished");
        } catch (IOException e) {
            this.publishProgress("Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            this.publishProgress("Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void doAudioMeasuring() throws IOException, ClassNotFoundException {
        this.networkService.receive(AudioMeasureRequest.class);
        this.audioService.startRecord();
        this.networkService.send(new OkResponse());
        this.networkService.disconnect();
        this.networkService.accept();
        this.networkService.receive(SignalPlayed.class);
        this.audioService.stopRecord();
        long time = this.audioService.getResult();
        this.publishProgress("Audio Signal found at timestamp: " + time);
        this.networkService.send(new SignalFound(time));
        this.networkService.disconnect();
    }

    /**
     * Blockiert bis zum erhalt eines StartRequests
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void waitForStartMessage() throws IOException, ClassNotFoundException {
        StartRequest request = null;
        while (request == null){
            request = this.networkService.receive(StartRequest.class);
        }
        this.networkService.send(new OkResponse());
        this.networkService.disconnect();
    }

    /**
     * F�hrt die Zeitsynchronisation aus, bekommt vom Sender die entsprechende Anzahl an Synchronisationsrunden
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void timeSyncReceiver() throws IOException, ClassNotFoundException {
        TimeSyncRequest syncRequest = null;
        while (syncRequest == null){
            syncRequest = this.networkService.receive(TimeSyncRequest.class);
        }
        int syncCount = syncRequest.getCount();
        for (int i = 0; i < syncCount; i++) {
            sendSyncTimeMessage();
        }
        this.networkService.disconnect();
    }

    private void ptpTimeSyncReceiver() throws IOException, ClassNotFoundException {
        TimeSyncRequest syncRequest = null;
        while (syncRequest == null){
            syncRequest = this.networkService.receive(TimeSyncRequest.class);
        }
        int syncCount = syncRequest.getCount();
        long offset = 0;
        for (int i = 0; i < syncCount; i++) {
            PTPTimeSync sync = this.networkService.receive(PTPTimeSync.class);
            long time = this.timeService.getTime();
            PTPFollowUp followUp = this.networkService.receive(PTPFollowUp.class);
            offset += (followUp.getTime() - time);
        }
        offset = Math.round(new Double(offset) / new Double(syncCount));
        long delaytime = 0;
        for (int i = 0; i < syncCount; i++) {
            this.networkService.send(new PTPDelayRequest());
            long time = this.timeService.getTime() + offset;
            PTPDelayResponse delayResponse = this.networkService.receive(PTPDelayResponse.class);
            delaytime += delayResponse.getTime() - time;
            //offset += (followUp.getTime() - time);
        }
        delaytime = Math.round((new Double(delaytime) / new Double(syncCount))/2);
        this.publishProgress("Sync ready with offset: " + offset + " and delaytime: " + delaytime);
        this.timeService.setOffset(offset + delaytime);
        this.networkService.disconnect();
    }

    /**
     * Versendet einen TimeSyncRoundTrip und wartet auf die Antwort.
     * Nach Erhalt der Antwort wird die Zeitsynchronisation im TimeService aufgerufen.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void sendSyncTimeMessage() throws IOException, ClassNotFoundException {
        TimeSyncRoundTrip roundTrip = new TimeSyncRoundTrip();
        roundTrip.setSendTime(this.timeService.getTime());
        this.networkService.send(roundTrip);
        roundTrip = this.networkService.receive(TimeSyncRoundTrip.class);
        this.timeService.syncTime(roundTrip);
    }
}
