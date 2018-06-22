package me.noverish.snmp.packet;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BERSerializable;

import java.io.IOException;
import java.io.OutputStream;

public class PDUVariable implements BERSerializable {
    public PDUVariableOID oid;
    public PDUVariableValue value;

    public PDUVariable() {

    }

    public PDUVariable(PDUVariableOID oid, PDUVariableValue value) {
        this.oid = oid;
        this.value = value;
    }

    // BERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        BER.encodeHeader(os, BER.SEQUENCE, getBERPayloadLength());
        oid.encodeBER(os);
        value.encodeBER(os);
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        BER.decodeHeader(is, new BER.MutableByte());

        oid = new PDUVariableOID();
        oid.decodeBER(is);

        value = new PDUVariableValue();
        value.decodeBER(is);
    }

    @Override
    public final int getBERLength() {
        int payloadLength = getBERPayloadLength();
        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        return oid.getBERLength() + value.getBERLength();
    }


    // toString
    @Override
    public String toString() {
        return "            variable:\n" +
                "                OID: " + oid.toString() + "\n" +
                "                value: " + value.toString() + "\n";
    }
}
