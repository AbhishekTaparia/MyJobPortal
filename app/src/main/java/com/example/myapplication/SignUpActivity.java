package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText textEmail;
    private EditText textPassword;
    private Button buttonSignUp;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        textEmail=(EditText)findViewById(R.id.editTextEmail);
        textPassword=(EditText)findViewById(R.id.editTextPassword);
        buttonSignUp=(Button)findViewById(R.id.buttonSignUp);
        progressBar=(ProgressBar)findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        Log.d("mAuth",mAuth+"");

        ((TextView)findViewById(R.id.textLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }

    private void addUser(){
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if( email.isEmpty()){
            textEmail.setError("Email is required");
            textEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmail.setError("Please enter a valid email");
            textEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            textPassword.setError("Password is required");
            textPassword.requestFocus();
            return;
        }

        if(password.isEmpty()){
            textPassword.setError("Password is required");
            textPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            textPassword.setError("Password should be greater then 6 characters");
            textPassword.requestFocus();
            return;
        }

        String passRegex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if (!password.matches(passRegex))
        {
            textPassword.setError("Password should contain at least one number, one lowercase letter, one uppercase letter, and one special character.");
            textPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signUp", "signUpWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this,"Check your Email for Verification",Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        textEmail.setText("");
                                        textPassword.setText("");
                                    }
                                    else{
                                        Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signUp", "signUpWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed. jkjl",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}