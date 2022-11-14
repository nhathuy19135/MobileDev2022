package com.example.mobiledev2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import database.doctor.DoctorDTO;
import database.doctor.DoctorModel;

public class crud_test extends AppCompatActivity {
    private DoctorDTO doctorActionController;
    private Toolbar toolbar;
    private RecyclerView doctorListRecyclerView;
    private FloatingActionButton addFAB;
    private FloatingActionButton sendTestData;
    private List<DoctorModel> doctorList;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_test);
        doctorActionController = DoctorDTO.newInstance();
        //TODO 3.1
        //toolbar setup
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Doctor View");
        setSupportActionBar(toolbar);

        //recycler view for doctor list
        doctorListRecyclerView = findViewById(R.id.doctorListRecyclerView);
        doctorListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setup empty textview, still testing if this should be implemented or not
        emptyTextView = findViewById(R.id.emptyTextView);

        //FAB for sending all pre-defined test data
        sendTestData = findViewById(R.id.sendTestDataFAB);
        sendTestData.setOnClickListener(new sendAllTestData());

        //FAB for creating doctor
        addFAB = findViewById(R.id.createDoctorFAB);
        addFAB.setOnClickListener(new createDoctorfromFAB());

    }

    @Override
    protected void onStart() {
        super.onStart();
        //todo 3.3
    }

    private class getAllDoctorOnCompleteListener implements OnCompleteListener<QuerySnapshot> {

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            //todo 3.3, 3.4

            if (doctorList == null || doctorList.size() == 0) {
                doctorListRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            } else {
                doctorListRecyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            }
        }
    }

    private void fillDoctorRecyclerView(List<DoctorModel> DoctorList) {
        //todo 3.4
    }

    //called when user clock something on the list
    public class DoctorListRecyclerViewOnItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int itemIndex = doctorListRecyclerView.indexOfChild(view);
            Log.d("DoctorListMainActivity", "" + itemIndex);

            DoctorModel doctor = doctorList.get(itemIndex);

            Intent intent = new Intent();
            intent.setClass(crud_test.this, crud_details.class);
            intent.putExtra(crud_details.OPERATION, crud_details.EDITING);

            // TODO: 4.2

            startActivity(intent);
        }
    }

    private class sendAllTestData implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //todo 3.2
            doctorActionController.sendTestData();
            Toast.makeText(crud_test.this, "Test data sent", Toast.LENGTH_SHORT).show();
        }
    }

    //called when user click the add button
    private class createDoctorfromFAB implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(crud_test.this, crud_details.class);
            intent.putExtra(crud_details.OPERATION, crud_details.CREATING);
        }
    }
}