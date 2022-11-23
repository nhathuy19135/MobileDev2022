package com.example.mobiledev2022.database.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobiledev2022.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PatientMainActivity extends AppCompatActivity {
    private static String TAG = PatientMainActivity.class.toString();
    private Toolbar toolbar;
    private RecyclerView patientRecyclerView;
    private FloatingActionButton sendAllTestData;
    private FloatingActionButton addPatientButton;
    private TextView emptyTextView;
     private List<Patient> patientList;
     private PatientFirestoreManager PFM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        PFM = PatientFirestoreManager.newInstance();
        //setup toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Patient List");
        //setup recycler view for patient
        patientRecyclerView = findViewById(R.id.patientRecyclerView);
        patientRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyTextView = findViewById(R.id.emptyTextView);

        //setup FAB for sending all test data
        sendAllTestData = findViewById(R.id.sendBulkDataFloatingButton);
        sendAllTestData.setOnClickListener(new SendAllPatientFABOnClickListener());
        //setup FAB for creating a new patient
        addPatientButton = findViewById(R.id.addFloatingButton);
        addPatientButton.setOnClickListener(new AffFABOnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        PFM.getAllPatients(new GetAllPatientOnCompleteListener());
    }

    private class SendAllPatientFABOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            PFM.sendTestData();
            Toast.makeText(PatientMainActivity.this, "test data sent", Toast.LENGTH_LONG).show();

        }
    }

    private class AffFABOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(PatientMainActivity.this, PatientDetailActivity.class);
            intent.putExtra(PatientDetailActivity.OPERATION, PatientDetailActivity.CREATING);
            startActivity(intent);
        }
    }
    private class GetAllPatientOnCompleteListener implements OnCompleteListener<QuerySnapshot> {

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null){
                    patientList = querySnapshot.toObjects(Patient.class);
                    populatePatientRecyclerView(patientList);
                }
            } else {
                Log.w(TAG, "error getting docs: ", task.getException());
            }
            if (patientList == null || patientList.size() == 0){
                patientRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            } else {
                patientRecyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            }
        }
    }
    private void populatePatientRecyclerView(List<Patient> patientList) {
        PatientRecyclerViewAdapter PRA = new PatientRecyclerViewAdapter(patientList, new PatientRecyclerViewOnItemClickListener());
        patientRecyclerView.setAdapter(PRA);
    }

    public class PatientRecyclerViewOnItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int itemPosition = patientRecyclerView.indexOfChild(view);
            Log.d("PatientMainActivity", "" + itemPosition);
            Patient patient = patientList.get(itemPosition);
            Intent intent = new Intent();
            intent.setClass(PatientMainActivity.this, PatientDetailActivity.class);
            intent.putExtra(PatientDetailActivity.OPERATION, PatientDetailActivity.EDITING);
            intent.putExtra(PatientDBContract.DOCUMENT_ID, patient.getDocumentId());
            intent.putExtra(PatientDBContract.FIELD_FIRST_NAME, patient.getFirstName());
            intent.putExtra(PatientDBContract.FIELD_LAST_NAME, patient.getLastName());
            intent.putExtra(PatientDBContract.FIELD_EMAIL, patient.getEmail());
            intent.putExtra(PatientDBContract.FIELD_PHONE_NUMBER, patient.getPhoneNumber());
            intent.putExtra(PatientDBContract.FIELD_GENDER, patient.getGender());
            startActivity(intent);
        }
    }
}