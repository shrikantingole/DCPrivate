package com.managment.doctor.doctorappoinment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils
{
    public static final String DOCTORKEY="DoctorsList";
    public static final String PATIENTKEY="PatientsList";
    public static String getTodayDate()
    {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();

        DateFormat date = new SimpleDateFormat("dd/MM/yyy");

        String localTime = date.format(currentLocalTime);
        return localTime;
    }
    public static void setAlertDialog(final Activity activity , final Callback callback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(activity,"you choose yes action for alertbox",
                                Toast.LENGTH_SHORT).show();
                        callback.onDeleted();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        callback.onCancel();
                        dialog.cancel();
                        Toast.makeText(activity,"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("AlertDialogExample");
        alert.show();

        }
        interface Callback
        {
            void onDeleted();
            void onCancel();
        }

    public static Date getDateString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }
        return d;
    }

    public static String url = "https://doctor-app-84650.firebaseio.com/";
}
