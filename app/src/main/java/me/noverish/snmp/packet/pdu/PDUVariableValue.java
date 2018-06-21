package me.noverish.snmp.packet.pdu;

import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;

import java.io.IOException;
import java.io.OutputStream;

import me.noverish.snmp.utils.CustomBERSerializable;
import me.noverish.snmp.utils.Utils;

public class PDUVariableValue implements CustomBERSerializable {
    public Integer intValue = null;
    public String stringValue = null;
    public PDUVariableOID oidValue = null;
    public Long timeTickValue = null;
    public Long gauge32Value = null;
    public Long counter32Value = null;
    public Boolean isEnd = null;

    public PDUVariableValue() {

    }

    public PDUVariableValue(Integer intValue) {
        this.intValue = intValue;
    }

    // CustomBERSerializable
    @Override
    public void encodeBER(OutputStream os) throws IOException {
        if (intValue != null) {
            BER.encodeInteger(os, BER.INTEGER, intValue);
        } else if (stringValue != null) {
            BER.encodeString(os, BER.OCTETSTRING, stringValue.getBytes());
        } else if (oidValue != null) {
            oidValue.encodeBER(os);
        } else if (timeTickValue != null) {
            BER.encodeUnsignedInteger(os, BER.TIMETICKS, timeTickValue);
        } else if (gauge32Value != null) {
            BER.encodeUnsignedInteger(os, BER.TIMETICKS, gauge32Value);
        } else if (counter32Value != null) {
            BER.encodeUnsignedInteger(os, BER.TIMETICKS, counter32Value);
        } else if (isEnd != null) {
            BER.encodeHeader(os, BER.ENDOFMIBVIEW, getBERPayloadLength());
        } else {
            BER.encodeHeader(os, BER.ASN_NULL, getBERPayloadLength());
        }
    }

    @Override
    public void decodeBER(BERInputStream is) throws IOException {
        byte type = is.getBuffer().array()[(int) is.getPosition()];
//        System.out.println("type : " + type);

        switch (type) {
            case BER.INTEGER: {
                BER.MutableByte valueType = new BER.MutableByte();
                intValue = BER.decodeInteger(is, valueType);
                break;
            }
            case BER.OCTETSTRING: {
                BER.MutableByte valueType = new BER.MutableByte();
                stringValue = new String(BER.decodeString(is, valueType));
                break;
            }
            case BER.OID: {
                oidValue = new PDUVariableOID();
                oidValue.decodeBER(is);
                break;
            }
            case BER.TIMETICKS: {
                BER.MutableByte valueType = new BER.MutableByte();
                timeTickValue = BER.decodeUnsignedInteger(is, valueType);
                break;
            }
            case BER.GAUGE32: {
                BER.MutableByte valueType = new BER.MutableByte();
                gauge32Value = BER.decodeUnsignedInteger(is, valueType);
                break;
            }
            case BER.COUNTER32: {
                BER.MutableByte valueType = new BER.MutableByte();
                counter32Value = BER.decodeUnsignedInteger(is, valueType);
                break;
            }
            case (byte) BER.ENDOFMIBVIEW: {
                BER.MutableByte valueType = new BER.MutableByte();
                BER.decodeHeader(is, valueType);
                isEnd = true;
                break;
            }
            case BER.NULL: {
                BER.MutableByte valueType = new BER.MutableByte();
                BER.decodeHeader(is, valueType);
                break;
            }
            default: {
                throw new IllegalStateException("Unknown PDU Variable Value");
            }
        }
    }

    @Override
    public int getBERLength() {
        int payloadLength = getBERPayloadLength();

//        if(intValue != null) {
//            return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
//        } else {
//            return 2;
//        }

        return payloadLength + BER.getBERLengthOfLength(payloadLength) + 1;
    }

    @Override
    public int getBERPayloadLength() {
        if (intValue != null) {
            return Utils.getHexLengthOfInteger(intValue);
        } else if (stringValue != null) {
            return stringValue.length();
        } else if (oidValue != null) {
            return oidValue.getBERPayloadLength();
        } else if (timeTickValue != null) {
            return Utils.getHexLengthOfLong(timeTickValue);
        } else if (gauge32Value != null) {
            return Utils.getHexLengthOfLong(gauge32Value);
        } else if (counter32Value != null) {
            return Utils.getHexLengthOfLong(counter32Value);
        } else if (isEnd != null) {
            return 0;
        } else {
            return 0;
        }
    }


    // toString
    @Override
    public String toString() {
        if (intValue != null) {
            return "{ Integer: " + intValue + " }";
        } else if (stringValue != null) {
            return "{ String: " + stringValue + " }";
        } else if (oidValue != null) {
            return "{ OID: " + oidValue.toString() + " }";
        } else if (timeTickValue != null) {
            return "{ TimeTick: " + timeTickValue + " }";
        } else if (gauge32Value != null) {
            return "{ Gauge32: " + gauge32Value + " }";
        } else if (counter32Value != null) {
            return "{ Counter32: " + counter32Value + " }";
        } else if (isEnd != null) {
            return "{ EndOfMibWindow }";
        } else {
            return "null";
        }
    }
}
