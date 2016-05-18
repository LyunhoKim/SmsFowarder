package com.klh.smsfowarder.smsfowarder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave;
    EditText etSender, etTarget;
    TextView tvLogs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        btnSave = (Button)findViewById(R.id.btnSave);
        etSender = (EditText)findViewById(R.id.etSender);
        etTarget = (EditText)findViewById(R.id.etTarget);
        tvLogs = (TextView)findViewById(R.id.tvLastLogs);

        btnSave.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("logs", MODE_PRIVATE);
        String receivedSender = sp.getString("receivedSender", "N/A");
        String receivedTimestamp = sp.getString("receivedTimestamp", "N/A");
        String receivedMsg = sp.getString("receivedMsg", "N/A");

        String sentTimestamp = sp.getString("sentTimestamp", "N/A");
        String sentMsg = sp.getString("sentMsg", "N/A");
        String sentTarget = sp.getString("sentTarget", "N/A");

        String lastSentLog = sp.getString("lastSentLog", "N/A");
        String lastDeliveryLog = sp.getString("lastDeliveryLog", "N/A");

        String exception = sp.getString("exception", "N/A");


        tvLogs.setText("#Received Log " + "\n" +
                "-Timestamp: " +  receivedTimestamp + "\n" +
                "-Sender: " + receivedSender + "\n" +
                "-Message: " + receivedMsg +
                "\n\n" +

                "#Send Log" + "\n" +
                "-Timestamp: " + sentTimestamp + "\n" +
                "-Target: " + sentTarget + "\n" +
                "-Message: " + sentMsg + "\n" +
                "-LastSentLog: " + lastSentLog + "\n" +
                "-LastDeliverLog: " + lastDeliveryLog +
                "\n\n" +

                "#Exception " + "\n" + exception);


        SharedPreferences numSp = getSharedPreferences("numbers", MODE_PRIVATE);
        String sender = numSp.getString("sender", "");
        String target = numSp.getString("target", "");

        etSender.setText(sender);
        etTarget.setText(target);


    }

    @Override
    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences("numbers", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sender", etSender.getText().toString());
        editor.commit();
        editor.putString("target", etTarget.getText().toString());
        editor.commit();

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }
}
