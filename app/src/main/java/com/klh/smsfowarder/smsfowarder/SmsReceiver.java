package com.klh.smsfowarder.smsfowarder;

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

public class SmsReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        if("com.klh.smsfowarder.SENT_SMS".equals(intent.getAction())) {

            String resultCode;
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    resultCode = "Sent OK";
                break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    resultCode = "RESULT_ERROR_GENERIC_FAILURE";
                break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    resultCode = "RESULT_ERROR_RADIO_OFF";
                break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    resultCode = "RESULT_ERROR_NULL_PDU";
                break;

                default:
                    resultCode = String.valueOf(getResultCode());
            }

            SharedPreferences sp = context.getSharedPreferences("logs", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lastSentLog", resultCode);
            editor.commit();

        }

        if("com.klh.smsfowarder.DELIVERY_SMS".equals(intent.getAction())) {
            String resultCode;
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    resultCode = "Delivery OK";
                    break;
                case Activity.RESULT_CANCELED:
                    resultCode = "RESULT_CANCELED";
                    break;
                default:
                    resultCode = String.valueOf(getResultCode());
            }

            SharedPreferences sp = context.getSharedPreferences("logs", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lastDeliveryLog", resultCode);
            editor.commit();
        }


        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Log.d("onReceive()","Sms Received");

            try {

                SharedPreferences sp = context.getSharedPreferences("numbers", context.MODE_PRIVATE);
                final String sender = sp.getString("sender", "");
                final String target = sp.getString("target", "");


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
                String message = smsMessage[0].getMessageBody().toString();


                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String timestamp = sdf.format(cal.getTime());

                SharedPreferences logSP = context.getSharedPreferences("logs", context.MODE_PRIVATE);
                SharedPreferences.Editor ed = logSP.edit();
                ed.putString("receivedSender", origNumber);
                ed.putString("receivedTimestamp", timestamp);
                ed.putString("receivedMsg", message);
                ed.commit();




                Log.d("onReceive()", origNumber + " " + curDate.toString() + " " + message);
                if (origNumber.contains(sender)) {
                    sendSMS(target, message, context);
                }
                ed.remove("exception");
                ed.putString("sentTimestamp", sdf.format(cal.getTime()));
                ed.putString("sentMsg", message);
                ed.putString("sentTarget", target);
                ed.commit();
            } catch (Exception e) {
                SharedPreferences logSP = context.getSharedPreferences("logs", context.MODE_PRIVATE);
                SharedPreferences.Editor ed = logSP.edit();
                ed.putString("exception", e.getMessage());
                ed.commit();
            }
        }
    }

    void sendSMS(String phoneNumber, String msg, Context c) {
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent sentIntent = PendingIntent.getBroadcast(c, 0, new Intent("com.klh.smsfowarder.SENT_SMS"), 0);
        PendingIntent deliverIntent = PendingIntent.getBroadcast(c, 0, new Intent("com.klh.smsfowarder.DELIVERY_SMS"), 0);

        if(msg.length() > 160) {
            msg += "MMS:";
            ArrayList<String> msgList = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(phoneNumber, null, msgList, null, null);
            Log.d("onReceive()", "send MMS:" + msg.length());
        } else {
            smsManager.sendTextMessage(phoneNumber, null, msg, sentIntent, deliverIntent);
            Log.d("onReceive()", "send SMS:" + msg.length());
        }

    }
}
