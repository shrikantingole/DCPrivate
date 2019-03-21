package com.managment.doctor.doctorappoinment.loginregister.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Doctor implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private String key;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static final String TABLE_DOCTOR = "doctor";

    public static final String COLUMN_DOCTOR_ID = "doctor_id";
    public static final String COLUMN_DOCTOR_NAME = "doctor_name";
    public static final String COLUMN_DOCTOR_EMAIL = "doctor_email";
    public static final String COLUMN_DOCTOR_PASSWORD = "doctor_password";

    public static String CREATE_DOCTOR_TABLE = "CREATE TABLE " + TABLE_DOCTOR + "("
            + COLUMN_DOCTOR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DOCTOR_NAME + " TEXT,"
            + COLUMN_DOCTOR_EMAIL + " TEXT,"
            + COLUMN_DOCTOR_PASSWORD + " TEXT" + ")";

    // drop table sql query
    public static String DROP_DOCTOR_TABLE = "DROP TABLE IF EXISTS " + TABLE_DOCTOR;
    private int x;
    private int y;

    public void setFireBaseKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean xy(int x, int y) {
        boolean xFlag = false, yFlag = false;
        if ((this.x + 50) > x && (this.x - 50) < x) {
            xFlag = true;
        }

        if ((this.y + 50) > y && (this.y - 50) < y) {
            yFlag = true;
        }
        if (xFlag) return yFlag;
        return false;
    }
}
