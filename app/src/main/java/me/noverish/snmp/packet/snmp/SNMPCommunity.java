package me.noverish.snmp.packet.snmp;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BERSerializable;

import java.io.IOException;
import java.io.OutputStream;

public class SNMPCommunity implements BERSerializable {
    public String value;

    public SNMPCommunity() {

    }

    public SNMPCommunity(String value) {
        this.value = value;
    }

    // BERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        BER.encodeString(os, BER.OCTETSTRING, value.getBytes());
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        value = new String(BER.decodeString(is, new BER.MutableByte()));
    }

    @Override
    public int getBERLength() {
        int payloadLength = getBERPayloadLength();
        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        return value.getBytes().length;
    }
}
