package me.noverish.snmp.snmp.async_task;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Random;

import me.noverish.snmp.MainActivity;
import me.noverish.snmp.net.NetworkClient;
import me.noverish.snmp.packet.PDUVariableOID;
import me.noverish.snmp.packet.PDUType;
import me.noverish.snmp.packet.SNMP;
import me.noverish.snmp.snmp.utils.SNMPPacketBuilder;

public class SNMPWalkAsyncTask extends AsyncTask<Void, SNMP, SNMP> {

    private SNMP packet;
    private SNMPPacketCallback callback;

    public SNMPWalkAsyncTask() {
        int requestId = new Random().nextInt(0x7FFFFFFF);

        packet = SNMPPacketBuilder.create(
                MainActivity.COMMUNITY_READ,
                PDUType.GET_NEXT_REQUEST,
                requestId,
                "1.3.6.1.2.1"
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
                packet.pdu.variables.get(0).oid = received.pdu.variables.get(0).oid;
            }
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        if (callback != null && packet != null)
            callback.onSNMPPacketSent(packet);
    }

    @Override
    protected void onProgressUpdate(SNMP... packets) {
        SNMP packet = packets[0];
        if (callback != null && packet != null)
            callback.onSNMPPacketReceived(packet);
    }

    public SNMPWalkAsyncTask setCallback(SNMPPacketCallback listener) {
        this.callback = listener;
        return this;
    }
}
