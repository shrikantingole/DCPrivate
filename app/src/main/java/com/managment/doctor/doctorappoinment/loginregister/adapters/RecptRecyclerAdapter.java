package com.managment.doctor.doctorappoinment.loginregister.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.managment.doctor.doctorappoinment.R;
import com.managment.doctor.doctorappoinment.loginregister.model.Recpt;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RecptRecyclerAdapter extends RecyclerView.Adapter<RecptRecyclerAdapter.PatientViewHolder> {

    private ArrayList<Recpt> arrayList;
    private OnItemClickListner callback;

    public RecptRecyclerAdapter(ArrayList<Recpt> listDoctors, OnItemClickListner callback) {
        this.arrayList = listDoctors;
        this.callback = callback;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recpt_recycler, parent, false);

        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        holder.textViewName.setText(arrayList.get(position).getName());
        holder.tvEmail.setText(arrayList.get(position).getEmail());
        holder.tvcontact.setText(arrayList.get(position).getContact());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public interface OnItemClickListner {
        void onClick(int position);

        void onDelete(int adapterPosition);

        void onEdit(int adapterPosition);
    }

    /**
     * ViewHolder class
     */
    public class PatientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvname)
        TextView textViewName;

        @BindView(R.id.tvemail)
        TextView tvEmail;

        @BindView(R.id.tvcontact)
        TextView tvcontact;

        public PatientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) callback.onClick(getAdapterPosition());
                }
            });
        }

        @OnClick(R.id.ivDelete)
        public void onDelete() {
            if (callback != null)
                callback.onDelete(getAdapterPosition());
        }

        @OnClick(R.id.ivEdit)
        public void onEdit() {
            if (callback != null)
                callback.onEdit(getAdapterPosition());
        }
    }
}
