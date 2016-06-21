package de.haw.informatik.devsupport.wp1516.audime;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.haw.informatik.devsupport.wp1516.audime.actorcommunication.AgentCommunicationService;
import de.haw.informatik.devsupport.wp1516.audime.controller.Controller;
import de.haw.informatik.devsupport.wp1516.audime.controller.SenderParams;
import de.haw.informatik.devsupport.wp1516.audime.gui.BluetoothArrayAdapter;
import de.haw.informatik.devsupport.wp1516.audime.network.BluetoothNetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.INetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkAddress;
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkHost;
import de.haw.informatik.devsupport.wp1516.audime.network.WLANNetworkService;

/**
 * Created by Tim on 22.11.2015.
 */
public class WLANSenderActivity extends SenderActivity  {
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            INetworkService networkService = new WLANNetworkService();
            this.controller = new Controller(networkService,  new AgentCommunicationService(getIntent().getExtras().getString("udphost"), getIntent().getExtras().getInt("udpport")), this.getIntent().getBooleanExtra("ptp", false));
        } catch (IOException e) {
            this.appendToConsole("Fehler: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        this.setContentView(R.layout.wlan_sender_layout);

    }

    public void startSender(View view){
        if (this.getCountText() < 1){
            Toast.makeText(this, "Bitte Anzahl der Synchronisationsrunden einstellen", Toast.LENGTH_SHORT).show();
        }
        else {
            this.controller.createSenderTask(this).execute(new SenderParams(this.receiverAddressDevices(), this.getCountText()));
        }

    }

    private List<NetworkHost> receiverAddressDevices(){
        ArrayList<NetworkHost> addresses = new ArrayList<NetworkHost>();
        TextView text = (TextView) this.findViewById(R.id.hostText);
        TextView textport = (TextView) this.findViewById(R.id.portText);
        if (text != null && textport != null){
            addresses.add(new NetworkHost(new NetworkAddress(text.getText().toString(), Integer.parseInt(textport.getText().toString()))));
        }
        return addresses;
    }

}
