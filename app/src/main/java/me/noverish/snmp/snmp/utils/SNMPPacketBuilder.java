package me.noverish.snmp.snmp.utils;


import java.util.ArrayList;

import me.noverish.snmp.packet.pdu.PDUVariableOID;
import me.noverish.snmp.packet.pdu.PDU;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.pdu.PDUVariableValue;
import me.noverish.snmp.packet.pdu.PDUVariable;
import me.noverish.snmp.packet.snmp.SNMPCommunity;
import me.noverish.snmp.packet.snmp.SNMP;
import me.noverish.snmp.packet.snmp.SNMPVersion;

public class SNMPPacketBuilder {
    public static SNMP create(String communityStr, PDUType pduType, int requestId, String oidStr, Object obj) {
        PDUVariableOID oid = new PDUVariableOID(oidStr);
        PDUVariableValue value = new PDUVariableValue();
        if (obj instanceof Integer)
            value.intValue = (Integer) obj;

        PDUVariable variable = new PDUVariable(oid, value);
        ArrayList<PDUVariable> variables = new ArrayList<>();
        variables.add(variable);

        PDU pdu = new PDU(pduType, requestId, 0, 0, variables);

        SNMPCommunity community = new SNMPCommunity(communityStr);
        SNMPVersion version = SNMPVersion.v2c;

        return new SNMP(version, community, pdu);
    }
}
