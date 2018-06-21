package me.noverish.snmp.utils;

import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BEROutputStream;

import me.noverish.snmp.net.UDPHelper;
import me.noverish.snmp.packet.snmp.SNMP;

import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class SNMPHelper {
    public static SNMP sendAndReceive(String host, int port, SNMP packet) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        SNMP receivedPacket = sendAndReceive(socket, host, port, packet);

        socket.close();

        return receivedPacket;
    }

    public static SNMP sendAndReceive(DatagramSocket socket, String host, int port, SNMP packet) throws IOException {
        BEROutputStream os = new BEROutputStream(ByteBuffer.allocate(packet.getBERLength()));
        packet.encodeBER(os);
        byte[] bytes = os.getBuffer().array();

//        ArrayUtil.print(bytes);
//        System.out.println(packet);

        byte[] receivedBytes = UDPHelper.sendAndReceive(socket, host, port, bytes);
//        ArrayUtil.print(receivedBytes);

        BERInputStream is = new BERInputStream(ByteBuffer.wrap(receivedBytes));
        SNMP receivedPacket = new SNMP();
        receivedPacket.decodeBER(is);

//        System.out.println(receivedPacket);

        return receivedPacket;
    }
}
