package com.example.smile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class More_information_activity extends AppCompatActivity {

    RadioGroup mRadioGroup;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    RadioButton single, married;
    String userId, jobOfUser, DOB, looking, ageOfUser, insta, facebook;
    DatabaseReference mDatabaseReferences;
    EditText job, dateOfBirth, lookingFor, age, instagram_id, facebook_id;
    Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_information);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRadioGroup = findViewById(R.id.radioGroup);
        job = findViewById(R.id.job);
        single = findViewById(R.id.radioButton1);
        married = findViewById(R.id.radioButton2);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        lookingFor = findViewById(R.id.lookingFor);
        age = findViewById(R.id.age);
        instagram_id = findViewById(R.id.instagram);
        facebook_id = findViewById(R.id.facebook);
        update_btn = findViewById(R.id.update_btn);
        userId = mUser.getUid();
        mDatabaseReferences = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserData();
        
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUserData();
            }
        });
        
    }

    private void getUserData() {
        mDatabaseReferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    assert map != null;
                    if(map.get("job") != null){
                        jobOfUser = Objects.requireNonNull(map.get("name")).toString();
                        job.setText(jobOfUser);
                    }
                    if(map.get("dob") != null){
                        DOB = Objects.requireNonNull(map.get("dob")).toString();
                        dateOfBirth.setText(DOB);
                    }
                    if(map.get("looking") != null){
                        looking = Objects.requireNonNull(map.get("looking")).toString();
                        lookingFor.setText(looking);
                    }
                    if(map.get("age") != null){
                        ageOfUser = Objects.requireNonNull(map.get("age")).toString();
                        age.setText(ageOfUser);
                    }
                    if(map.get("instagram") != null){
                        insta = Objects.requireNonNull(map.get("instagram")).toString();
                        instagram_id.setText(insta);
                    }
                    if(map.get("facebook") != null){
                        facebook = Objects.requireNonNull(map.get("facebook")).toString();
                        facebook_id.setText(facebook);
                    }

                    if (map.get("relationship") != null){
                        String relationship = map.get("relationship").toString();
                        if (relationship.equals("Married")){
                            married.setChecked(true);

                        }
                        else{
                            single.setChecked(true);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void storeUserData() {
        String jobData = job.getText().toString();
        String ageData = age.getText().toString();
        String lookinfor = lookingFor.getText().toString();
        String dateofbirth = dateOfBirth.getText().toString();
        String insta = instagram_id.getText().toString();
        String face = facebook_id.getText().toString();
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedId);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("job", jobData);
        userInfo.put("age", ageData);
        userInfo.put("looking", lookinfor);
        userInfo.put("dob", dateofbirth);
        userInfo.put("instagram", insta);
        userInfo.put("facebook", face);
        userInfo.put("relationship", radioButton.getText().toString());

        mDatabaseReferences.updateChildren(userInfo);
        Intent intent = new Intent(More_information_activity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void goToFrontPage(View view){
        Intent intent = new Intent(More_information_activity.this, setting_Activity.class);
        startActivity(intent);
    }

}