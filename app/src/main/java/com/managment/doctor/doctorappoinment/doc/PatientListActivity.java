package com.managment.doctor.doctorappoinment.doc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.loginregister.adapters.PatientRecyclerAdapter;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;
import com.managment.doctor.doctorappoinment.loginregister.sql.DatabaseHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PatientListActivity extends AppCompatActivity {

    private AppCompatActivity activity = PatientListActivity.this;
    private RecyclerView recyclerViewUsers;
    private ArrayList<Patient> patients = new ArrayList<>();
    private PatientRecyclerAdapter adapter;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        ButterKnife.bind(this);
        tvTitle.setText("View Patient");
        initViews();
        initObjects();

    }
    @OnClick(R.id.ivBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initViews() {
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
    }

    private void initObjects() {
        patients = new ArrayList<>();
        patients.addAll(DatabaseHelper.getInstance(PatientListActivity.this).getAllPatient(getApplicationContext()));
        adapter = new PatientRecyclerAdapter(patients,
                new PatientRecyclerAdapter.OnItemClickListner() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(activity, "" + patients.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDelete(int adapterPosition) {
                        DatabaseHelper.getInstance(PatientListActivity.this).deletePatient(patients.get(adapterPosition));
                        Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                       update();
                    }

                    @Override
                    public void onEdit(int adapterPosition) {
                        startActivity(new Intent(PatientListActivity.this,AddPatientActivity.class)
                        .putExtra("patient",patients.get(adapterPosition)));
                    }
                });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }
    private  void update()
    {
        if (adapter!=null) {
            patients.clear();
            patients.addAll(DatabaseHelper.getInstance(PatientListActivity.this).getAllPatient(getApplicationContext()));
            adapter.notifyDataSetChanged();
        }
    }

}
