package database.patient;

import static database.patient.PatientDBContract.DOCUMENT_ID;
import static database.patient.PatientDBContract.FIELD_EMAIL;
import static database.patient.PatientDBContract.FIELD_FIRST_NAME;
import static database.patient.PatientDBContract.FIELD_GENDER;
import static database.patient.PatientDBContract.FIELD_LAST_NAME;
import static database.patient.PatientDBContract.FIELD_PHONE_NUMBER;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

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
    private Spinner roleEditText;
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
        roleEditText = findViewById(R.id.roleEditText);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new PatientDetailActivity.DeleteButtonOnClickListener());
        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new PatientDetailActivity.OkButtonOnClickListener());

        Bundle bundle = getIntent().getExtras();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleEditText.setAdapter(adapter);

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
            String avatar = getIntent().getExtras().getString("image");
            String role = roleEditText.getSelectedItem().toString();
            Patient patient = new Patient(firstName, lastName, gender, email, phoneNumber);
            patient.setRole(role);
            patient.setImage(avatar);
            if (operationTypeString.equals(CREATING)){
                PFM.newPatient(patient);
            } else if (operationTypeString.equals(EDITING)){
                PFM.updatePatient(documentId, patient);
            }
            finish();
        }
    }
}