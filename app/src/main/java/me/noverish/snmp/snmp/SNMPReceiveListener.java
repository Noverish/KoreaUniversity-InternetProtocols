package me.noverish.snmp.snmp;

import me.noverish.snmp.packet.snmp.SNMP;

public interface SNMPReceiveListener {
    void onSNMPPacketReceived(SNMP packet);
}
