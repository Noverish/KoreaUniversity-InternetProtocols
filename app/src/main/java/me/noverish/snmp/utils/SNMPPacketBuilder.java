package me.noverish.snmp.utils;


import java.util.ArrayList;

import me.noverish.snmp.packet.pdu.PDUOID;
import me.noverish.snmp.packet.pdu.PDUPacket;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.pdu.PDUValue;
import me.noverish.snmp.packet.pdu.PDUVariable;
import me.noverish.snmp.packet.snmp.SNMPCommunity;
import me.noverish.snmp.packet.snmp.SNMPPacket;
import me.noverish.snmp.packet.snmp.SNMPVersion;

public class SNMPPacketBuilder {
    public static SNMPPacket create(String communityStr, PDUType pduType, int requestId, String oidStr, Object obj) {
        PDUOID oid = new PDUOID(oidStr);
        PDUValue value;
        if (obj instanceof Integer)
            value = new PDUValue((Integer) obj);
        else
            value = new PDUValue();

        PDUVariable variable = new PDUVariable(oid, value);
        ArrayList<PDUVariable> variables = new ArrayList<>();
        variables.add(variable);

        PDUPacket pdu = new PDUPacket(pduType, requestId, 0, 0, variables);

        SNMPCommunity community = new SNMPCommunity(communityStr);
        SNMPVersion version = SNMPVersion.v2c;

        return new SNMPPacket(version, community, pdu);
    }
}
