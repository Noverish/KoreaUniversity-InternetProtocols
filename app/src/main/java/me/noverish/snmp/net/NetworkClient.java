package me.noverish.snmp.net;

import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BEROutputStream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

import me.noverish.snmp.packet.snmp.SNMP;

public class NetworkClient {

    private static final int DEFAULT_RECEIVE_BUFFER_SIZE = 300;
    private static final int DEFAULT_TIMEOUT = 1000;

    public static SNMP sendSNMP(String host, int port, SNMP packet) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        SNMP receivedPacket = sendSNMP(socket, host, port, packet);

        socket.close();

        return receivedPacket;
    }

    public static SNMP sendSNMP(DatagramSocket socket, String host, int port, SNMP packet) throws IOException {
        BEROutputStream os = new BEROutputStream(ByteBuffer.allocate(packet.getBERLength()));
        packet.encodeBER(os);
        byte[] bytes = os.getBuffer().array();

        byte[] receivedBytes = NetworkClient.sendUDP(socket, host, port, bytes);

        BERInputStream is = new BERInputStream(ByteBuffer.wrap(receivedBytes));
        SNMP receivedPacket = new SNMP();
        receivedPacket.decodeBER(is);

        return receivedPacket;
    }

    private static byte[] sendUDP(DatagramSocket socket, String host, int port, byte[] data) throws IOException {
        socket.setSoTimeout(DEFAULT_TIMEOUT);

        InetAddress ia = InetAddress.getByName(host);
        DatagramPacket dp = new DatagramPacket(data, data.length, ia, port);

        byte[] receiveData = new byte[DEFAULT_RECEIVE_BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        while (true) {
            socket.send(dp);

            try {
                socket.receive(receivePacket);
                return receivePacket.getData();
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }
}
