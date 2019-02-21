package com.managment.doctor.doctorappoinment.loginregister.model;

public class Doctor {

    private int id;
    private String name;
    private String email;
    private String password;

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

}
