package com.managment.doctor.doctorappoinment.loginregister.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.doc.DashBoard;
import com.managment.doctor.doctorappoinment.loginregister.SharePref;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class ImageAuthActivity extends AppCompatActivity {
    Button chooseImg, uploadImg;
    ImageView imgView;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.tvCount)
    TextView count;
    private boolean flag = false;
    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_auth);
        ButterKnife.bind(this);
        chooseImg = findViewById(R.id.chooseImg);
        uploadImg = findViewById(R.id.uploadImg);
        imgView = findViewById(R.id.imgView);
        if (getIntent().getExtras() != null) {
            doctor = (Doctor) getIntent().getExtras().getSerializable("Doctor");
        }
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");
        pd.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
//        getPassworsPoint();
        StorageReference storageRef = storage.getReference().child(FirebaseAuth.getInstance().getUid() + ".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    pd.dismiss();
                    imgView.setVisibility(View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pd.dismiss();
                }
            });
        } catch (IOException e) {
        }

    }

    @OnClick(R.id.chooseImg)
    public void onValidateEmail() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    public void onUploadImage() {
        if (filePath != null) {
            pd.show();
            StorageReference childRef = storageRef.child(FirebaseAuth.getInstance().getUid() + ".jpg");
            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                    Toast.makeText(ImageAuthActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(ImageAuthActivity.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ImageAuthActivity.this, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting image to ImageView
                imgView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnTouch({R.id.imgView})
    public boolean onImageClick(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (!FirebaseAuth.getInstance().getUid().isEmpty()) {
            if (doctor.xy(x, y))
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
            SharePref.getInstance(getApplicationContext()).setSharedPreferenceString("user", "1");
            finish();
//                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            return false;
        }

        doctor.setX(x);
        doctor.setY(y);
        Toast.makeText(this, "Password Applied", Toast.LENGTH_SHORT).show();

        return false;
    }
    @OnClick(R.id.btnReset)
    public void onReset() {
    }

    @OnClick(R.id.uploadImg)
    public void loginByMail() {
        pd.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(doctor.getEmail(), doctor.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        } else {
                            createUser();
                        }
                    }
                });
    }

    private void createUser() {
        pd.show();
        DatabaseReference mFirebaseInstance = FirebaseDatabase.getInstance().getReference("DoctorsList");
        String userId = mFirebaseInstance.child(FirebaseAuth.getInstance().getUid()).getKey();
        mFirebaseInstance.child(userId).setValue(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                onUploadImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
            }
        });


    }

}