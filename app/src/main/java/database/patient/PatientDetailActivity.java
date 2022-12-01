package database.patient;

import static database.patient.PatientDBContract.*;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobiledev2022.R;

public class PatientDetailActivity extends AppCompatActivity {

    public static final String OPERATION = "OPERATION";
    public static final String CREATING = "CREATING";
    public static final String EDITING  = "EDITING";

    private String operationTypeString;
    private PatientFirestoreManager PFM;
    private String documentId;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText genderEditText;

    private Button deleteButton;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        PFM = PatientFirestoreManager.newInstance();
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        genderEditText = findViewById(R.id.genderEditText);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new PatientDetailActivity.DeleteButtonOnClickListener());
        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new PatientDetailActivity.OkButtonOnClickListener());

        Bundle bundle = getIntent().getExtras();

        operationTypeString = bundle.getString(OPERATION);
        if (operationTypeString.equals(CREATING)){
            okButton.setText("CREATE");
            deleteButton.setVisibility(View.GONE);
        } else if (operationTypeString.equals(EDITING)){
            okButton.setText("UPDATE");
            deleteButton.setVisibility(View.VISIBLE);
            documentId = bundle.getString(DOCUMENT_ID);
            firstNameEditText.setText(bundle.getString(FIELD_FIRST_NAME));
            lastNameEditText.setText(bundle.getString(FIELD_LAST_NAME));
            emailEditText.setText(bundle.getString(FIELD_EMAIL));
            phoneNumberEditText.setText(bundle.getString(FIELD_PHONE_NUMBER));
            genderEditText.setText(bundle.getString(FIELD_GENDER));

        }
    }

    private class DeleteButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            PFM.deletePatient(documentId);
            finish();
        }
    }

    private class OkButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();
            String gender = genderEditText.getText().toString();
            Patient patient = new Patient(firstName, lastName, gender, email, phoneNumber);
            if (operationTypeString.equals(CREATING)){
                PFM.newPatient(patient);
            } else if (operationTypeString.equals(EDITING)){
                PFM.updatePatient(documentId, patient);
            }
            finish();
        }
    }
}