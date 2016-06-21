package de.haw.informatik.devsupport.wp1516.audime.network;

import android.bluetooth.BluetoothDevice;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by wp1516 on 20.11.15.
 */
public interface INetworkService {

    /**
     * Erstellt ein Serversocket und wartet auf eingehende Verbindungen
     * @return true wenn der Netzwerkservice verbunden ist
     * @throws IOException
     */
    boolean accept() throws IOException;

    /**
     * Versendet ein Objekt ueber den Bluetooth Kanal
     * @param obj
     * @throws IOException
     */
    void send(Object obj) throws IOException;

    /**
     * Blockt den aufrufenden Thread solange bis eine Nachricht erhalten wurde
     * versucht diese dann zu T zu casten und zurueckzugeben
     * @param returnClass
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    <T> T receive(Class<T> returnClass) throws IOException, ClassNotFoundException;

    /**
     * INetworkService erstellt eine Verbindung zu dem entfernten Geraet
     * @param device
     * @return
     * @throws IOException
     */
    boolean connect(NetworkHost device) throws IOException;

    /**
     * Schliesst die aktuelle Verbindung
     * @return
     */
    boolean disconnect();

}
