package com.managment.doctor.doctorappoinment.doc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.Utils;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;
import com.managment.doctor.doctorappoinment.loginregister.sql.DatabaseHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPatientActivity extends AppCompatActivity {

    @BindView(R.id.etPName)
    EditText etName;

    @BindView(R.id.etPMail)
    EditText etMail;

    @BindView(R.id.etPContact)
    EditText etContact;

    @BindView(R.id.etPCidty)
    EditText etCity;

    @BindView(R.id.swGender)
    Switch swGender;

    @BindView(R.id.spPAddressIllType)
    Spinner spPAddressIllType;

    private String illness="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        ButterKnife.bind(this);
        setSpinner();
    }

    private  void setSpinner()
    {
        final String list[]=getResources().getStringArray(R.array.illness);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPAddressIllType.setAdapter(aa);
        spPAddressIllType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                illness=list[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.tvDate)
    public void selectedDate()
    {

    }

    @OnClick(R.id.btnSubmit)
    public void submitDetails()
    {

        if (isEmpty(etName) || isEmpty(etMail) || isEmpty(etContact) || isEmpty(etCity))
            Toast.makeText(this, "Fill all field", Toast.LENGTH_SHORT).show();
        else if (illness.isEmpty())
        {
            Toast.makeText(this, "select illness type", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Patient d=new Patient();
            d.setName(getString(etName));
            d.setContact(getString(etContact));
            d.setEmail(getString(etMail));
            d.setCity(getString(etCity));
            d.setGender(swGender.isSelected()?"Male":"Female");
            d.setDoctor(getString(etMail));
            d.setIllness(illness);
            d.setOppDate(getString(etMail));
            d.setRegDate(Utils.getTodayDate());
            DatabaseHelper.getInstance(this).addPatient(d);
        }
    }



    private String getString(EditText editText)
    {
        return editText.getText().toString().trim();
    }

    private boolean isEmpty(EditText editText)
    {
        return editText.getText().toString().trim().isEmpty();
    }
}
