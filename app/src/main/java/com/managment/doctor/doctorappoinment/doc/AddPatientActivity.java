package com.managment.doctor.doctorappoinment.doc;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.Utils;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;
import com.managment.doctor.doctorappoinment.loginregister.sql.DatabaseHelper;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.managment.doctor.doctorappoinment.Utils.PATIENTKEY;

public class AddPatientActivity extends AppCompatActivity {

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @BindView(R.id.etPName)
    EditText etName;

    @BindView(R.id.etPMail)
    EditText etMail;

    @BindView(R.id.etPContact)
    EditText etContact;

    @BindView(R.id.etPCidty)
    EditText etCity;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.swGender)
    Switch swGender;

    @BindView(R.id.spPAddressIllType)
    Spinner spPAddressIllType;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    private String illness = "";
    private Patient patient;
    String list[];
    private String date = "";
    private Patient d;
    private String doctorName;
    private DatabaseReference mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        ButterKnife.bind(this);
        doctorName = FirebaseAuth.getInstance().getUid();
        tvTitle.setText("Add Patient");
        list = getResources().getStringArray(R.array.illness);
        setSpinner();
        tvDate.setText("Appointment date : " + Utils.getTodayDate());
        if (getIntent().getExtras() != null) {
            patient = (Patient) getIntent().getExtras().getSerializable("patient");
            btnSubmit.setText("Update Details");
            tvTitle.setText("Update Patient Details");
            tvDate.setText("Appointment date : " + patient.getOppDate());
            etName.setText(patient.getName());
            etCity.setText(patient.getCity());
            etContact.setText(patient.getContact());
            swGender.setChecked(patient.getGender().equalsIgnoreCase("Female"));
            etMail.setText(patient.getEmail());
            date = patient.getOppDate();
            for (int i = 0; i < list.length; i++) {
                if (list[i].equalsIgnoreCase(patient.getIllness())) {
                    spPAddressIllType.setSelection(i);
                    return;
                }
            }
        }
    }

    private void setSpinner() {
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPAddressIllType.setAdapter(aa);
        spPAddressIllType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                illness = list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.tvDate)
    public void selectedDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date = day + "/" + month + "/" + year;
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "/" + month + "/" + year;
                tvDate.setText("Appointment date : " + date);

            }
        }, year, month, day).show();

    }

    @OnClick(R.id.ivBack)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @OnClick(R.id.btnSubmit)
    public void submitDetails() {
        if (progressbar.getVisibility() == View.VISIBLE) return;
        if (isEmpty(etName) || isEmpty(etMail) || isEmpty(etContact) || isEmpty(etCity))
            Toast.makeText(this, "Fill all field", Toast.LENGTH_SHORT).show();
        else if (illness.isEmpty()) {
            Toast.makeText(this, "select illness type", Toast.LENGTH_SHORT).show();
        } else if (date.isEmpty())
            Toast.makeText(this, "select Appoinment date", Toast.LENGTH_SHORT).show();
        else {
            d = new Patient();
            d.setName(getString(etName));
            d.setContact(getString(etContact));
            d.setEmail(getString(etMail));
            d.setCity(getString(etCity));
            d.setGender(!swGender.isChecked() ? "Male" : "Female");
            d.setDoctor(doctorName);
            d.setIllness(illness);
            d.setOppDate(date);
            if (patient == null) {
                d.setRegDate(Utils.getTodayDate());
                if (DatabaseHelper.getInstance(this).addPatient(d))
                    performPatientDetails(false);
            } else {
                d.setId(patient.getId());
                d.setRegDate(patient.getRegDate());
                d.setFireBaseKey(patient.getKey());
                performPatientDetails(true);
            }
        }
    }


    private String getString(EditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void performPatientDetails(boolean update) {
        if (FirebaseAuth.getInstance() == null || FirebaseAuth.getInstance().getUid() == null)
            return;
        mFirebaseInstance = FirebaseDatabase.getInstance().getReference(PATIENTKEY)
                .child(FirebaseAuth.getInstance().getUid());
        String userId = "";
        if (!update) {
            userId = mFirebaseInstance.push().getKey();
        } else {
            userId = d.getKey();
        }
        mFirebaseInstance.child(userId).setValue(d).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (DatabaseHelper.getInstance(AddPatientActivity.this).addPatient(d)) {
                    finish();
                } else Toast.makeText(AddPatientActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPatientActivity.this, "Someting went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
