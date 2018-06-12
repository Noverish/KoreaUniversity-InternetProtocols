package me.noverish.snmp.packet.pdu;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;

import java.io.IOException;
import java.io.OutputStream;

import me.noverish.snmp.utils.CustomBERSerializable;

public class PDUVariable implements CustomBERSerializable {
    public PDUOID oid;
    public PDUValue value;

    public PDUVariable() {

    }

    public PDUVariable(PDUOID oid, PDUValue value) {
        this.oid = oid;
        this.value = value;
    }

    // CustomBERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        int length = getBERPayloadLength();
        BER.encodeHeader(os, BER.SEQUENCE, length);
        oid.encodeBER(os);
        value.encodeBER(os);
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        BER.MutableByte type = new BER.MutableByte();
        int length = BER.decodeHeader(is, type);
        long startPos = is.getPosition();

        oid = new PDUOID();
        oid.decodeBER(is);

        value = new PDUValue();
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
        return "      {\n" +
                "        \"OID\": " + oid.toString() + "\n" +
                "        \"value\": " + value.toString() + "\n" +
                "      }\n";
    }
}
