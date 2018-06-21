package me.noverish.snmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import me.noverish.snmp.packet.SNMP;
import me.noverish.snmp.snmp.async_task.SNMPGetAsyncTask;
import me.noverish.snmp.snmp.async_task.SNMPReceiveListener;
import me.noverish.snmp.snmp.async_task.SNMPSetAsyncTask;
import me.noverish.snmp.snmp.async_task.SNMPWalkAsyncTask;

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
                    public void onSNMPPacketReceived(SNMP packet) {
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
                    public void onSNMPPacketReceived(SNMP packet) {
                        resultTextView.setText(packet.toString());
                    }
                })
                .execute();
    }

    private void onWalkBtnClicked() {
        resultTextView.setText("");

        new SNMPWalkAsyncTask()
                .setListener(new SNMPReceiveListener() {
                    @Override
                    public void onSNMPPacketReceived(final SNMP packet) {
                        String tmp = resultTextView.getText().toString();
                        tmp += packet.toSimpleString() + "\n";
                        resultTextView.setText(tmp);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                })
                .execute();
    }
}
