package com.example.smile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    Button registerButton;
    TextView loginPage;
    ProgressDialog progressDialog;
    RadioGroup mradiogroup;

    FirebaseDatabase database;

    FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        inputName = findViewById(R.id.username);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_btn);
        mradiogroup = findViewById(R.id.radioGroup);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        loginPage = findViewById(R.id.already_have_an_account__login_now);
        database = FirebaseDatabase.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mUser != null){
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                     finish();
                     return;
                }
                else{

                }
            }
        };

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAuth();
            }
        });
    }

    private void performAuth() {
        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = inputConfirmPassword.getText().toString();
        int selectedId = mradiogroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedId);

        if (radioButton.getText() == null){
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = radioButton.getText().toString();


        if(name.isEmpty()||email.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()||gender.isEmpty()){
            Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!password.equals(confirmPassword)){
            inputPassword.requestFocus();
            Toast.makeText(RegisterActivity.this, "Password mismatch", Toast.LENGTH_LONG).show();
            return;
        }else {
            progressDialog.setTitle("Registration...");
            progressDialog.setMessage("Please wait until registration...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();

                        String userId = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentDb = database.getReference().child("Users").child(gender).child(userId);
                        Map userInfo = new HashMap();
                        userInfo.put("name",name);
                        userInfo.put("profileImage","default");
                        currentDb.updateChildren(userInfo);

                        currentDb.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, "Registration completed", Toast.LENGTH_LONG).show();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration error", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(firebaseAuthStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mAuth.removeAuthStateListener(firebaseAuthStateListener);
//    }
}