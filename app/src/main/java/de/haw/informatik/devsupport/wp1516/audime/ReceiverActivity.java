package de.haw.informatik.devsupport.wp1516.audime;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;

import de.haw.informatik.devsupport.wp1516.audime.actorcommunication.AgentCommunicationService;
import de.haw.informatik.devsupport.wp1516.audime.controller.Controller;
import de.haw.informatik.devsupport.wp1516.audime.gui.BluetoothArrayAdapter;
import de.haw.informatik.devsupport.wp1516.audime.network.BluetoothNetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.INetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkInterfaces;
import de.haw.informatik.devsupport.wp1516.audime.network.WLANNetworkService;

/**
 * Created by Tim on 22.11.2015.
 */
public class ReceiverActivity extends Activity {

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.receiver_layout);

        try {

            INetworkService networkService = null;
            if (NetworkInterfaces.BLUETOOTH.toString().equals(getIntent().getExtras().getString("networktype")))
                networkService = new BluetoothNetworkService();
            else{
                networkService = new WLANNetworkService();
                WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();
                //Deprecation ist ok, betrifft nur IPV6 der wifimgr unterst�tzt aber nur ipv4
                String ipAddress = Formatter.formatIpAddress(ip);
                TextView view = (TextView)findViewById(R.id.ipText);
                view.setText(ipAddress.toCharArray(), 0, ipAddress.length());
            }
            this.controller = new Controller(networkService, new AgentCommunicationService(getIntent().getExtras().getString("udphost"), getIntent().getExtras().getInt("udpport")),this.getIntent().getBooleanExtra("ptp", false));

        } catch (IOException e) {
            Toast.makeText(this, "Fehler: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();
        }

    }

    public void startReceiver(View view){
        this.controller.createReceiverTask(this).execute();
    }

    public void appendToConsole(String string){
        TextView text = (TextView)this.findViewById(R.id.editText2);
        string = string + System.getProperty("line.separator");
        text.append(string);
//        if (text.getText().length() > 1000);
//            text.setText(text.getText().toString().toCharArray(), 100, text.length()-100);

       // Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
