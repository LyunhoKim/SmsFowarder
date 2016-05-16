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
        String receivedLog = sp.getString("receivedLog", "There are no received log.");

        String sentLog = sp.getString("lastSentLog", "Didn't saved Sent Logs.");
        String deliverLog = sp.getString("lastDeliveryLog", "Didn't saved Deliver Logs.");

        String exception = sp.getString("exception", "There are no exception.");


        tvLogs.setText("Received Log " + "\n" +
                receivedLog + "\n\n" +
                "Send Log" + "\n" +
                "SentLog: " + sentLog + "\n" +
                "DeliverLog: " + deliverLog + "\n\n" +
                "Exception " + "\n" + exception);

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
        editor.putString("target", etTarget.getText().toString());
        editor.commit();

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }
}
