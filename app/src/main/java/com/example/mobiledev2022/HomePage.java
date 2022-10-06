package com.example.mobiledev2022;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private Button button_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        firebaseAuth = FirebaseAuth.getInstance();
        String name= firebaseAuth.getCurrentUser().getEmail().toString();
        textView = findViewById(R.id.icon_homepage);
        textView.setText("Email "+name);
        button_logout =  findViewById(R.id.button_logout);
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