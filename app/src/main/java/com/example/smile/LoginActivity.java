package com.example.smile;

import androidx.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText inputEmail, inputPassword;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    FirebaseAuth mAuth;
    TextView toRegister, forgotPassword;
    String emailPattern =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    ImageView showPassword;

    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        loginBtn = findViewById(R.id.rectangle_173);
        inputEmail = findViewById(R.id.rectangle_4_ek1);
        inputPassword = findViewById(R.id.rectangle_4);
        toRegister = findViewById(R.id.don_t_have_an_account__register_now);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);
        showPassword = findViewById(R.id.__img___fluent_eye_20_filled);
        showPassword.setImageResource(R.drawable.__img___fluent_eye_20_filled);
        forgotPassword = findViewById(R.id.forgot_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, forgotPassword_activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
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
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mUser != null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                else{

                }
            }
        };
    }

    private void performLogin() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.equals(emailPattern)){
            inputEmail.setError("Bad email");
        }

        if(email.isEmpty()){
            inputEmail.setError("Email required");
            inputEmail.requestFocus();
        } else if (password.isEmpty()) {
            inputPassword.setError("Password required");
            inputPassword.requestFocus();
        }else{
            progressDialog.setMessage("Please wait until login...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();



            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       FirebaseUser user = mAuth.getCurrentUser();
                       assert user != null;
                       if (user.isEmailVerified()){
                           progressDialog.dismiss();
                           sendUserToNextPage();
                           Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                           return;
                       }
                       else{
                           progressDialog.dismiss();
                           Toast.makeText(LoginActivity.this, "Kindly verify your account", Toast.LENGTH_SHORT).show();
                       }


                       }else{
                           progressDialog.dismiss();
                           Toast.makeText(LoginActivity.this, "No user found", Toast.LENGTH_LONG).show();

                       }


               }
           });
        }
    }

    private void sendUserToNextPage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    public void  goToFrontPage(View view){
        Intent intent = new Intent(LoginActivity.this, loginORregister_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


}