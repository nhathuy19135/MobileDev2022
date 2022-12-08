package com.example.mobiledev2022;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private TextView buttonSignUp;
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private EditText email,password;
    private boolean isSignIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSignUp = findViewById(R.id.sign_up_text);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textView = findViewById(R.id.textView);
        email= findViewById(R.id.edit_email);
        password = findViewById(R.id.userPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this,HomePage.class));
            finish();
        }
        buttonSignUp.setOnClickListener(v ->{
            handleSignUp();
        });
        buttonSignIn.setOnClickListener(view -> {
            if (email.getText().toString().isEmpty() || email.getText().toString().isEmpty() ) {
                Toast.makeText(MainActivity.this,"Missing input",Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (isSignIn) {
                    handleSignUp();
                } else {
                    handleLogin();
                }
            }

        });
        textView.setOnClickListener(view -> {
            if (isSignIn) {
                isSignIn = false ;
                email.setVisibility(View.GONE);
                buttonSignIn.setText("Don't have a already account :)) Sign up");
            } else {
                isSignIn = true;
                email.setVisibility(View.VISIBLE);
                buttonSignIn.setText("Sign In");
                textView.setText("Already have an account");
            }
        });
    }
    private void handleSignUp () {
        startActivity(new Intent(MainActivity.this,SignUp.class));
        finish();
    }
    private void handleLogin() {
        Log.e("email",email.getText().toString());
        Log.e("password",password.getText().toString());
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this,HomePage.class));
                    Toast.makeText(MainActivity.this,"Log In",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,HomePage.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}