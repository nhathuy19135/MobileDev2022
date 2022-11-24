package com.example.mobiledev2022.database.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobiledev2022.R;
import java.util.List;

public class DoctorRecyclerViewAdapter extends RecyclerView.Adapter<DoctorRecyclerViewAdapter.ViewHolder> {
    private List doctorList;

    private DoctorMainActivity.DoctorRecyclerViewOnItemClickListener doctorRecyclerViewOnItemClickListener;

    DoctorRecyclerViewAdapter(List doctorList, DoctorMainActivity.DoctorRecyclerViewOnItemClickListener doctorRecyclerViewOnItemClickListener){
        this.doctorList = doctorList;
        this.doctorRecyclerViewOnItemClickListener = doctorRecyclerViewOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item_cardview, parent, false);
        itemView.setOnClickListener(doctorRecyclerViewOnItemClickListener);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DoctorRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Doctor doctor = (Doctor) doctorList.get(position);

        // Populate user interface
        viewHolder.fullNameTextView.setText(doctor.getFirstName() + " " + doctor.getLastName());
        viewHolder.emailTextView.setText(doctor.getEmail());
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    /** ViewHolder pattern: Inner class needed to keep the references between widgets and data to improve the performance */
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullNameTextView;
        TextView emailTextView;

        ViewHolder(View itemView) {
            super(itemView);

            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }
    }

}