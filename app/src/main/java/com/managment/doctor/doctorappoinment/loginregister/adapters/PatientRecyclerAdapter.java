package com.managment.doctor.doctorappoinment.loginregister.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.loginregister.model.Doctor;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.PatientViewHolder> {

    private ArrayList<Patient> patientList;
    private OnItemClickListner callback;
    public PatientRecyclerAdapter(ArrayList<Patient> listDoctors,OnItemClickListner callback ) {
        this.patientList = listDoctors;
        this.callback=callback;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_recycler, parent, false);

        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        holder.textViewName.setText(patientList.get(position).getName());
        holder.tvcity.setText(patientList.get(position).getCity());
        holder.tvEmail.setText(patientList.get(position).getEmail());
        holder.tvappdate.setText(patientList.get(position).getOppDate());
        holder.tvcontact.setText(patientList.get(position).getContact());
        holder.tvgender.setText(patientList.get(position).getGender());
        holder.tvillness.setText(patientList.get(position).getIllness());
        holder.tvregdate.setText(patientList.get(position).getRegDate());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }


    /**
     * ViewHolder class
     */
    public class PatientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvname)
        TextView textViewName;

        @BindView(R.id.tvcity)
        TextView tvcity;

        @BindView(R.id.tvemail)
        TextView tvEmail;

        @BindView(R.id.tvgender)
        TextView tvgender;

        @BindView(R.id.tvappdate)
        TextView tvappdate;

        @BindView(R.id.tvcontact)
        TextView tvcontact;

        @BindView(R.id.tvillness)
        TextView tvillness;

        @BindView(R.id.tvregdate)
        TextView tvregdate;

        public PatientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(callback != null) callback.onClick(getAdapterPosition());
                }
            });
        }
        @OnClick(R.id.ivDelete)
        public void onDelete()
        {
            if(callback != null)
                callback.onDelete(getAdapterPosition());
        }
        @OnClick(R.id.ivEdit)
        public void onEdit()
        {
            if(callback != null)
                callback.onEdit(getAdapterPosition());
        }
    }


    public interface OnItemClickListner
    {
        void onClick(int position);

        void onDelete(int adapterPosition);

        void onEdit(int adapterPosition);
    }
}
