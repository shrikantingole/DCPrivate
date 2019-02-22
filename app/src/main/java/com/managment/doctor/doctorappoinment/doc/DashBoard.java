package com.managment.doctor.doctorappoinment.doc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.loginregister.SharePref;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;
import com.managment.doctor.doctorappoinment.loginregister.sql.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashBoard extends AppCompatActivity
{
    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        tvTitle.setText("DashBoard");
        ivBack.setVisibility(View.GONE);
        initObjects();
    }

    private void initObjects() {
        String email=SharePref.getInstance(this).getSharedPreferenceString("email","");
        Doctor d=DatabaseHelper.getInstance(this).getDoctorDetails(email);
        if (d!=null)
        textViewName.setText("Welcome "+d.getName());
    }


    @OnClick(R.id.cvAddPatient)
    public void cvAddPatient()
    {
        startActivity(new Intent(this,AddPatientActivity.class));
    }

    @OnClick(R.id.cvViewPatient)
    public void viewPatient()
    {
        startActivity(new Intent(this,PatientListActivity.class));

    }
    @OnClick(R.id.cvProfile)
    public void profile()
    {

    }
}
