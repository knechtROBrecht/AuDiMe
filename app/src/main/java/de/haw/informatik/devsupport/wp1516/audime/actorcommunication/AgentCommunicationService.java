package de.haw.informatik.devsupport.wp1516.audime.actorcommunication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by wp1516 on 08.01.16.
 */
public class AgentCommunicationService implements IAgentCommunicationService{

    private DatagramSocket socket;
    private String hostname;
    private int port;

    public AgentCommunicationService(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void sendMessage(String message) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("tpe",  "net.devsupport.audime.api.Result");
            obj.put("value", message);
            socket = new DatagramSocket();
            InetAddress IPAddress =  InetAddress.getByName(hostname);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            sendData =("{\"tpe\": \"net.devsupport.messaging.tunnel.api.Publish\", \"group\":\"AuDiMeGroup\", \"msg\": \"" + obj.toString()+ "\" }").getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            socket.send(sendPacket);
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
