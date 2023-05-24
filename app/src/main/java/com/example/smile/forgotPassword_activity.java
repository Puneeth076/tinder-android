package com.example.smile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassword_activity extends AppCompatActivity {

    EditText forgotEmail;
    Button button;
    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotEmail = findViewById(R.id.forgotEmail);
        button = findViewById(R.id.button);
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final String email = forgotEmail.getText().toString();
                if (email.isEmpty()){
                    forgotEmail.setError("Email required");
                    return;
                }
                mProgressDialog.setTitle("Sending mail");
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            mProgressDialog.dismiss();
                            Intent intent = new Intent(forgotPassword_activity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Toast.makeText(forgotPassword_activity.this, "Check your mail", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else {
                            mProgressDialog.dismiss();
                            Toast.makeText(forgotPassword_activity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressDialog.dismiss();
                        Toast.makeText(forgotPassword_activity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}