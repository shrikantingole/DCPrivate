package com.managment.doctor.doctorappoinment.imageauth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.doc.DashBoard;
import com.managment.doctor.doctorappoinment.loginregister.SharePref;
import com.managment.doctor.doctorappoinment.loginregister.activities.LoginActivity;
import com.managment.doctor.doctorappoinment.utils.PictUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginImageActivity extends AppCompatActivity {
    @BindView(R.id.simpleGridView)
    GridView simpleGrid;
    String tempPass = "";

    ArrayList<Bitmap> logos;
    Uri filePath;
    List<Integer> array;
    boolean login = false;
    private String pass;
    private int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_image);
        ButterKnife.bind(this);
        pass = SharePref.getInstance(this).getSharedPreferenceString("pass", "");

        if (pass.isEmpty()) {
            startActivity(new Intent(this, ImageAuthActivity.class));
            finish();
        }
        simpleGrid = findViewById(R.id.simpleGridView); // init GridView

        Bitmap imageFile = PictUtil.loadImageFromStorage(SharePref.getInstance(this).getSharedPreferenceString("path", ""));
        if (imageFile != null) {
            logos = splitBitmap(imageFile);
            updateAdapter();

        }


    }

    private void updateAdapter() {
        if (logos == null) return;
        ArrayList<Bitmap> names = new ArrayList<>(logos);
        Collections.shuffle(names);
        array = new ArrayList<>();
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), names, new OnClickListner() {
            @Override
            public void onItemClick(int pos, Bitmap b) {
                if (pass.contains(String.valueOf(logos.indexOf(b)))) {
                    if (!tempPass.contains(String.valueOf(pos))) {
                        tempPass = pos + ",";
                        a++;
                        if (a == 3) {
                            Toast.makeText(LoginImageActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DashBoard.class));
                            finish();
                        }
                    } else
                        Toast.makeText(LoginImageActivity.this, "Doubple pressed", Toast.LENGTH_SHORT).show();
                } else {
                    a = 0;
                    Toast.makeText(LoginImageActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        simpleGrid.setAdapter(customAdapter);
    }

    public ArrayList<Bitmap> splitBitmap(Bitmap picture) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, 240, 240, true);
        ArrayList<Bitmap> imgs = new ArrayList<>();
        imgs.add(0, Bitmap.createBitmap(scaledBitmap, 0, 0, 80, 80));
        imgs.add(1, Bitmap.createBitmap(scaledBitmap, 80, 0, 80, 80));
        imgs.add(2, Bitmap.createBitmap(scaledBitmap, 160, 0, 80, 80));
        imgs.add(3, Bitmap.createBitmap(scaledBitmap, 0, 80, 80, 80));
        imgs.add(4, Bitmap.createBitmap(scaledBitmap, 80, 80, 80, 80));
        imgs.add(5, Bitmap.createBitmap(scaledBitmap, 160, 80, 80, 80));
        imgs.add(6, Bitmap.createBitmap(scaledBitmap, 0, 160, 80, 80));
        imgs.add(7, Bitmap.createBitmap(scaledBitmap, 80, 160, 80, 80));
        imgs.add(8, Bitmap.createBitmap(scaledBitmap, 160, 160, 80, 80));
        return imgs;
    }

    //    @OnClick(R.id.btnImageLogin)
//    public void onValidateEmail() {
//        if (array.size() < 3) {
//            Toast.makeText(this, "Select box Image", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//    }
    @OnClick(R.id.btnSignout)
    public void signout() {
        FirebaseAuth.getInstance().signOut();
        SharePref.getInstance(getApplicationContext()).clearData();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}