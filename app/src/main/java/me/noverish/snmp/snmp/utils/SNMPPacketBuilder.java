package me.noverish.snmp.snmp.utils;


import java.util.ArrayList;

import me.noverish.snmp.packet.PDUVariableOID;
import me.noverish.snmp.packet.PDU;
import me.noverish.snmp.packet.PDUType;
import me.noverish.snmp.packet.PDUVariableValue;
import me.noverish.snmp.packet.PDUVariable;
import me.noverish.snmp.packet.SNMP;

public class SNMPPacketBuilder {
    public static SNMP create(String community, PDUType pduType, int requestId, String oidStr, Object obj) {
        PDUVariableOID oid = new PDUVariableOID(oidStr);
        PDUVariableValue value = new PDUVariableValue();
        if (obj instanceof Integer)
            value.intValue = (Integer) obj;

        PDUVariable variable = new PDUVariable(oid, value);
        ArrayList<PDUVariable> variables = new ArrayList<>();
        variables.add(variable);

        PDU pdu = new PDU(pduType, requestId, 0, 0, variables);

        return new SNMP(1, community, pdu);
    }
}
