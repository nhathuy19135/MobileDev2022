package com.example.mobiledev2022;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiledev2022.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import database.patient.Patient;
import database.patient.PatientFirestoreManager;


public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private Uri avatarUri;
    private String avatarDb;
    private FirebaseAuth firebaseAuth;
    private boolean isLoading = false;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private PatientFirestoreManager patientFirestoreManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        patientFirestoreManager = new PatientFirestoreManager();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        initView();
    }
    private void initView() {
        binding.signUpAvatar.setOnClickListener(v ->{
            try {
                selectAvatar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
                            avatarUri = result.getData().getData();
                            InputStream inputStream = getContentResolver().openInputStream(avatarUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            avatarDb = encodeImage(bitmap);
                            binding.signUpAvatar.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    private void updateAvatar(String email) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        StorageReference ref  = storageReference.child("avatar"+email);
        ref.putFile(avatarUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressDialog.setMessage("Uploading "+10+"  ");
            }
        });
    }
    private void setListener() {
        binding.buttonSignUp.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v ->{
            boolean check = isValidSignUp();
            if (check) {
                signUp();
            }
        });
    }
    private void createUser(Patient newUser) {
        this.patientFirestoreManager.newPatient(newUser);
//        databaseReference = FirebaseDatabase.getInstance().getReference("Patients");
//        databaseReference.push().setValue(newUser);
    }
    private void signUp(){
        isLoading = true;
        firebaseAuth.createUserWithEmailAndPassword(binding.signUpEmailAddress.getText().toString(),binding.signUpPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Patient  newUser = new Patient();
                        String userId = UUID.randomUUID().toString();
                        newUser.setDocumentId(userId);
                        newUser.setEmail(binding.signUpEmailAddress.getText().toString());
                        newUser.setName(binding.signUpUserName.getText().toString());
                        newUser.setImage(avatarDb);
                        createUser(newUser);
                        updateAvatar(binding.signUpEmailAddress.getText().toString());
                        Toast.makeText(SignUp.this,"Sign Up",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this,HomePage.class));
                        finish();
                    } else {
                        Toast.makeText(SignUp.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
    private boolean isValidSignUp() {
        if (avatarUri == null) {
            showToast("select Avatar");
            return false;
        }
        if (binding.signUpEmailAddress.getText().toString().trim().isEmpty()) {
            showToast("Enter the email");
            return false;
        }
        if (binding.signUpPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter the password");
            return false;
        }
        if (binding.signUpUserName.getText().toString().trim().isEmpty()) {
            showToast("Enter the userName");
            return false;
        }
        return true;
    }
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