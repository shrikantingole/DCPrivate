package com.managment.doctor.doctorappoinment.imageauth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.managment.doctor.doctorappoinment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageAuthActivity extends AppCompatActivity {

    @BindView(R.id.simpleGridView)
    GridView simpleGrid;

    @BindView(R.id.btnSelectImage)
    Button btnSelect;

    Bitmap[] logos;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    List<Integer> array;
    boolean login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_auth);
        ButterKnife.bind(this);
        simpleGrid = findViewById(R.id.simpleGridView); // init GridView
        if (getIntent().getExtras() != null)
            login = getIntent().getExtras().getBoolean("login");

        setView();
    }

    private void setView() {
        btnSelect.setVisibility(View.GONE);
    }

    private void updateAdapter() {
        List<Bitmap> names = Arrays.asList(logos);
        Collections.shuffle(names);
        array = new ArrayList<>();
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos);
        simpleGrid.setAdapter(customAdapter);
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (array.size() < 3)
                    array.add(position);
                else
                    Toast.makeText(ImageAuthActivity.this, "three box only", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Bitmap[] splitBitmap(Bitmap picture) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(picture, 240, 240, true);
        Bitmap[] imgs = new Bitmap[9];
        imgs[0] = Bitmap.createBitmap(scaledBitmap, 0, 0, 80, 80);
        imgs[1] = Bitmap.createBitmap(scaledBitmap, 80, 0, 80, 80);
        imgs[2] = Bitmap.createBitmap(scaledBitmap, 160, 0, 80, 80);
        imgs[3] = Bitmap.createBitmap(scaledBitmap, 0, 80, 80, 80);
        imgs[4] = Bitmap.createBitmap(scaledBitmap, 80, 80, 80, 80);
        imgs[5] = Bitmap.createBitmap(scaledBitmap, 160, 80, 80, 80);
        imgs[6] = Bitmap.createBitmap(scaledBitmap, 0, 160, 80, 80);
        imgs[7] = Bitmap.createBitmap(scaledBitmap, 80, 160, 80, 80);
        imgs[8] = Bitmap.createBitmap(scaledBitmap, 160, 160, 80, 80);
        return imgs;
    }

    @OnClick(R.id.btnSelectImage)
    public void onValidateEmail() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
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
                logos = splitBitmap(bitmap);
                updateAdapter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}