package com.example.smile;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText inputName, inputEmail, inputPassword, inputConfirmPassword;
    Button registerButton;
    TextView loginPage;
    ProgressDialog progressDialog;
    RadioGroup mRadioGroup;

    FirebaseDatabase database;
    ImageView googleImage;

    int RC_SIGN_IN = 20;

    TextView genderError;
    String emailPattern =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8, 20}$";
    FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    FirebaseAuth mAuth;
    ImageView showPassword, showConfirmPassword;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        inputName = findViewById(R.id.username);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.update_btn);
        mRadioGroup = findViewById(R.id.radioGroup);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        googleImage = findViewById(R.id.__img___google);
        mUser = mAuth.getCurrentUser();
        genderError = findViewById(R.id.genderError);
        loginPage = findViewById(R.id.already_have_an_account__login_now);
        database = FirebaseDatabase.getInstance();
        showPassword = findViewById(R.id.__img___fluent_eye_20_filled);
        showPassword.setImageResource(R.drawable.__img___fluent_eye_20_filled);
        showConfirmPassword = findViewById(R.id.__img___fluent_eye_20_filled1);
        showConfirmPassword.setImageResource(R.drawable.__img___fluent_eye_20_filled);

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.__img___fluent_eye_20_filled);
                }else{
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPassword.setImageResource(R.drawable.hide);
                }
            }
        });

        showConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputConfirmPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    inputConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showConfirmPassword.setImageResource(R.drawable.__img___fluent_eye_20_filled);
                }else{
                    inputConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showConfirmPassword.setImageResource(R.drawable.hide);
                }
            }
        });


        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mUser != null){
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                     finish();
                     return;
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
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedId);


        if (!email.equals(emailPattern) && email.length() == 0){
            inputEmail.setError("check your email");
            finish();
            return;
        }

        if (!password.equals(passwordPattern) && password.length() == 0){
            inputPassword.setError("Password contains all fields");
            finish();
            return;
        }


        if(name.isEmpty()){
            inputName.setError("name required");
            finish();
            return;
        }
        if(!password.equals(confirmPassword)){
            inputPassword.requestFocus();
            Toast.makeText(RegisterActivity.this, "Password mismatch", Toast.LENGTH_LONG).show();
            inputPassword.setError("Password mismatch");
            finish();
        }else {

            progressDialog.setTitle("Registration...");
            progressDialog.setMessage("Please wait until registration...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, (task) -> {
                if(!task.isSuccessful()){
                    Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else{
                    Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    String userId = mAuth.getCurrentUser().getUid();
                                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                    Map userInfo = new HashMap();
                                    userInfo.put("name", name);
                                    userInfo.put("gender", radioButton.getText().toString());
                                    userInfo.put("profileImage", "default");
                                    currentUserDb.updateChildren(userInfo);



                                    currentUserDb.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            FirebaseUser  user = mAuth.getCurrentUser();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
            
        mAuth.addAuthStateListener(firebaseAuthStateListener);
        }
        else{
            Toast.makeText(this, "Kindly verify the email", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();


        mAuth.removeAuthStateListener(firebaseAuthStateListener);

    }

    public void goToFrontPage(View view){
        Intent intent = new Intent(this, loginORregister_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}