package com.klh.smsfowarder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.klh.smsfowarder.database.DBHelper;
import com.klh.smsfowarder.smsfowarder.R;

import static android.view.View.GONE;

public class NewPhoneNumber extends AppCompatActivity implements View.OnClickListener {

    EditText etName;
    EditText etPhoneNumber;
    CheckBox cbIsSender;

    String mActivityMode;
    String mTableName;
    int _id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_phone_number);


        mActivityMode = "new";
        etName = (EditText)findViewById(R.id.et_name);
        etPhoneNumber = (EditText)findViewById(R.id.et_phonenumber);
        cbIsSender = (CheckBox)findViewById(R.id.cb_sender);

        ((Button)findViewById(R.id.btn_save)).setOnClickListener(this);
        ((Button)findViewById(R.id.btn_delete)).setOnClickListener(this);


        mActivityMode = getIntent().getStringExtra("activity_mode");

        // 편집인지 구분하여 뷰 셋팅
        if(mActivityMode != null) {
            ((Button)findViewById(R.id.btn_delete)).setVisibility(View.VISIBLE);

            mTableName = getIntent().getStringExtra("which_table");
            _id = getIntent().getIntExtra("_id", -1);

            cbIsSender.setEnabled(false);
            cbIsSender.setChecked(mTableName.equals(DBHelper.TABLE_NAME_SENDER));

            String data = getIntent().getStringExtra("data");
            String [] tmp = data.split("\n");

            etName.setText(tmp[0]);
            etPhoneNumber.setText(tmp[1]);
        }


    }

    @Override
    public void onClick(View v) {


        if(v.getId() == R.id.btn_save) {


            boolean isSender = cbIsSender.isChecked();
            String table = isSender ? DBHelper.TABLE_NAME_SENDER : DBHelper.TABLE_NAME_RECEIVER;


            DBHelper dbh = new DBHelper(this.getApplicationContext(), "SMSForwarder.db", null, 1);
            if(mActivityMode == null) {
                dbh.insertPhoneNumber(table, etName.getText().toString(), etPhoneNumber.getText().toString());
                dbh.close();
                setResult(RESULT_OK);
//                finishActivity(MainScreenActivity.REQ_CODE_NEW_NUMBER);
                Toast.makeText(this, "SAVED!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                dbh.updatePhoneNumber(_id, table, etName.getText().toString(), etPhoneNumber.getText().toString());
                dbh.close();
                setResult(RESULT_OK);
//                finishActivity(MainScreenActivity.REQ_CODE_MODIFY_NUMBER);
                Toast.makeText(this, "MODIFIED!", Toast.LENGTH_LONG).show();
                finish();
            }





        } else if(v.getId() == R.id.btn_delete) {
            DBHelper dbh = new DBHelper(this.getApplicationContext(), "SMSForwarder.db", null, 1);
            dbh.deletePhoneNumber(_id, mTableName);
            dbh.close();

            setResult(RESULT_OK);
            Toast.makeText(this, "DELETED!", Toast.LENGTH_LONG).show();
            finish();
        }

    }
}
