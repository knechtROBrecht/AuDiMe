package de.haw.informatik.devsupport.wp1516.audime.gui;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.haw.informatik.devsupport.wp1516.audime.R;

/**
 * Adapter fuer die Bluetoothliste in der ReceiverActivity
 * Created by wp1516 on 20.11.15.
 */
public class BluetoothArrayAdapter extends ArrayAdapter<BluetoothDevice> {



    public BluetoothArrayAdapter(Context context, List<BluetoothDevice> objects) {
        super(context, -1, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.bluetoothlist_layout, parent, false);

        CheckBox checkbox = (CheckBox) row.findViewById(R.id.checkboxbluetooth);
        BluetoothDevice item = this.getItem(position);
        checkbox.setText(item.getName());


        return row;
    }
}
