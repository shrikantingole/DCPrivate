package com.managment.doctor.doctorappoinment.doc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.loginregister.adapters.PatientRecyclerAdapter;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.managment.doctor.doctorappoinment.Utils.PATIENTKEY;


public class PatientListActivity extends AppCompatActivity {

    private AppCompatActivity activity = PatientListActivity.this;
    private RecyclerView recyclerViewUsers;
    private ArrayList<Patient> patients = new ArrayList<>();
    private PatientRecyclerAdapter adapter;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivRight)
    ImageView ivRefresh;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        ButterKnife.bind(this);
        ivRefresh.setImageDrawable(getResources().getDrawable(R.drawable.ic_reuse));
        progressBar.setVisibility(View.VISIBLE);
        tvTitle.setText("View Patient");
        initViews();
        initObjects();
        getAllPatientList();


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
//        patients.addAll(DatabaseHelper.getInstance(PatientListActivity.this).getAllPatient(getApplicationContext()));
        adapter = new PatientRecyclerAdapter(patients,
                new PatientRecyclerAdapter.OnItemClickListner() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(activity, "" + patients.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDelete(int adapterPosition) {
//                        DatabaseHelper.getInstance(PatientListActivity.this).deletePatient(patients.get(adapterPosition));
                        FirebaseDatabase.getInstance().getReference(PATIENTKEY).child(FirebaseAuth.getInstance().getUid())
                                .child(patients.get(adapterPosition).getKey()).removeValue();
                        Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                        getAllPatientList();
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


    private  void update()
    {
        if (adapter!=null)
        {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }


    private void getAllPatientList()
    {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference(PATIENTKEY).child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Count ", "" + dataSnapshot.getChildrenCount());
                patients.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Patient post = postSnapshot.getValue(Patient.class);
                    post.setFireBaseKey(postSnapshot.getKey());
                    patients.add(post);
                }
                update();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.ivRight)
    public void onRefresh() {
        patients.clear();
        getAllPatientList();
        progressBar.setVisibility(View.VISIBLE);

    }
}
