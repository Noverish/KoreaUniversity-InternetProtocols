package me.noverish.snmp.snmp.async_task;

import me.noverish.snmp.packet.snmp.SNMP;

public interface SNMPReceiveListener {
    void onSNMPPacketReceived(SNMP packet);
}
