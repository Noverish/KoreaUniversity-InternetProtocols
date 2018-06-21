package me.noverish.snmp.snmp;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.Random;

import me.noverish.snmp.MainActivity;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.snmp.SNMP;
import me.noverish.snmp.utils.SNMPHelper;
import me.noverish.snmp.utils.SNMPPacketBuilder;

public class SNMPGetAsyncTask extends AsyncTask<Void, Void, SNMP> {

    private SNMPReceiveListener listener;
    private SNMP packet;

    public SNMPGetAsyncTask(String oid) {
        int requestId = new Random().nextInt(0x7FFFFFFF);

        packet = SNMPPacketBuilder.create(
                MainActivity.COMMUNITY_READ,
                PDUType.GET_REQUEST,
                requestId,
                oid,
                null
        );
    }

    @Override
    protected SNMP doInBackground(Void... voids) {
        try {
            return SNMPHelper.sendAndReceive(MainActivity.HOST, MainActivity.PORT, packet);
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(SNMP packet) {
        if (listener != null)
            if (packet != null)
                listener.onSNMPPacketReceived(packet);
    }

    public SNMPGetAsyncTask setListener(SNMPReceiveListener listener) {
        this.listener = listener;
        return this;
    }
}
