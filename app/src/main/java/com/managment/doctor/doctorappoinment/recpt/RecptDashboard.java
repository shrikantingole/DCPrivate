package com.managment.doctor.doctorappoinment.recpt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.Utils;
import com.managment.doctor.doctorappoinment.doc.AddPatientActivity;
import com.managment.doctor.doctorappoinment.doc.PatientListActivity;
import com.managment.doctor.doctorappoinment.loginregister.SharePref;
import com.managment.doctor.doctorappoinment.loginregister.activities.LoginActivity;
import com.managment.doctor.doctorappoinment.loginregister.adapters.UpcomingEventAdapter;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.managment.doctor.doctorappoinment.Utils.PATIENTKEY;
import static com.managment.doctor.doctorappoinment.Utils.recptContact;
import static com.managment.doctor.doctorappoinment.Utils.recptDocKey;
import static com.managment.doctor.doctorappoinment.Utils.recptMail;
import static com.managment.doctor.doctorappoinment.Utils.recptName;

public class RecptDashboard extends AppCompatActivity {
    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.ivRight)
    ImageView ivRight;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    List<Patient> patientList;
    private UpcomingEventAdapter adapter;
    private String name;
    private String key;
    private String mail;
    private String contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recpt_dashboard);
        ButterKnife.bind(this);

        name = SharePref.getInstance(getApplicationContext()).getSharedPreferenceString(recptName, "");
        key = SharePref.getInstance(getApplicationContext()).getSharedPreferenceString(recptDocKey, "");
        mail = SharePref.getInstance(getApplicationContext()).getSharedPreferenceString(recptMail, "");
        contact = SharePref.getInstance(getApplicationContext()).getSharedPreferenceString(recptContact, "");

        ivRight.setImageDrawable(getResources().getDrawable(R.drawable.ic_reuse));
        tvTitle.setText("DashBoard");
        ivBack.setVisibility(View.GONE);
        initRecyclerView();
        getAllPatientList();
    }

    @OnClick(R.id.cvAddPatient)
    public void cvAddPatient() {
        startActivity(new Intent(this, AddPatientActivity.class));
    }

    @OnClick(R.id.cvViewPatient)
    public void viewPatient() {
        startActivity(new Intent(this, PatientListActivity.class));

    }

    private void initRecyclerView() {
        patientList = new ArrayList<>();
        adapter = new UpcomingEventAdapter(patientList);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (key.isEmpty()) {
            SharePref.getInstance(getApplicationContext()).clearData();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        } else
            getAllPatientList();
    }

    private void getAllPatientList() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference(PATIENTKEY).child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Count ", "" + dataSnapshot.getChildrenCount());
                        patientList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Patient post = postSnapshot.getValue(Patient.class);
                            post.setFireBaseKey(postSnapshot.getKey());
                            patientList.add(post);
                        }
                        if (patientList.size() == 0) return;
                        sortData();
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void sortData() {
        Collections.sort(patientList, new Comparator<Patient>() {
            public int compare(Patient o1, Patient o2) {
                return Utils.getDateString(o1.getOppDate()).compareTo(Utils.getDateString(o2.getOppDate()));
            }
        });
    }

    @OnClick(R.id.ivRight)
    public void signout() {
        SharePref.getInstance(getApplicationContext()).clearData();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
