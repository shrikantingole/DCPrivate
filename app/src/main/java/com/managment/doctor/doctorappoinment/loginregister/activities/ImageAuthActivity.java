package com.managment.doctor.doctorappoinment.loginregister.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.doc.DashBoard;
import com.managment.doctor.doctorappoinment.loginregister.model.ImageXY;

import java.util.ArrayList;
import java.util.List;

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
    List<ImageXY> pointsList;
    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @BindView(R.id.tvCount)
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_auth);
        ButterKnife.bind(this);
        pointsList = new ArrayList<>();
        chooseImg = findViewById(R.id.chooseImg);
        uploadImg = findViewById(R.id.uploadImg);
        imgView = findViewById(R.id.imgView);

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

    }

    @OnClick(R.id.chooseImg)
    public void onValidateEmail() {
        if (FirebaseAuth.getInstance().getUid() != null) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @OnClick(R.id.uploadImg)
    public void onUploadImage() {
        if (filePath != null) {
            pd.show();
            StorageReference childRef = storageRef.child(FirebaseAuth.getInstance().getUid() + ".jpg");
            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    updatePassword();
                    pd.dismiss();
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

    private void updatePassword() {
        DatabaseReference mFirebaseInstance = FirebaseDatabase.getInstance().getReference("DoctorsList");
        String userId = mFirebaseInstance.child(FirebaseAuth.getInstance().getUid()).getKey();
        mFirebaseInstance.child(userId).child("Points").setValue(pointsList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(ImageAuthActivity.this, DashBoard.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });


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

        if (pointsList.size() < 2) {
            Log.d("touched x ", x + "");
            Log.d("touched y", y + "");

            pointsList.add(new ImageXY(x, y));
            Toast.makeText(this, x + "  " + y, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Password point limit exceed \nclick on reset button", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @OnClick(R.id.btnReset)
    public void onReset() {
        pointsList.clear();
    }
}