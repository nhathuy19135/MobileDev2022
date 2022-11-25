package com.example.mobiledev2022;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobiledev2022.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.example.mobiledev2022.database.contact.ContactListMainActivity;
import com.example.mobiledev2022.database.doctor.Doctor;
import com.example.mobiledev2022.database.doctor.DoctorMainActivity;
import com.example.mobiledev2022.database.patient.PatientMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import database.patient.Patient;
import database.services.UserAdapter;
public class HomePage extends AppCompatActivity {
    private PreferenceManager preferenceManager;
    private FirebaseAuth firebaseAuth;
    private Button button_logout;
    private Button button_chatRoom;
    private EditText roomID;
    private ActivityChatBinding binding;
    private UserAdapter userAdapter;
    private ArrayList<Patient> userList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private RoundedImageView roundedImageView;

    private Button button_crud;
    private Button button_doctor;
    private Button button_patient;
    
    private Button button_call;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_home_page);
        recyclerView = findViewById(R.id.recyclerUser);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        roundedImageView = findViewById(R.id.roundedImageView);
        initView();
        setUpUserList();
        setUpAdapter();
        setUpAvatar();
    }
    private void setUpAdapter() {
        userList = new ArrayList<>();
        this.userAdapter = new UserAdapter(userList);
        this.linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void initView() {
        String userEmail = firebaseAuth.getCurrentUser().getUid().toString();
        button_chatRoom = findViewById(R.id.button_roomChat);
        button_logout = findViewById(R.id.button_logout);
        roomID = findViewById(R.id.roomID);
        button_chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room = roomID.getText().toString();
                if (room.equals("")) {
                    Toast toast = Toast.makeText(HomePage.this, "Enter Room Number or Room Name", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent intent = new Intent(HomePage.this, Chat.class);
                    intent.putExtra("userEmail", userEmail);
                    intent.putExtra("room", room);
                    startActivity(intent);
                }
            }
        });
        button_call = findViewById(R.id.button_call);
        button_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomePage.this,VideoCall.class));
                finish();
            }
        });
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(HomePage.this, MainActivity.class));
                finish();
            }
        });
        button_crud = findViewById(R.id.button_crud);
        button_crud.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, ContactListMainActivity.class));
            }
        });
        button_doctor = findViewById(R.id.button_doctor);
        button_doctor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, DoctorMainActivity.class));
            }
        });
        button_patient = findViewById(R.id.button_patient);
        button_patient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, PatientMainActivity.class));
            }
        });
    }
    private void setUpAvatar() {
        firestore.collection("Patients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                    for (Patient index : querySnapshot.toObjects(Patient.class)) {
                        if (index.getEmail() == firebaseAuth.getCurrentUser().getEmail()) {
                            Bitmap bit = decodeIamge(index.getImage());
                            roundedImageView.setImageBitmap(bit);
                        }
                    }
            }
        });
    }
    private void setUpUserList() {
        String email = firebaseAuth.getCurrentUser().getEmail();
        Log.e("email", email);
        firestore.collection("Patients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()){
                    showToast("No doctor");
                } else {
                    List<Patient> list =  querySnapshot.toObjects(Patient.class);
                    userList.addAll(list);
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }
    public Bitmap decodeIamge(String imageBitmap) {
        byte[] bytes = Base64.decode(imageBitmap,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}