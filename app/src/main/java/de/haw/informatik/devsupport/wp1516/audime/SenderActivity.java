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

import de.haw.informatik.devsupport.wp1516.audime.controller.Controller;
import de.haw.informatik.devsupport.wp1516.audime.controller.SenderParams;
import de.haw.informatik.devsupport.wp1516.audime.gui.BluetoothArrayAdapter;
import de.haw.informatik.devsupport.wp1516.audime.network.BluetoothNetworkService;
import de.haw.informatik.devsupport.wp1516.audime.network.INetworkService;

/**
 * Created by Tim on 22.11.2015.
 */
public class SenderActivity extends Activity {

    protected int getCountText() {
        TextView view = (TextView) findViewById(R.id.numbertext);
        return Integer.parseInt(view.getText().toString());
    }
    /**
     * Schreibt einen String in das Multilinefeld
     * @param string
     */
    public void appendToConsole(String string){
        TextView text = (TextView)this.findViewById(R.id.editText);
        string = string + System.getProperty("line.separator");
        text.append(string);
//        if (text.getText().length() > 1000);
//            text.setText(text.getText().toString().toCharArray(), 100, text.length()-100);
        //Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }


}
