package com.managment.doctor.doctorappoinment.loginregister.model;


public class Patient {

    private int id;
    private String name;
    private String email;
    private String contact;
    private String city;
    private String gender;
    private String illness;
    private String oppDate;
    private String regDate;
    private String doctor;

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getIllness() {
        return illness;
    }
    public void setIllness(String illness) {
        this.illness = illness;
    }
    public String getOppDate() {
        return oppDate;
    }
    public void setOppDate(String oppDate) {
        this.oppDate = oppDate;
    }
    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    public String getDoctor() {
        return doctor;
    }
    public void setDoctor(String doctor) {
        this.doctor = doctor;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public static final String TABLE_PATIENT = "patient";

    public static final String COLUMN_PATIENT_ID = "patient_id";
    public static final String COLUMN_PATIENT_NAME = "patient_name";
    public static final String COLUMN_PATIENT_EMAIL = "patient_email";

    public static final String COLUMN_PATIENT_CONTACT = "contact";
    public static final String COLUMN_PATIENT_CITY = "city";
    public static final String COLUMN_PATIENT_GENDER = "gender";
    public static final String COLUMN_PATIENT_ILLNESS = "illness";
    public static final String COLUMN_PATIENT_OPPDATE = "oppdate";
    public static final String COLUMN_PATIENT_DOCTOR = "doctor";
    public static final String COLUMN_PATIENT_REGDATE = "regdate";

    public static String CREATE_PATIENT_TABLE = "CREATE TABLE " + TABLE_PATIENT + "("
            + COLUMN_PATIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PATIENT_NAME + " TEXT,"
            + COLUMN_PATIENT_EMAIL + " TEXT,"
            + COLUMN_PATIENT_CONTACT + " TEXT,"
            + COLUMN_PATIENT_CITY + " TEXT,"
            + COLUMN_PATIENT_GENDER + " TEXT,"
            + COLUMN_PATIENT_ILLNESS + " TEXT,"
            + COLUMN_PATIENT_OPPDATE + " TEXT,"
            + COLUMN_PATIENT_DOCTOR + " TEXT,"
            + COLUMN_PATIENT_REGDATE + " TEXT"
            + ")";

    // drop table sql query
    public static String DROP_PATIENT_TABLE = "DROP TABLE IF EXISTS " + TABLE_PATIENT;

}
