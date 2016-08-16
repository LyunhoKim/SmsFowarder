package com.klh.smsfowarder.receiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.klh.smsfowarder.database.DBHelper;
import com.klh.smsfowarder.dtc.PhoneNumber;

public class SmsReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

//        if("com.klh.smsfowarder.SENT_SMS".equals(intent.getAction())) {
//
//            String resultCode;
//            switch (getResultCode()) {
//                case Activity.RESULT_OK:
//                    resultCode = "Sent OK";
//                break;
//                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                    resultCode = "RESULT_ERROR_GENERIC_FAILURE";
//                break;
//                case SmsManager.RESULT_ERROR_RADIO_OFF:
//                    resultCode = "RESULT_ERROR_RADIO_OFF";
//                break;
//                case SmsManager.RESULT_ERROR_NULL_PDU:
//                    resultCode = "RESULT_ERROR_NULL_PDU";
//                break;
//
//                default:
//                    resultCode = String.valueOf(getResultCode());
//            }
//
//
////            SharedPreferences sp = context.getSharedPreferences("logs", context.MODE_PRIVATE);
////            SharedPreferences.Editor editor = sp.edit();
////            editor.putString("lastSentLog", resultCode);
////            editor.commit();
//
//        }
//
//        if("com.klh.smsfowarder.DELIVERY_SMS".equals(intent.getAction())) {
//            String resultCode;
//            switch (getResultCode()) {
//                case Activity.RESULT_OK:
//                    resultCode = "Delivery OK";
//                    break;
//                case Activity.RESULT_CANCELED:
//                    resultCode = "RESULT_CANCELED";
//                    break;
//                default:
//                    resultCode = String.valueOf(getResultCode());
//            }
//
//            SharedPreferences sp = context.getSharedPreferences("logs", context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("lastDeliveryLog", resultCode);
//            editor.commit();
//        }

        // SMS 수신 시
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction()) ||
                "android.provider.Telephony.SMS_DELIVER".equals(intent.getAction())) {
            Log.d("onReceive()","Sms Received");
            String sendLog = new String();
            String message = new String("Empty Message");
            String timestamp = new String("Empty Message");
            DBHelper dbh = new DBHelper(context, "SMSForwarder.db", null, 1);

            try {


                ArrayList<PhoneNumber> senderArray = dbh.getPhoneNumbers(DBHelper.TABLE_NAME_SENDER);
                ArrayList<PhoneNumber> receiverArray = dbh.getPhoneNumbers(DBHelper.TABLE_NAME_RECEIVER);

                Bundle bundle = intent.getExtras();
                Object messages[] = (Object[]) bundle.get("pdus");
                SmsMessage smsMessage[] = new SmsMessage[messages.length];

                for (int i = 0; i < messages.length; i++) {

                    smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);
                }


                // 메시지 발신자 확인
                String origNumber = smsMessage[0].getOriginatingAddress();

                // 메시지 수신 시간 확인
                Date curDate = new Date(smsMessage[0].getTimestampMillis());
                // curDate.toString();

                // 메시지 내용 확인
                message = smsMessage[0].getMessageBody().toString();


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                timestamp = sdf.format(cal.getTime());

                Log.d("onReceive()", origNumber + " " + curDate.toString() + " " + message);

                if (isRegisterSender(senderArray, origNumber)) {
                    for (PhoneNumber pn :receiverArray) {
                        sendLog += sendSMS(pn.getNumber(), message, context) + "\n";
                    }
                }
            } catch (Exception e) {
                sendLog += "\n" +
                        "[Exception]" + "\n" +
                        e.getMessage();
            } finally {
                dbh.insertLog(message, sendLog, timestamp);
            }
        }
    }

    // SMS 발송
    String sendSMS(String phoneNumber, String msg, Context c) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(c, 0, new Intent("com.klh.smsfowarder.SENT_SMS").putExtra("phonenumber", phoneNumber), 0);
        PendingIntent deliverIntent = PendingIntent.getBroadcast(c, 0, new Intent("com.klh.smsfowarder.DELIVERY_SMS"), 0);

        try {
            if (msg.length() > 160) {
                msg += "MMS:";
                ArrayList<String> msgList = smsManager.divideMessage(msg);
                smsManager.sendMultipartTextMessage(phoneNumber, null, msgList, null, null);
                Log.d("onReceive()", "send MMS:" + msg.length());
            } else {
                smsManager.sendTextMessage(phoneNumber, null, msg, sentIntent, deliverIntent);
                Log.d("onReceive()", "send SMS:" + msg.length());
            }
        } catch (Exception e) {
            return phoneNumber + ": Send Failed(" + e.getMessage() + ")";
        }
        return phoneNumber + ": Send OK";
    }

    // Check sender is in Database
    boolean isRegisterSender(ArrayList<PhoneNumber> list, String number) {
        for (PhoneNumber pn : list) {
            if(number.contains(pn.getNumber())) return true;
        }
        return false;
    }
}
