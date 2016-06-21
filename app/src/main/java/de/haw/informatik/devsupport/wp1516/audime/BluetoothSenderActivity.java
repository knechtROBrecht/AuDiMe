package de.haw.informatik.devsupport.wp1516.audime;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
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
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkHost;
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkInterfaces;

/**
 * Created by Tim on 22.11.2015.
 */
public class BluetoothSenderActivity extends SenderActivity implements AdapterView.OnItemClickListener {
    private Controller controller;
    private BluetoothArrayAdapter arrayAdapter;
    private List<BluetoothDevice> selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.selected = new ArrayList<BluetoothDevice>();
        try {
            INetworkService networkService = new BluetoothNetworkService();
            this.controller = new Controller(networkService,  new AgentCommunicationService(getIntent().getExtras().getString("udphost"), getIntent().getExtras().getInt("udpport")), this.getIntent().getBooleanExtra("ptp", false));

            this.setContentView(R.layout.sender_layout);
            ListView lv = (ListView) findViewById(R.id.listview);

            //    //TODO bessere Anzeige
            arrayAdapter = new BluetoothArrayAdapter(
                    this,
                    controller.getBluetoothDevices());

            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(this);
        } catch (IOException e) {
            //this.appendToConsole("Fehler: " + e.getLocalizedMessage());
            Toast.makeText(this, "Fehler: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }


    }

    public void startSender(View view){
        if (this.getcheckedDevices().size() < 1){
            Toast.makeText(this, "Bitte mindestens ein Bluetooth device ausw�hlen", Toast.LENGTH_SHORT);
        }
        else if (this.getCountText() < 1){
            Toast.makeText(this, "Bitte Anzahl der Synchronisationsrunden einstellen", Toast.LENGTH_SHORT).show();
        }
        else {
            this.controller.createSenderTask(this).execute(new SenderParams(this.getcheckedDevices(), this.getCountText()));
        }

    }

    private List<NetworkHost> getcheckedDevices() {
        ArrayList<NetworkHost> addresses = new ArrayList<NetworkHost>();
        for (BluetoothDevice device : this.selected){
           addresses.add(new NetworkHost(device));
        }
        return addresses;
    }


    @Override
    /**
     * Speichert die Selektierten Bluetoothdevices
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, ,Toast.LENGTH_SHORT).show();
        LinearLayout layout = (LinearLayout) view;
        CheckBox box = (CheckBox)layout.getChildAt(0);
        box.setChecked(!box.isChecked());
        BluetoothDevice device = this.arrayAdapter.getItem(position);

        if (!box.isChecked()){
            this.selected.remove(device);
        }
        else {
            this.selected.add(device);
        }

    }

}
