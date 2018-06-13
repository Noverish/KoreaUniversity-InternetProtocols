package me.noverish.snmp.snmp;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.Random;

import me.noverish.snmp.MainActivity;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.snmp.SNMPPacket;
import me.noverish.snmp.utils.SNMPHelper;
import me.noverish.snmp.utils.SNMPPacketBuilder;

public class SNMPSetAsyncTask extends AsyncTask<Void, Void, SNMPPacket> {

    private SNMPPacket packet;
    private SNMPReceiveListener listener;

    public SNMPSetAsyncTask(String oid, int value) {
        int requestId = new Random().nextInt(0x7FFFFFFF);

        packet = SNMPPacketBuilder.create(
                MainActivity.COMMUNITY_WRITE,
                PDUType.SET_REQUEST,
                requestId,
                oid,
                value
        );
    }

    @Override
    protected SNMPPacket doInBackground(Void... voids) {
        try {
            return SNMPHelper.sendAndReceive(MainActivity.HOST, MainActivity.PORT, packet);
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(SNMPPacket packet) {
        if (listener != null)
            if (packet != null)
                listener.onSNMPPacketReceived(packet);
    }

    public SNMPSetAsyncTask setListener(SNMPReceiveListener listener) {
        this.listener = listener;
        return this;
    }
}
