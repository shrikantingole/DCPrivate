package com.managment.doctor.doctorappoinment.manager;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;

import static com.managment.doctor.doctorappoinment.Utils.DOCTORKEY;

public class FirebaseNetworkManager {
    public void getCurrentDoctorDetails(final LoginDetailsCallback callback) {
        FirebaseDatabase.getInstance().getReference(DOCTORKEY).child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Count ", "" + dataSnapshot.getChildrenCount());
                        Doctor post = dataSnapshot.getValue(Doctor.class);
                        callback.getCurrentDoctorDetailsSuccess(post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.getCurrentDoctorDetailsFailed(databaseError.getMessage());
                    }
                });
    }

    public interface LoginDetailsCallback {
        void getCurrentDoctorDetailsSuccess(Doctor doctor);

        void getCurrentDoctorDetailsFailed(String error);
    }

}
