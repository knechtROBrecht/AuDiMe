package de.haw.informatik.devsupport.wp1516.audime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import de.haw.informatik.devsupport.wp1516.audime.network.BluetoothNetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.NetworkInterfaces;

public class AuDiMeActivity extends Activity {

    private static Context context;
    private BluetoothNetworkService networkService;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuDiMeActivity.context = getApplicationContext();
        setContentView(R.layout.activity_au_di_me);
    }

    public void startSender(View view) {
        Intent intent = null;
        RadioButton button = (RadioButton) findViewById(R.id.bluetoothRadioButton);
        if (button.isChecked()){
            intent = new Intent(this, BluetoothSenderActivity.class);
        }
        else {
            intent = new Intent(this, WLANSenderActivity.class);
        }
        this.putExtras(intent);
        startActivity(intent);
    }

    private void putExtras(Intent intent){
        Bundle b = new Bundle();
        RadioButton button = (RadioButton) findViewById(R.id.bluetoothRadioButton);
        if (button.isChecked()){
            b.putString("networktype", NetworkInterfaces.BLUETOOTH.toString());
        }
        else {
            b.putString("networktype", NetworkInterfaces.WLAN.toString());
        }
        RadioButton buttonTime = (RadioButton) findViewById(R.id.ptpRadioButton);

        b.putBoolean("ptp", buttonTime.isChecked());

        TextView udpHost = (TextView) findViewById(R.id.udpAgentIp);
        b.putString("udphost", udpHost.getText().toString());
        TextView udpport = (TextView) findViewById(R.id.udpAgentPort);
        String udpportString = udpport.getText().toString().equals("") ? "12345" : udpport.getText().toString();
        b.putInt("udpport", Integer.parseInt(udpportString));
        intent.putExtras(b);
    }

    public void startReceiver(View view) {
        Intent intent = new Intent(this, ReceiverActivity.class);
        this.putExtras(intent);
        startActivity(intent);
    }

    public static Context getAppContext() {
        return AuDiMeActivity.context;
    }
}
