package me.noverish.snmp.snmp.async_task;

import me.noverish.snmp.packet.SNMP;

public interface SNMPReceiveListener {
    void onSNMPPacketSent(SNMP packet);
    void onSNMPPacketReceived(SNMP packet);
}
