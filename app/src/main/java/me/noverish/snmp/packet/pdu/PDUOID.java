package me.noverish.snmp.packet.pdu;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import me.noverish.snmp.utils.CustomBERSerializable;

public class PDUOID implements CustomBERSerializable {

    public int[] value;

    public PDUOID() {

    }

    public PDUOID(String oid) {
        String[] numbers = oid.split("\\.");
        value = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++)
            value[i] = Integer.parseInt(numbers[i]);
    }

    // CustomBERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        BER.encodeOID(os, BER.OID, value);
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        BER.MutableByte type = new BER.MutableByte();
        value = BER.decodeOID(is, type);
    }

    @Override
    public int getBERLength() {
        int payloadLength = getBERPayloadLength();
        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        return BER.getOIDLength(value);
    }


    // toString
    @Override
    public String toString() {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i : value)
            tmp.add(String.valueOf(i));
        return String.join(".", tmp);
    }
}
