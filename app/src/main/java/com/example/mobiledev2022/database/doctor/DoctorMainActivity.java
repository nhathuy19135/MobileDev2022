package com.example.mobiledev2022.database.doctor;

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
import com.example.mobiledev2022.database.contact.Contact;
import com.example.mobiledev2022.database.contact.ContactDetailsActivity;
import com.example.mobiledev2022.database.contact.ContactListMainActivity;
import com.example.mobiledev2022.database.contact.ContactsFirestoreDBContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DoctorMainActivity extends AppCompatActivity {
    private static String TAG = DoctorMainActivity.class.toString();
    private Toolbar toolbar;
    private RecyclerView doctorRecyclerView;
    private FloatingActionButton sendAllTestData;
    private FloatingActionButton addDoctorButton;
    private TextView emptyTextView;

    private List<Doctor> doctorList;
    private DoctorFirestoreManager DFM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        DFM = DoctorFirestoreManager.newInstance();

        //setup toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.doctor_alias));

        //setup recycler view for doctor
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyTextView = findViewById(R.id.emptyTextView);

        //setup FAB for sending all test data
        sendAllTestData = findViewById(R.id.sendBulkDataFloatingButton);
        sendAllTestData.setOnClickListener(new SendAllDoctorFABOnClickListener());

        //setup FAB for creating a new doctor
        addDoctorButton = findViewById(R.id.addFloatingButton);
        addDoctorButton.setOnClickListener(new AddFABOnClickListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        DFM.getAllDoctors(new GetAllDoctorsOnCompleteListener());
    }

    private class SendAllDoctorFABOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DFM.sendTestData();
            Toast.makeText(DoctorMainActivity.this, "test data sent", Toast.LENGTH_LONG).show();
        }
    }

    private class AddFABOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(DoctorMainActivity.this, DoctorDetailsActivity.class);
            intent.putExtra(DoctorDetailsActivity.OPERATION, DoctorDetailsActivity.CREATING);
            startActivity(intent);
        }
    }

    private class GetAllDoctorsOnCompleteListener implements OnCompleteListener<QuerySnapshot> {

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null){
                    doctorList = querySnapshot.toObjects(Doctor.class);
                    populateDoctorRecyclerView(doctorList);
                }
            } else {
                Log.w(TAG, "error getting docs: ", task.getException());
            }
            if (doctorList == null || doctorList.size() == 0){
                doctorRecyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            } else {
                doctorRecyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            }
        }
    }

    private void populateDoctorRecyclerView(List<Doctor> doctorList) {
        DoctorRecyclerViewAdapter DRA = new DoctorRecyclerViewAdapter(doctorList, new DoctorRecyclerViewOnItemClickListener());
        doctorRecyclerView.setAdapter(DRA);
    }

    public class DoctorRecyclerViewOnItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            int itemPosition = doctorRecyclerView.indexOfChild(view);
            Log.d("DoctorMainActivity", "" + itemPosition);
            Doctor doctor = doctorList.get(itemPosition);
            Intent intent = new Intent();
            intent.setClass(DoctorMainActivity.this, DoctorDetailsActivity.class);
            intent.putExtra(DoctorDetailsActivity.OPERATION, DoctorDetailsActivity.EDITING);
            intent.putExtra(DoctorDBContract.DOCUMENT_ID, doctor.getDocumentId());
            intent.putExtra(DoctorDBContract.FIELD_FIRST_NAME, doctor.getFirstName());
            intent.putExtra(DoctorDBContract.FIELD_LAST_NAME, doctor.getLastName());
            intent.putExtra(DoctorDBContract.FIELD_EMAIL, doctor.getEmail());
            intent.putExtra(DoctorDBContract.FIELD_PHONE_NUMBER, doctor.getPhoneNumber());
            intent.putExtra(DoctorDBContract.FIELD_GENDER, doctor.getGender());
            intent.putExtra(DoctorDBContract.FIELD_FIELD, doctor.getField());
            intent.putExtra(DoctorDBContract.FIELD_TITLE, doctor.getTitle());
            startActivity(intent);

        }
    }
}