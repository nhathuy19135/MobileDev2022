package com.example.mobiledev2022;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiledev2022.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import database.patient.Patient;
import database.patient.PatientMainActivity;
import database.services.UserAdapter;
public class HomePage extends AppCompatActivity {
    private DatabaseReference databaseReference;
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
    private ImageView roundedImageView;
    private Button button_crud;
    private Button button_doctor;
    private Button button_patient;
    private Button button_call;
    private Patient user;
    private String avatar;
    private HomePage.RoomchatClick roomchatClick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_home_page);
        user = new Patient();
        roomchatClick = new HomePage.RoomchatClick();
        recyclerView = findViewById(R.id.recyclerUser);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        roundedImageView = findViewById(R.id.roundedImageView);
        findUserById();
        setUpAdapter();
        initView();
        setUpUserList();
    }
    private void findUserById() {
        firestore.collection("patient").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (DocumentSnapshot index : querySnapshot.getDocuments()) {
                    Patient a = index.toObject(Patient.class);
                    if (a.getEmail().equals(firebaseAuth.getCurrentUser().getEmail() )) {
                        roundedImageView.setImageBitmap(decodeIamge(a.getImage()));
                        user = a;
                        avatar = a.getImage();
                    }
                }
            }
        });
    }
    private void setUpAdapter() {
        userList = new ArrayList<>();
        this.userAdapter = new UserAdapter(userList);
        this.linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);
        userAdapter.setRoomchatClick(roomchatClick);
    }
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    private void initView() {
        String userEmail = firebaseAuth.getCurrentUser().getUid().toString();
        button_chatRoom = findViewById(R.id.button_roomChat);
//        button_logout = findViewById(R.id.button_logout);
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
                    intent.putExtra("userEmail", user.getEmail());
                    intent.putExtra("roomdb", room);
                    intent.putExtra("image",user.getImage());
                    intent.putExtra("userName",user.getFirstName());
                    startActivity(intent);
                }
            }
        });
//        button_call = findViewById(R.id.button_call);
//        button_call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(HomePage.this,VideoCall.class));
//                finish();
//            }
//        });
//        button_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                firebaseAuth.signOut();
//                startActivity(new Intent(HomePage.this, MainActivity.class));
//                finish();
//            }
//        });
//        button_crud = findViewById(R.id.button_crud);
//        button_crud.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomePage.this, ContactListMainActivity.class));
//            }
//        });
//        button_doctor = findViewById(R.id.button_doctor);
//        button_doctor.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomePage.this, DoctorMainActivity.class));
//            }
//        });
//        button_patient = findViewById(R.id.button_patient);
//        button_patient.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomePage.this, PatientMainActivity.class));
//            }
//        });
//    }

    private void setUpUserList() {
        String email = firebaseAuth.getCurrentUser().getEmail();
        Log.e("email", email);
        firestore.collection("patient").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()){
                    showToast("No doctor");
                } else {
                    for(Patient index : querySnapshot.toObjects(Patient.class)) {
                            if (!index.getEmail().equals(firebaseAuth.getCurrentUser().getEmail()) && index.getRole().equals("Doctor")) {
                                userList.add(index);
                            }
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }
    public Bitmap decodeIamge(String imageBitmap) {
        byte[] bytes = Base64.decode(imageBitmap,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
    public class RoomchatClick implements  View.OnClickListener {
        private Patient user;
        @Override
        public void onClick(View v) {
                int position = recyclerView.indexOfChild(v);
                Patient temp = userList.get(position);
                String name = temp.getFirstName();
                Intent intent = new Intent(HomePage.this, Chat.class);
                if (this.user.getRole().equals("Doctor")) {
                    String room = temp.getDocumentId()+this.user.getDocumentId();
                    intent.putExtra("roomdb", room);
                    intent.putExtra("name",name);
                    intent.putExtra("image",this.user.getImage());
                    startActivity(intent);
                } else {
                    String room = this.user.getDocumentId() + temp.getDocumentId();
                    intent.putExtra("roomdb", room);
                    intent.putExtra("name",name);
                    intent.putExtra("image",this.user.getImage());
                    startActivity(intent);
                }
            }
        public void setUser(Patient user) {
            this.user = user;
        }
    }
}