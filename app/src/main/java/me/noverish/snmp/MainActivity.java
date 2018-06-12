package me.noverish.snmp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.net.DatagramSocket;

import me.noverish.snmp.packet.pdu.PDUOID;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.snmp.SNMPPacket;
import me.noverish.snmp.utils.SNMPHelper;
import me.noverish.snmp.utils.SNMPPacketBuilder;

public class MainActivity extends AppCompatActivity {

    public static String HOST = "kuwiden.iptime.org";
    public static int PORT = 11161;
    public static String COMMUNITY_READ = "public";
    public static String COMMUNITY_WRITE = "write";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    testWalk();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


    }


    public void testGet() throws IOException {
        SNMPPacket packet = SNMPPacketBuilder.create(
                COMMUNITY_READ,
                PDUType.GET_REQUEST,
                0x12345678,
                "1.3.6.1.2.1.2.2.1.7.1",
                null);

        SNMPHelper.sendAndReceive(HOST, PORT, packet);
    }

    public void testSet() throws IOException {
        SNMPPacket packet = SNMPPacketBuilder.create(
                COMMUNITY_WRITE,
                PDUType.GET_REQUEST,
                0x12345678,
                "1.3.6.1.2.1.2.2.1.7.1",
                2);

        SNMPHelper.sendAndReceive(HOST, PORT, packet);
    }

    public void testWalk() throws IOException {
        DatagramSocket socket = new DatagramSocket();

        SNMPPacket packet = SNMPPacketBuilder.create(
                COMMUNITY_READ,
                PDUType.GET_NEXT_REQUEST,
                0x12345678,
                "1.3.6.1.2.1",
                null);

        while (true) {
            SNMPPacket received = SNMPHelper.sendAndReceive(socket, HOST, PORT, packet);

            if (received.pdu.variables.get(0).value.isEnd != null) {
                System.out.println("ENDEND!!");
                return;
            }

            packet.pdu.requestId += 1;
            packet.pdu.variables.get(0).oid = new PDUOID(received.pdu.variables.get(0).oid.toString());
        }
    }
}
