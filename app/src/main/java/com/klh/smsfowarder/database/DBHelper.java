package com.klh.smsfowarder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.klh.smsfowarder.dtc.ForwardingLog;
import com.klh.smsfowarder.dtc.PhoneNumber;

import java.util.ArrayList;

/**
 * Created by LyunhoKim on 16. 6. 18..
 */
public class DBHelper extends SQLiteOpenHelper {

    final String FEILD_NAME_NAME = "name";
    final String FEILD_NAME_NUMBER = "number";
    final String FEILD_NAME_ID = "_id";

    final String FEILD_NAME_MESSAGE = "message";
    final String FEILD_NAME_LOG = "log";
    final String FEILD_NAME_LOGGINGDATE = "loggingdate";

    static public final String TABLE_NAME_SENDER = "TARGET_PHONE_NUMBER";
    static public final String TABLE_NAME_RECEIVER = "SOURCE_PHONE_NUMBER";
    static public final String TABLE_NAME_LOG = "FORWARDING_LOG";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TARGET_PHONE_NUMBER(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, number TEXT);");
        db.execSQL("CREATE TABLE SOURCE_PHONE_NUMBER(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, number TEXT);");
        db.execSQL("CREATE TABLE FORWARDING_LOG(_id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, log TEXT, loggingdate TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String query) {
        SQLiteDatabase db = getWritableDatabase();
        String q = "INSERT INTO TARGET_PHONE_NUMBER VALUES(null, '김련호', '010-4045-5447');";
        db.execSQL(q);
        q = "INSERT INTO SOURCE_PHONE_NUMBER VALUES(null, '김련호', '010-4045-5447');";
        db.execSQL(q);

    }

    public void deleteData(String query) {

    }

    public String getData(String table) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM TARGET_PHONE_NUMBER;", null);

        String result = new String();

        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(FEILD_NAME_ID));
            String name = c.getString(c.getColumnIndex(FEILD_NAME_NAME));
            String number = c.getString(c.getColumnIndex(FEILD_NAME_NUMBER));

            result = new String(id + name + number);

        }

        c.close();
        db.close();

        return result;

    }

    public ArrayList<PhoneNumber> getPhoneNumbers(String tableName) {
        ArrayList<PhoneNumber> result = new ArrayList<PhoneNumber>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ tableName + ";", null);



        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(FEILD_NAME_ID));
            String name = c.getString(c.getColumnIndex(FEILD_NAME_NAME));
            String number = c.getString(c.getColumnIndex(FEILD_NAME_NUMBER));

            result.add(new PhoneNumber(id, name, number));

        }

        c.close();
        db.close();



        return result;
    }

    public ArrayList<ForwardingLog> getForwardingLog() {
        ArrayList<ForwardingLog> result = new ArrayList<ForwardingLog>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ TABLE_NAME_LOG + " ORDER BY _id DESC;", null);



        while(c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(FEILD_NAME_ID));
            String msg = c.getString(c.getColumnIndex(FEILD_NAME_MESSAGE));
            String log = c.getString(c.getColumnIndex(FEILD_NAME_LOG));
            String date = c.getString(c.getColumnIndex(FEILD_NAME_LOGGINGDATE));

            result.add(new ForwardingLog(id, msg, log, date));

        }

        c.close();
        db.close();

        return result;
    }

    public void insertPhoneNumber(String table, String name, String phoneNumber) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(FEILD_NAME_NAME, name);
            cv.put(FEILD_NAME_NUMBER, phoneNumber);
            db.insert(table, null, cv);
            db.close();
        } catch (Exception e) {
            Log.e("SMS", e.getMessage());
        }
    }

    public void insertLog(String msg, String log, String date) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(FEILD_NAME_MESSAGE, msg);
            cv.put(FEILD_NAME_LOG, log);
            cv.put(FEILD_NAME_LOGGINGDATE, date);
            db.insert(TABLE_NAME_LOG, null, cv);
            db.close();
        } catch (Exception e) {
            Log.e("SMS", e.getMessage());
        }
    }

    public void deletePhoneNumber(int id, String table) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM " + table + " WHERE _id=" + id + ";" );
//            int result = db.delete(table, "_id=?", new String [] {String.valueOf(id)});
            db.close();
        } catch (Exception e) {
            Log.e("SMS", e.getMessage());
        }
    }



    public void updatePhoneNumber(int id, String table, String name, String number) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("number", number);
            db.update(table, cv, "_id=?", new String[] {String.valueOf(id)});
            db.close();
        } catch (Exception e) {
            Log.e("SMS", e.getMessage());
        }
    }
}
