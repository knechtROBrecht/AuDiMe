package de.haw.informatik.devsupport.wp1516.audime.network;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import de.haw.informatik.devsupport.wp1516.audime.AuDiMeActivity;



/**
 * Created by wp1516 on 20.11.15.
 */
public class BluetoothNetworkService implements INetworkService {

    private final BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket socket;


    /**
     *
     *
     */
    public BluetoothNetworkService() throws IOException {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            throw new IOException("No Bluetooth adapter found");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            //TODO
            throw new IOException("Bluetooth not enabled");
        }
    }

    @Override
    public boolean accept() throws IOException {
        //TODO UUID überprüfen
        BluetoothServerSocket serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("AuDiMe", new UUID(1234,1234));
        this.socket = serverSocket.accept();
        serverSocket.close();
        return this.socket != null && this.socket.isConnected();
    }

    @Override
    public void send(Object obj) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
        oos.writeObject(obj);
        //explizit nicht schließen, weil wiederverwendbar
        //oos.close();
    }

    @Override
    public <T> T receive(Class<T> returnClass) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
        Object received = ois.readObject();
        if (returnClass.isAssignableFrom(received.getClass())){
            return (T) received;
        }
        else {
         throw new ClassCastException("Received Object cannot be castet to this Type");
        }
    }

    @Override
    /**
     * Für Client um Verbindungen aufzubauen
     */
    public boolean connect(NetworkHost device) throws IOException {
        BluetoothDevice btdevice = (BluetoothDevice) device.getDevice();
        this.socket = btdevice.createRfcommSocketToServiceRecord(new UUID(1234,1234));
        this.socket.connect();
        return this.socket != null && this.socket.isConnected();
    }

    @Override
    public boolean disconnect() {
        if (this.socket != null && socket.isConnected()){
            try {
                socket.close();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public static List<BluetoothDevice> getConnectedDevices(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            return new ArrayList<BluetoothDevice>();
        }
        ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>(mBluetoothAdapter.getBondedDevices());
        return devices;
    }
}
