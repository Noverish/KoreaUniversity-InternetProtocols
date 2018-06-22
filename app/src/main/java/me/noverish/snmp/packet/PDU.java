package me.noverish.snmp.packet;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BERSerializable;

import me.noverish.snmp.snmp.utils.BERLengthUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PDU implements BERSerializable {
    public PDUType type;
    public int requestId;
    public int errorStatus = 0;
    public int errorIndex = 0;
    public ArrayList<PDUVariable> variables;

    public PDU() {

    }

    public PDU(PDUType type, int requestId, int errorStatus, int errorIndex, ArrayList<PDUVariable> variables) {
        this.type = type;
        this.requestId = requestId;
        this.errorStatus = errorStatus;
        this.errorIndex = errorIndex;
        this.variables = variables;
    }

    // BERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        BER.encodeHeader(os, type.getValue(), getBERPayloadLength());
        BER.encodeInteger(os, BER.INTEGER, requestId);
        BER.encodeInteger(os, BER.INTEGER, errorStatus);
        BER.encodeInteger(os, BER.INTEGER, errorIndex);

        int variablesLength = 0;
        for (PDUVariable v : variables)
            variablesLength += v.getBERLength();

        BER.encodeHeader(os, BER.SEQUENCE, variablesLength);
        for (PDUVariable v : variables)
            v.encodeBER(os);
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        BER.MutableByte pduType = new BER.MutableByte();
        BER.decodeHeader(is, pduType);
        type = PDUType.parse(pduType.getValue());

        requestId = BER.decodeInteger(is, new BER.MutableByte());
        errorStatus = BER.decodeInteger(is, new BER.MutableByte());
        errorIndex = BER.decodeInteger(is, new BER.MutableByte());

        int variableLength = BER.decodeHeader(is, new BER.MutableByte());

        int startPos = (int) is.getPosition();
        variables = new ArrayList<>();
        while (is.getPosition() - startPos < variableLength) {
            PDUVariable vb = new PDUVariable();
            vb.decodeBER(is);
            variables.add(vb);
        }
    }

    @Override
    public int getBERLength() {
        int payloadLength = getBERPayloadLength();
        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        int length = 0;

        int requestIdLength = BERLengthUtil.getLengthOfInteger(requestId);
        int errorStatusLength = BERLengthUtil.getLengthOfInteger(errorStatus);
        int errorIndexLength = BERLengthUtil.getLengthOfInteger(errorIndex);

        length += requestIdLength + BER.getBERLengthOfLength(requestIdLength) + 1;
        length += errorStatusLength + BER.getBERLengthOfLength(errorStatusLength) + 1;
        length += errorIndexLength + BER.getBERLengthOfLength(errorIndexLength) + 1;

        int variableSequenceLength = 0;
        for (PDUVariable v : variables)
            variableSequenceLength += v.getBERLength();
        length += variableSequenceLength + BER.getBERLengthOfLength(variableSequenceLength) + 1;

        return length;
    }


    // toString
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("    PDU:\n");
        builder.append("        type: ").append(type.toString()).append("\n");
        builder.append("        requestId: ").append(String.format("0x%X", requestId)).append("\n");
        builder.append("        errorStatus: ").append(errorStatus).append("\n");
        builder.append("        errorIndex: ").append(errorIndex).append("\n");
        builder.append("        variables (array):\n");
        for (PDUVariable v : variables)
            builder.append(v.toString());

        return builder.toString();
    }
}
