package com.klh.smsfowarder.dtc;

/**
 * Created by LyunhoKim on 16. 6. 18..
 */
public class PhoneNumber {

    int id;
    String name;
    String number;


    public PhoneNumber(int id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
