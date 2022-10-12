package com.example.mobiledev2022;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class HomePage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private Button button_logout;
    private Button button_chatRoom;
    private EditText roomID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        firebaseAuth = FirebaseAuth.getInstance();
        String userEmail= firebaseAuth.getCurrentUser().getUid().toString();
        button_chatRoom = findViewById(R.id.button_roomChat);
        textView = findViewById(R.id.toolbar1);
        textView.setText("Email "+userEmail);
        button_logout =  findViewById(R.id.button_logout);
        roomID = findViewById(R.id.roomID);

        button_chatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room = roomID.getText().toString();
                Intent intent = new Intent(HomePage.this,Chat.class);
                intent.putExtra("userEmail",userEmail);
                intent.putExtra("room",room);
                startActivity(intent);
            }
        });
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(HomePage.this,MainActivity.class));
                finish();
            }
        });

    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile,menu);
        return false;
    }
}