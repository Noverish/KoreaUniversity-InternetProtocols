package me.noverish.snmp.packet.pdu;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;

import me.noverish.snmp.utils.CustomBERSerializable;
import me.noverish.snmp.utils.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class PDU implements CustomBERSerializable {
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

    // CustomBERSerializable
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
        int length = BER.decodeHeader(is, pduType);
        int pduStartPos = (int) is.getPosition();

        type = PDUType.parseValue(pduType.getValue());

        BER.MutableByte requestIdType = new BER.MutableByte();
        requestId = BER.decodeInteger(is, requestIdType);

        BER.MutableByte errorStatusType = new BER.MutableByte();
        errorStatus = BER.decodeInteger(is, errorStatusType);

        BER.MutableByte errorIndexType = new BER.MutableByte();
        errorIndex = BER.decodeInteger(is, errorIndexType);

        BER.MutableByte variableLengthType = new BER.MutableByte();
        int variableLength = BER.decodeHeader(is, variableLengthType);

        int startPos = (int) is.getPosition();
        variables = new ArrayList<>();
        while (is.getPosition() - startPos < variableLength) {
            PDUVariable vb = new PDUVariable();
            vb.decodeBER(is);
            variables.add(vb);
        }

//        if (BER.isCheckSequenceLength()) {
//            BER.checkSequenceLength(length, (int) is.getPosition() - pduStartPos, this);
//        }
    }

    @Override
    public int getBERLength() {
        int payloadLength = getBERPayloadLength();
        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        int length = 0;

        int variableSequenceLength = 0;
        for (PDUVariable v : variables)
            variableSequenceLength += v.getBERLength();
        length += variableSequenceLength + BER.getBERLengthOfLength(variableSequenceLength) + 1;

        int requestIdLength = Utils.getHexLengthOfInteger(requestId);
        int errorStatusLength = Utils.getHexLengthOfInteger(errorStatus);
        int errorIndexLength = Utils.getHexLengthOfInteger(errorIndex);

        length += requestIdLength + BER.getBERLengthOfLength(requestIdLength) + 1;
        length += errorStatusLength + BER.getBERLengthOfLength(errorStatusLength) + 1;
        length += errorIndexLength + BER.getBERLengthOfLength(errorIndexLength) + 1;

        return length;
    }


    // toString
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("  \"pdu\": {\n");
        builder.append("    \"type\": ").append(type.toString()).append("\n");
        builder.append("    \"requestId\": ").append(String.format("%X", requestId)).append("\n");
        builder.append("    \"errorStatus\": ").append(errorStatus).append("\n");
        builder.append("    \"errorIndex\": ").append(errorIndex).append("\n");

        builder.append("    \"variables\": [\n");
        for (PDUVariable v : variables) {
            builder.append(v.toString());
        }
        builder.append("    ]\n");

        builder.append("  }\n");
        return builder.toString();
    }
}
