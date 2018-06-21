package me.noverish.snmp.packet.pdu;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BERSerializable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PDUVariableOID implements BERSerializable {
    public int[] value;

    public PDUVariableOID() {

    }

    public PDUVariableOID(String oid) {
        String[] numbers = oid.split("\\.");
        value = new int[numbers.length];

        for (int i = 0; i < numbers.length; i++)
            value[i] = Integer.parseInt(numbers[i]);
    }

    // BERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        BER.encodeOID(os, BER.OID, value);
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        value = BER.decodeOID(is, new BER.MutableByte());
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
