package database.doctor;

import static database.doctor.DoctorDBContract.DOCUMENT_ID;
import static database.doctor.DoctorDBContract.FIELD_EMAIL;
import static database.doctor.DoctorDBContract.FIELD_FIELD;
import static database.doctor.DoctorDBContract.FIELD_FIRST_NAME;
import static database.doctor.DoctorDBContract.FIELD_GENDER;
import static database.doctor.DoctorDBContract.FIELD_LAST_NAME;
import static database.doctor.DoctorDBContract.FIELD_PHONE_NUMBER;
import static database.doctor.DoctorDBContract.FIELD_TITLE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledev2022.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class DoctorDetailsActivity extends AppCompatActivity {

    /* Constants to define the database operation type */
    public static final String OPERATION = "OPERATION";
    public static final String CREATING = "CREATING";
    public static final String EDITING = "EDITING";

    private String operationTypeString;

    /* Repository reference */
    private DoctorFirestoreManager DFM;

    /* Document ID of this particular Contact item */
    private String documentId;

    /* Widgets */
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText fieldEditText;
    private EditText genderEditText;
    private EditText titleEditText;
    private ImageView avatarDoctor;
    private Button deleteButton;
    private Button okButton;
    private String avatar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        // Get a reference of contactsFirestoreManager
        // TODO: 4.1 Creating a Contact
        DFM = DoctorFirestoreManager.newInstance();
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        fieldEditText = findViewById(R.id.fieldEditText);
        genderEditText = findViewById(R.id.genderEditText);
        titleEditText = findViewById(R.id.titleEditText);
        avatarDoctor = findViewById(R.id.signUpAvatarDoctor);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new DoctorDetailsActivity.DeleteButtonOnClickListener());
        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new DoctorDetailsActivity.OKButtonOnClickListener());
        Bundle bundle = getIntent().getExtras();
        operationTypeString = bundle.getString(OPERATION);
        if (operationTypeString.equals(CREATING)) {
            okButton.setText("CREATE");
            deleteButton.setVisibility(View.GONE);
        } else if (operationTypeString.equals(EDITING)) {
            avatarDoctor = findViewById(R.id.signUpAvatarDoctor);
            okButton.setText("UPDATE");
            deleteButton.setVisibility(View.VISIBLE);
            documentId = bundle.getString(DOCUMENT_ID);
            firstNameEditText.setText(bundle.getString(FIELD_FIRST_NAME));
            lastNameEditText.setText(bundle.getString(FIELD_LAST_NAME));
            emailEditText.setText(bundle.getString(FIELD_EMAIL));
            phoneNumberEditText.setText(bundle.getString(FIELD_PHONE_NUMBER));
            fieldEditText.setText(bundle.getString(FIELD_FIELD));
            titleEditText.setText(bundle.getString(FIELD_TITLE));
            genderEditText.setText(bundle.getString(FIELD_GENDER));

            // TODO: 4.2 Updating a Contact
        }
        avatarDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    selectAvatar();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class OKButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();
            String gender = genderEditText.getText().toString();
            String title = titleEditText.getText().toString();
            String field = fieldEditText.getText().toString();
            String avatarDoctor =  avatar;
            Doctor doctor = new Doctor(firstName, lastName, email, phoneNumber, gender, title, field);
            doctor.setImage(avatarDoctor);
            if (operationTypeString.equals(CREATING)) {
                // TODO: 4.1 Creating a Contact
                DFM.newDoctor(doctor);

            } else if (operationTypeString.equals(EDITING)) {
                // TODO: 4.2 Updating a Contact
                DFM.updateDoctor(documentId, doctor);
            }

            finish();
        }
    }

    private class DeleteButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            // TODO: 4.3 Deleting a Contact
            DFM.deleteDoctor(documentId);

            finish();
        }
    }
    private void selectAvatar() throws IOException {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImage.launch(intent);
    }
    ActivityResultLauncher<Intent> selectImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            Uri avatarUri = result.getData().getData();
                            InputStream inputStream = getContentResolver().openInputStream(avatarUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            avatar = encodeImage(bitmap);
                            avatarDoctor.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
}
