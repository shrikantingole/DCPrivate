package com.managment.doctor.doctorappoinment.imageauth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.doc.DashBoard;
import com.managment.doctor.doctorappoinment.loginregister.SharePref;
import com.managment.doctor.doctorappoinment.utils.PictUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageAuthActivity extends AppCompatActivity {

    @BindView(R.id.simpleGridView)
    GridView simpleGrid;
    String password = "";

    @BindView(R.id.btnSelectImage)
    Button btnSelect;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @BindView(R.id.btnSaveImage)
    Button btnSaveImage;

    ArrayList<Bitmap> logos;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    List<Integer> array;
    boolean login = false;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_auth);
        ButterKnife.bind(this);
        simpleGrid = findViewById(R.id.simpleGridView); // init GridView
        if (getIntent().getExtras() != null)
            login = getIntent().getExtras().getBoolean("login");
        bitmap = PictUtil.loadImageFromStorage(SharePref.getInstance(this).getSharedPreferenceString("path", ""));
        if (bitmap != null) {
            logos = splitBitmap(bitmap);
            updateAdapter();
        }
    }


    private void updateAdapter() {
//        List<Bitmap> names=logos;
//        Collections.shuffle(names);
        array = new ArrayList<>();
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos, new OnClickListner() {
            @Override
            public void onItemClick(int pos, Bitmap b) {
                if (array.size() < 3)
                    array.add(pos);
                else
                    Toast.makeText(ImageAuthActivity.this, "three box only", Toast.LENGTH_SHORT).show();
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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting image to ImageView
                logos = splitBitmap(bitmap);
                updateAdapter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btnSaveImage)
    public void saveImage() {
        if (bitmap == null) {
            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (array.size() < 3) {
            Toast.makeText(this, "Select Image Password", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Integer a : array)
            password = password + "," + a;
        new LongOperation().execute();

    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SharePref.getInstance(getApplicationContext()).setSharedPreferenceString("pass", password);
            String path = PictUtil.saveToInternalStorage(bitmap, getApplicationContext());
            SharePref.getInstance(getApplicationContext()).setSharedPreferenceString("path", path);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressbar.setVisibility(View.GONE);
            Toast.makeText(ImageAuthActivity.this, "Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish();
        }

        @Override
        protected void onPreExecute() {
            progressbar.setVisibility(View.VISIBLE);
        }

    }
}