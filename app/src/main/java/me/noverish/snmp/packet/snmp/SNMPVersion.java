package me.noverish.snmp.packet.snmp;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BERSerializable;

import java.io.IOException;
import java.io.OutputStream;

import me.noverish.snmp.snmp.utils.BERLengthUtil;

public enum SNMPVersion implements BERSerializable {
    v2c;

    public int getValue() {
        switch (this) {
            case v2c:
                return 0x01;
            default:
                throw new IllegalStateException("Unknown SNMP Version");
        }
    }

    public static SNMPVersion parse(int value) {
        for (SNMPVersion v : SNMPVersion.values())
            if (v.getValue() == value)
                return v;

        throw new IllegalStateException("Unknown SNMP Version Value");
    }

    // BERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        BER.encodeInteger(os, BER.INTEGER, getValue());
    }

    @Override
    public void decodeBER(BERInputStream inputStream) throws IOException {

    }

    @Override
    public int getBERLength() {
        int payloadLength = getBERPayloadLength();
        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        return BERLengthUtil.getLengthOfInteger(getValue());
    }
}
