package com.managment.doctor.doctorappoinment.doc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.loginregister.adapters.PatientRecyclerAdapter;
import com.managment.doctor.doctorappoinment.loginregister.adapters.UsersRecyclerAdapter;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;
import com.managment.doctor.doctorappoinment.loginregister.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class PatientListActivity extends AppCompatActivity {

    private AppCompatActivity activity = PatientListActivity.this;
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    private ArrayList<Patient> patients=new ArrayList<>();
    private PatientRecyclerAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        ButterKnife.bind(this);
        initViews();
        initObjects();

    }

    private void initViews() {
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUsers);
    }

    private void initObjects() {
        patients = new ArrayList<>();
        patients=DatabaseHelper.getInstance(PatientListActivity.this).getAllPatient();
        adapter = new PatientRecyclerAdapter(patients,
                new PatientRecyclerAdapter.OnItemClickListner() {
            @Override
            public void onClick(int position) {
                patients.get(position);
                Toast.makeText(activity, ""+patients.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(activity);



    }


}
