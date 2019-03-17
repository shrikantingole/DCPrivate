package com.managment.doctor.doctorappoinment.loginregister.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.Utils;
import com.managment.doctor.doctorappoinment.loginregister.model.Patient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class UpcomingEventAdapter extends RecyclerView.Adapter<UpcomingEventAdapter.UserViewHolder> {

    private List<Patient> listPatient;

    public UpcomingEventAdapter(List<Patient> listPatient) {
        this.listPatient = listPatient;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_upcoming_event, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.tvName.setText(listPatient.get(position).getName());
        holder.tvUCDate.setText(listPatient.get(position).getOppDate());

        Calendar mDate = Calendar.getInstance(); // just for example
        mDate.setTime(Utils.getDateString(listPatient.get(position).getOppDate()));
        if (DateUtils.isToday(mDate.getTimeInMillis())) {
            holder.tvUCDay.setText("Today");
        } else {
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
            holder.tvUCDay.setText(String.valueOf(simpleDateformat.format(Utils.getDateString(listPatient.get(position).getOppDate()))));
        }

    }

    @Override
    public int getItemCount() {
        Log.v(UpcomingEventAdapter.class.getSimpleName(), "" + listPatient.size());
        return listPatient.size();
    }


    /**
     * ViewHolder class
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvUCDate;
        public TextView tvUCDay;

        public UserViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvPatientName);
            tvUCDate = view.findViewById(R.id.tvPatientDate);
            tvUCDay = view.findViewById(R.id.tvPatientAppDay);
        }
    }


}
