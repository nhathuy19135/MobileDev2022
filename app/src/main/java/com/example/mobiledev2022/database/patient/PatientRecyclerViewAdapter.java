package com.example.mobiledev2022.database.patient;

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

public class PatientRecyclerViewAdapter extends RecyclerView.Adapter<PatientRecyclerViewAdapter.ViewHolder>{
    private List patientList;
    private PatientMainActivity.PatientRecyclerViewOnItemClickListener patientRecyclerViewOnItemClickListener;
    PatientRecyclerViewAdapter(List patientList, PatientMainActivity.PatientRecyclerViewOnItemClickListener patientRecyclerViewOnItemClickListener){
        this.patientList = patientList;
        this.patientRecyclerViewOnItemClickListener = patientRecyclerViewOnItemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_detail_cardview, parent, false);
        itemView.setOnClickListener(patientRecyclerViewOnItemClickListener);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PatientRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Patient patient = (Patient) patientList.get(position);
        viewHolder.fullNameTextView.setText(patient.getFirstName() + " " + patient.getLastName());
        viewHolder.emailTextView.setText(patient.getEmail());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView fullNameTextView;
        TextView emailTextView;

        ViewHolder(View itemView){
            super(itemView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }
    }
}