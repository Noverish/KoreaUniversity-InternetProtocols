package me.noverish.snmp.utils;

import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BEROutputStream;

import me.noverish.snmp.net.UDPHelper;
import me.noverish.snmp.packet.snmp.SNMPPacket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class SNMPHelper {
    public static SNMPPacket sendAndReceive(String host, int port, SNMPPacket packet) throws IOException {
        BEROutputStream os = new BEROutputStream(ByteBuffer.allocate(packet.getBERLength()));
        packet.encodeBER(os);
        byte[] bytes = os.getBuffer().array();

        ArrayUtil.print(bytes);
        System.out.println(packet);

        byte[] receivedBytes = UDPHelper.sendAndReceive(host, port, bytes);
        ArrayUtil.print(receivedBytes);

        BERInputStream is = new BERInputStream(ByteBuffer.wrap(receivedBytes));
        SNMPPacket receivedPacket = new SNMPPacket();
        receivedPacket.decodeBER(is);

        System.out.println(receivedPacket);

        return receivedPacket;
    }

    public static SNMPPacket sendAndReceive(DatagramSocket socket, String host, int port, SNMPPacket packet) throws IOException {
        BEROutputStream os = new BEROutputStream(ByteBuffer.allocate(packet.getBERLength()));
        packet.encodeBER(os);
        byte[] bytes = os.getBuffer().array();

        ArrayUtil.print(bytes);
        System.out.println(packet);

        byte[] receivedBytes = UDPHelper.sendAndReceive(socket, host, port, bytes);
        ArrayUtil.print(receivedBytes);

        BERInputStream is = new BERInputStream(ByteBuffer.wrap(receivedBytes));
        SNMPPacket receivedPacket = new SNMPPacket();
        receivedPacket.decodeBER(is);

        System.out.println(receivedPacket);

        return receivedPacket;
    }
}
