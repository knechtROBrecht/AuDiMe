package de.haw.informatik.devsupport.wp1516.audime.network;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by wp1516 on 20.11.15.
 */
public class WLANNetworkService implements INetworkService {

    private Socket socket;


    /**
     *
     *
     */
    public WLANNetworkService() throws IOException {
    }

    @Override
    public boolean accept() throws IOException {
        //TODO UUID überprüfen
        ServerSocket serverSocket = new ServerSocket(8080);
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
        NetworkAddress address = (NetworkAddress) device.getDevice();

        this.socket = new Socket(address.getHostname(), address.getPort());
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
}
