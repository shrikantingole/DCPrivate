package com.managment.doctor.doctorappoinment.loginregister.sql;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.managment.doctor.doctorappoinment.loginregister.SharePref;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;

import java.util.ArrayList;
import java.util.List;
import static com.managment.doctor.doctorappoinment.loginregister.model.Doctor.COLUMN_DOCTOR_EMAIL;
import static com.managment.doctor.doctorappoinment.loginregister.model.Doctor.COLUMN_DOCTOR_ID;
import static com.managment.doctor.doctorappoinment.loginregister.model.Doctor.COLUMN_DOCTOR_NAME;
import static com.managment.doctor.doctorappoinment.loginregister.model.Doctor.COLUMN_DOCTOR_PASSWORD;
import static com.managment.doctor.doctorappoinment.loginregister.model.Doctor.CREATE_DOCTOR_TABLE;
import static com.managment.doctor.doctorappoinment.loginregister.model.Doctor.DROP_DOCTOR_TABLE;
import static com.managment.doctor.doctorappoinment.loginregister.model.Doctor.TABLE_DOCTOR;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_CITY;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_CONTACT;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_DOCTOR;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_EMAIL;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_GENDER;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_ID;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_ILLNESS;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_NAME;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_OPPDATE;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.COLUMN_PATIENT_REGDATE;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.CREATE_PATIENT_TABLE;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.DROP_PATIENT_TABLE;
import static com.managment.doctor.doctorappoinment.loginregister.model.Patient.TABLE_PATIENT;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper databaseHelper = null;
    private static final int DATABASE_VERSION = 1;
    private static final String DOCTORDBNAME = "DoctorManager.db";


    public DatabaseHelper(Context context) {
        super(context, DOCTORDBNAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DOCTOR_TABLE);
        db.execSQL(CREATE_PATIENT_TABLE);
    }

    public static DatabaseHelper getInstance(Activity activity) {
        if (databaseHelper == null) databaseHelper = new DatabaseHelper(activity);
        return databaseHelper;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_DOCTOR_TABLE);
        db.execSQL(DROP_PATIENT_TABLE);
        onCreate(db);

    }

    public boolean addUser(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_NAME, doctor.getName());
        values.put(COLUMN_DOCTOR_EMAIL, doctor.getEmail());
        values.put(COLUMN_DOCTOR_PASSWORD, doctor.getPassword());
        long id=db.insert(TABLE_DOCTOR, null, values);
        db.close();
        return id>0;
    }

    public List<Doctor> getAllUser() {
        String[] columns = {
                COLUMN_DOCTOR_ID,
                COLUMN_DOCTOR_EMAIL,
                COLUMN_DOCTOR_NAME,
                COLUMN_DOCTOR_PASSWORD};
        String sortOrder = COLUMN_DOCTOR_NAME + " ASC";
        List<Doctor> doctorList = new ArrayList<Doctor>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTOR, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor();
                doctor.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_ID))));
                doctor.setName(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_NAME)));
                doctor.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_EMAIL)));
                doctor.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_PASSWORD)));
                doctorList.add(doctor);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return doctorList;
    }

    public void updateUser(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCTOR_NAME, doctor.getName());
        values.put(COLUMN_DOCTOR_EMAIL, doctor.getEmail());
        values.put(COLUMN_DOCTOR_PASSWORD, doctor.getPassword());

        db.update(TABLE_DOCTOR, values, COLUMN_DOCTOR_ID + " = ?",
                new String[]{String.valueOf(doctor.getId())});
        db.close();
    }

    public void deleteUser(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete doctor record by id
        db.delete(TABLE_DOCTOR, COLUMN_DOCTOR_ID + " = ?",
                new String[]{String.valueOf(doctor.getId())});
        db.close();
    }

    public boolean checkUser(String email) {
        String[] columns = {COLUMN_DOCTOR_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DOCTOR_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_DOCTOR, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }

    public boolean checkUser(String email, String password) {

        String[] columns = {COLUMN_DOCTOR_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DOCTOR_EMAIL + " = ?" + " AND " + COLUMN_DOCTOR_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_DOCTOR, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public Doctor getDoctorDetails(String email)
    {
        ArrayList<Doctor> doctorList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DOCTOR_EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_DOCTOR, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
       doctorList=parseDoctorCursor(cursor);
        cursor.close();
        db.close();

        return doctorList.get(0);
    }
    public ArrayList<Patient> getAllPatient(Context context) {
        String selection = COLUMN_PATIENT_DOCTOR+ " = ?";
        ArrayList<Patient> list = new ArrayList<>();
        String[] selectionArgs = {SharePref.getInstance(context).getSharedPreferenceString("email","")};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PATIENT, //Table to query
                null,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order
        list=parsepatientCursor(cursor);
        cursor.close();
        db.close();
        return list;
    }
    public Patient getPatientDetails(String id)
    {
        ArrayList<Patient> list=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_PATIENT_ID + " = ?";
        String[] selectionArgs = {id};
        Cursor cursor = db.query(TABLE_DOCTOR, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        list=parsepatientCursor(cursor);
        cursor.close();
        db.close();

        return list.get(0);
    }

    public boolean addPatient(Patient patient)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_NAME, patient.getName());
        values.put(COLUMN_PATIENT_EMAIL, patient.getEmail());
        values.put(COLUMN_PATIENT_CONTACT, patient.getContact());
        values.put(COLUMN_PATIENT_CITY, patient.getCity());
        values.put(COLUMN_PATIENT_GENDER, patient.getGender());
        values.put(COLUMN_PATIENT_ILLNESS, patient.getIllness());
        values.put(COLUMN_PATIENT_DOCTOR, patient.getDoctor());
        values.put(COLUMN_PATIENT_OPPDATE, patient.getOppDate());
        values.put(COLUMN_PATIENT_REGDATE, patient.getRegDate());
        long id=db.insert(TABLE_PATIENT, null, values);
        Log.d("XXXZZZXXX", "addPatient: "+id);
        db.close();
        return id>0;
    }

    private ArrayList<Doctor> parseDoctorCursor(Cursor cursor)
    {
        ArrayList<Doctor> list=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor();
                doctor.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_ID))));
                doctor.setName(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_NAME)));
                doctor.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_EMAIL)));
                doctor.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_DOCTOR_PASSWORD)));
                list.add(doctor);
            } while (cursor.moveToNext());
        }
        return list;
    }

    private ArrayList<Patient> parsepatientCursor(Cursor cursor)
    {
        ArrayList<Patient> list=new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_ID))));
                patient.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_NAME)));
                patient.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_EMAIL)));
                patient.setGender(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_GENDER)));
                patient.setRegDate(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_REGDATE)));
                patient.setOppDate(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_OPPDATE)));
                patient.setCity(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_CITY)));
                patient.setContact(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_CONTACT)));
                patient.setIllness(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_ILLNESS)));
                list.add(patient);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void deletePatient(Patient patient)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PATIENT, COLUMN_PATIENT_ID+ " = ?", new String[]{String.valueOf(patient.getId())});
        db.close();
    }

    public boolean updatePatient(Patient d)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_NAME, d.getName());
        values.put(COLUMN_PATIENT_EMAIL, d.getEmail());
        values.put(COLUMN_PATIENT_GENDER, d.getGender());
        values.put(COLUMN_PATIENT_REGDATE, d.getRegDate());
        values.put(COLUMN_PATIENT_CITY, d.getCity());
        values.put(COLUMN_PATIENT_CONTACT, d.getContact());
        values.put(COLUMN_PATIENT_ILLNESS, d.getIllness());
        values.put(COLUMN_PATIENT_OPPDATE, d.getOppDate());

        int id=db.update(TABLE_PATIENT,values, COLUMN_PATIENT_ID+ " = ?", new String[]{String.valueOf(d.getId())});
        db.close();
        return id>0;
    }
}
