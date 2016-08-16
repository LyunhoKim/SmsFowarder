package com.klh.smsfowarder.dtc;

/**
 * Created by LyunhoKim on 16. 6. 18..
 */
public class ForwardingLog {
    int id;
    String message;
    String log;
    String date;

    public int getId() {
        return id;
    }

    public ForwardingLog(int id, String message, String log, String date) {
        this.id = id;
        this.message = message;
        this.log = log;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
