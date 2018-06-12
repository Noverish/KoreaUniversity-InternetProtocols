package me.noverish.snmp.packet.snmp;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;

import java.io.IOException;
import java.io.OutputStream;

import me.noverish.snmp.packet.pdu.PDUPacket;
import me.noverish.snmp.utils.CustomBERSerializable;

public class SNMPPacket implements CustomBERSerializable {
    public SNMPVersion version;
    public SNMPCommunity community;
    public PDUPacket pdu;

    public SNMPPacket() {

    }

    public SNMPPacket(SNMPVersion version, SNMPCommunity community, PDUPacket pdu) {
        this.version = version;
        this.community = community;
        this.pdu = pdu;
    }

    // CustomBERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        int payloadLength = getBERPayloadLength();

        BER.encodeHeader(os, BER.SEQUENCE, payloadLength);
        version.encodeBER(os);
        community.encodeBER(os);
        pdu.encodeBER(os);
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        BER.MutableByte mutableByte = new BER.MutableByte();
        int length = BER.decodeHeader(is, mutableByte);
        int startPos = (int) is.getPosition();

        BER.MutableByte versionType = new BER.MutableByte();
        int versionInt = BER.decodeInteger(is, versionType);
        version = SNMPVersion.parseValue(versionInt);

        BER.MutableByte communityType = new BER.MutableByte();
        String communityStr = new String(BER.decodeString(is, communityType));
        community = new SNMPCommunity(communityStr);

        pdu = new PDUPacket();
        pdu.decodeBER(is);
    }

    @Override
    public int getBERLength() {
        int payloadLength = getBERPayloadLength();
        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        int length = pdu.getBERLength();
        length += community.getBERLength();
        length += version.getBERLength();
        return length;
    }


    // toString
    @Override
    public String toString() {
        return "{\n" +
                "  \"version\": " + version.getValue() + "\n" +
                "  \"community\": " + community.value + "\n" +
                pdu.toString() +
                "}";
    }
}
