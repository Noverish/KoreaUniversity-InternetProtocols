package me.noverish.snmp.snmp;

import me.noverish.snmp.packet.snmp.SNMPPacket;

public interface SNMPReceiveListener {
    void onSNMPPacketReceived(SNMPPacket packet);
}
