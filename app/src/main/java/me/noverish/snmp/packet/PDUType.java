package me.noverish.snmp.packet;

import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BERSerializable;

import java.io.IOException;
import java.io.OutputStream;

public enum PDUType {
    GET_REQUEST,
    GET_NEXT_REQUEST,
    GET_RESPONSE,
    SET_REQUEST;

    public byte getValue() {
        switch (this) {
            case GET_REQUEST:
                return (byte) 0xA0;
            case GET_NEXT_REQUEST:
                return (byte) 0xA1;
            case GET_RESPONSE:
                return (byte) 0xA2;
            case SET_REQUEST:
                return (byte) 0xA3;
            default:
                throw new IllegalStateException("Unknown PDU Type");
        }
    }

    public static PDUType parse(byte value) {
        for (PDUType t : PDUType.values())
            if (t.getValue() == value)
                return t;

        throw new IllegalStateException("Unknown PDU Type Value");
    }


    // toString
    @Override
    public String toString() {
        return this.name();
    }
}
