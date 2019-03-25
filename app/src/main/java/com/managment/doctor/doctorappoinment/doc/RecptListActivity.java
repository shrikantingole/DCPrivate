package com.managment.doctor.doctorappoinment.doc;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.Utils;
import com.managment.doctor.doctorappoinment.loginregister.adapters.RecptRecyclerAdapter;
import com.managment.doctor.doctorappoinment.loginregister.model.Recpt;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.managment.doctor.doctorappoinment.Utils.PATIENTKEY;
import static com.managment.doctor.doctorappoinment.Utils.RECPTEY;

public class RecptListActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivRight)
    ImageView ivRefresh;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private AppCompatActivity activity = RecptListActivity.this;
    private RecyclerView recyclerViewUsers;
    private ArrayList<Recpt> arrayList = new ArrayList<>();
    private RecptRecyclerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recpt_list);
        ButterKnife.bind(this);
        ivRefresh.setImageDrawable(getResources().getDrawable(R.drawable.ic_reuse));
        progressBar.setVisibility(View.VISIBLE);
        tvTitle.setText("View Receptionist");
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
        arrayList = new ArrayList<>();
//        arrayList.addAll(DatabaseHelper.getInstance(PatientListActivity.this).getAllPatient(getApplicationContext()));
        adapter = new RecptRecyclerAdapter(arrayList,
                new RecptRecyclerAdapter.OnItemClickListner() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(activity, "" + arrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDelete(int adapterPosition) {
//                        DatabaseHelper.getInstance(PatientListActivity.this).deletePatient(arrayList.get(adapterPosition));
                        FirebaseDatabase.getInstance().getReference(PATIENTKEY).child(FirebaseAuth.getInstance().getUid())
                                .child(arrayList.get(adapterPosition).getKey()).removeValue();
                        Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show();
                        getAllPatientList();
                    }

                    @Override
                    public void onEdit(int adapterPosition) {
                    }
                });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(adapter);

    }


    private void update() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }
    }


    private void getAllPatientList() {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(Utils.url);
        databaseReference.child(RECPTEY).child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Count ", "" + dataSnapshot.getChildrenCount());
                        arrayList.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Recpt post = postSnapshot.getValue(Recpt.class);
                            post.setFireBaseKey(postSnapshot.getKey());
                            arrayList.add(post);
                        }
                        update();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick(R.id.ivRight)
    public void onRefresh() {
        arrayList.clear();
        getAllPatientList();
        progressBar.setVisibility(View.VISIBLE);

    }
}
