package de.haw.informatik.devsupport.wp1516.audime.controller;

import android.bluetooth.BluetoothDevice;

import java.io.IOException;
import java.util.List;

import de.haw.informatik.devsupport.wp1516.audime.ReceiverActivity;
import de.haw.informatik.devsupport.wp1516.audime.BluetoothSenderActivity;
import de.haw.informatik.devsupport.wp1516.audime.SenderActivity;
import de.haw.informatik.devsupport.wp1516.audime.actorcommunication.AgentCommunicationService;
import de.haw.informatik.devsupport.wp1516.audime.actorcommunication.IAgentCommunicationService;
import de.haw.informatik.devsupport.wp1516.audime.audio.AudioService;
import de.haw.informatik.devsupport.wp1516.audime.audio.IAudioService;
import de.haw.informatik.devsupport.wp1516.audime.network.BluetoothNetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.INetworkService;
import de.haw.informatik.devsupport.wp1516.audime.time.ITimeService;
import de.haw.informatik.devsupport.wp1516.audime.time.TimeService;

/**
 * Controller regelt den ablauf zwischen GUI und Fachlichen Komponenten
 * Created by Tim on 22.11.2015.
 */
public class Controller {

    private final INetworkService networkService;
    private final ITimeService timeService;

    private final IAgentCommunicationService agentCommunicationService;
    private final boolean ptp;
    private IAudioService audioService;

    public Controller(INetworkService service, IAgentCommunicationService agentService, boolean ptp) throws IOException {
        this.networkService = service;
        this.timeService = new TimeService();
        //TODO Audioservice initialisieren,
        this.audioService = new AudioService(this.timeService);
        this.agentCommunicationService = agentService;
        this.ptp = ptp;
    }

    /**
     * Erstellt einen Sendertask f�r die Activity
     * @param senderActivity
     * @return
     */
    public AsyncSenderTask createSenderTask(SenderActivity senderActivity){

        return new AsyncSenderTask(this.networkService, this.timeService, this.agentCommunicationService, senderActivity, audioService, ptp);
    }

    /**
     * Erstellt einen Receivertask f�r die Activity
     * @param receiverActivity
     * @return
     */
    public AsyncReceiverTask createReceiverTask(ReceiverActivity receiverActivity){
        return new AsyncReceiverTask(this.networkService, this.timeService, this.agentCommunicationService, receiverActivity, audioService, ptp);

    }

    /**
     * Gibt die Bluetoothdevices wieder, die zur Verf�gung stehen
     * @return
     */
    public List<BluetoothDevice> getBluetoothDevices(){
        return BluetoothNetworkService.getConnectedDevices();
    }

    public ITimeService getTimeService(){
        return this.timeService;
    }


}
