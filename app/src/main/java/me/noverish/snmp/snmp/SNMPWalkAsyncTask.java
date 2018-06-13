package me.noverish.snmp.snmp;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Random;

import me.noverish.snmp.MainActivity;
import me.noverish.snmp.packet.pdu.PDUOID;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.snmp.SNMPPacket;
import me.noverish.snmp.utils.SNMPHelper;
import me.noverish.snmp.utils.SNMPPacketBuilder;

public class SNMPWalkAsyncTask extends AsyncTask<Void, SNMPPacket, SNMPPacket> {

    private SNMPPacket packet;
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
    protected SNMPPacket doInBackground(Void... voids) {
        try {
            DatagramSocket socket = new DatagramSocket();

            while (true) {
                SNMPPacket received = SNMPHelper.sendAndReceive(socket, MainActivity.HOST, MainActivity.PORT, packet);

                onProgressUpdate(received);

                if (received.pdu.variables.get(0).value.isEnd != null) {
                    return null;
                }

                packet.pdu.requestId += 1;
                packet.pdu.variables.get(0).oid = new PDUOID(received.pdu.variables.get(0).oid.toString());
            }
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(SNMPPacket... packets) {
        SNMPPacket packet = packets[0];

        if (listener != null)
            if (packet != null)
                listener.onSNMPPacketReceived(packet);
    }

    public SNMPWalkAsyncTask setListener(SNMPReceiveListener listener) {
        this.listener = listener;
        return this;
    }
}
