package me.noverish.snmp.snmp.async_task;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Random;

import me.noverish.snmp.MainActivity;
import me.noverish.snmp.net.NetworkClient;
import me.noverish.snmp.packet.pdu.PDUVariableOID;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.snmp.SNMP;
import me.noverish.snmp.snmp.utils.SNMPPacketBuilder;

public class SNMPWalkAsyncTask extends AsyncTask<Void, SNMP, SNMP> {

    private SNMP packet;
    private SNMPReceiveListener listener;

    public SNMPWalkAsyncTask() {
        int requestId = new Random().nextInt(0x7FFFFFFF);

        packet = SNMPPacketBuilder.create(
                MainActivity.COMMUNITY_READ,
                PDUType.GET_NEXT_REQUEST,
                requestId,
                "1.3.6.1.2.1",
                null
        );
    }

    @Override
    protected SNMP doInBackground(Void... voids) {
        try {
            DatagramSocket socket = new DatagramSocket();

            while (true) {
                SNMP received = NetworkClient.sendSNMP(socket, MainActivity.HOST, MainActivity.PORT, packet);

                publishProgress(received);

                if (received.pdu.variables.get(0).value.isEnd != null) {
                    return null;
                }

                packet.pdu.requestId += 1;
                packet.pdu.variables.get(0).oid = new PDUVariableOID(received.pdu.variables.get(0).oid.toString());
            }
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(SNMP... packets) {
        SNMP packet = packets[0];

        if (listener != null)
            if (packet != null)
                listener.onSNMPPacketReceived(packet);
    }

    public SNMPWalkAsyncTask setListener(SNMPReceiveListener listener) {
        this.listener = listener;
        return this;
    }
}
