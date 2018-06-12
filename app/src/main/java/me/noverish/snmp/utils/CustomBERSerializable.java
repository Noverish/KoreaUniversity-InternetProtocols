package me.noverish.snmp.utils;

import org.snmp4j.asn1.BERInputStream;

import java.io.IOException;
import java.io.OutputStream;

public interface CustomBERSerializable {
    void encodeBER(OutputStream os) throws IOException;

    void decodeBER(BERInputStream is) throws IOException;

    int getBERLength();

    int getBERPayloadLength();
}
