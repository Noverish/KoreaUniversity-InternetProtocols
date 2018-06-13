package me.noverish.snmp.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPHelper {

    private static final int DEFAULT_RECEIVE_BUFFER_SIZE = 300;
    private static final int DEFAULT_TIMEOUT = 1000;

    public static byte[] simpleSendAndReceive(String host, int port, byte[] data) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        byte[] tmp = sendAndReceive(socket, host, port, data);

        socket.close();

        return tmp;
    }

    public static byte[] sendAndReceive(DatagramSocket socket, String host, int port, byte[] data) throws IOException {
        socket.setSoTimeout(DEFAULT_TIMEOUT);

        InetAddress ia = InetAddress.getByName(host);
        DatagramPacket dp = new DatagramPacket(data, data.length, ia, port);

        byte[] receiveData = new byte[500];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        while (true) {

            socket.send(dp);

            try {
                System.out.println("receive start");
                socket.receive(receivePacket);
                System.out.println("receive end");
                break;
            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
            }
        }

        return receivePacket.getData();
    }
}
