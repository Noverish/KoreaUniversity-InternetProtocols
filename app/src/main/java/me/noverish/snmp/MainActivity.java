package me.noverish.snmp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramSocket;

import me.noverish.snmp.packet.pdu.PDUOID;
import me.noverish.snmp.packet.pdu.PDUType;
import me.noverish.snmp.packet.snmp.SNMPPacket;
import me.noverish.snmp.snmp.SNMPGetAsyncTask;
import me.noverish.snmp.snmp.SNMPReceiveListener;
import me.noverish.snmp.snmp.SNMPSetAsyncTask;
import me.noverish.snmp.snmp.SNMPWalkAsyncTask;
import me.noverish.snmp.utils.SNMPHelper;
import me.noverish.snmp.utils.SNMPPacketBuilder;

public class MainActivity extends AppCompatActivity {

    public static String HOST = "kuwiden.iptime.org";
    public static int PORT = 11161;
    public static String COMMUNITY_READ = "public";
    public static String COMMUNITY_WRITE = "write";

    private EditText oidField, valueField;
    private TextView resultTextView;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    testWalk();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute();

        View getBtn = findViewById(R.id.get_btn);
        View setBtn = findViewById(R.id.set_btn);
        View walkBtn = findViewById(R.id.walk_btn);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetBtnClicked();
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSetBtnClicked();
            }
        });
        walkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWalkBtnClicked();
            }
        });

        oidField = findViewById(R.id.oid_field);
        valueField = findViewById(R.id.value_field);
        resultTextView = findViewById(R.id.result_text_view);
        scrollView = findViewById(R.id.scroll_view);
    }

    private void onGetBtnClicked() {
        String oid = oidField.getText().toString();

        new SNMPGetAsyncTask(oid)
                .setListener(new SNMPReceiveListener() {
                    @Override
                    public void onSNMPPacketReceived(SNMPPacket packet) {
                        resultTextView.setText(packet.toString());
                    }
                })
                .execute();
    }

    private void onSetBtnClicked() {
        String oid = oidField.getText().toString();
        int value = Integer.parseInt(valueField.getText().toString());

        new SNMPSetAsyncTask(oid, value)
                .setListener(new SNMPReceiveListener() {
                    @Override
                    public void onSNMPPacketReceived(SNMPPacket packet) {
                        resultTextView.setText(packet.toString());
                    }
                })
                .execute();
    }

    private void onWalkBtnClicked() {
        new SNMPWalkAsyncTask()
                .setListener(new SNMPReceiveListener() {
                    @Override
                    public void onSNMPPacketReceived(final SNMPPacket packet) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String tmp = resultTextView.getText().toString();
                                tmp += packet.toString() + "\n";
                                resultTextView.setText(tmp);

//                                scrollView.scrollTo(0, Integer.MAX_VALUE);
                            }
                        });

                    }
                })
                .execute();
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
