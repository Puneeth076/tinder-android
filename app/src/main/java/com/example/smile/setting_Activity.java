package com.example.smile;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class setting_Activity extends AppCompatActivity {



    EditText inputName, inputPhone, inputLocation;
    Button confirmBtn, backBtn;
    ImageView inputProfile;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    TextView username;

    DatabaseReference mUsersDatabase;
    String userid, name, phone, imageUrl,value, location;
    Uri resultUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        inputLocation = findViewById(R.id.location);
        inputName = findViewById(R.id.inputName);
        inputPhone = findViewById(R.id.phone);
        inputProfile = findViewById(R.id.image);
        confirmBtn = findViewById(R.id.rectangle_243);
        backBtn = findViewById(R.id.rectangle_242);
        username = findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        userid = mAuth.getCurrentUser().getUid();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(setting_Activity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        getUserData();

        confirmBtn.setOnClickListener(view -> storeUsersData());

        inputProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,1);
        });

    }

    private void getUserData(){
        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    assert map != null;
                    if(map.get("name") != null){
                        name = Objects.requireNonNull(map.get("name")).toString();
                        inputName.setText(name);
                        username.setText(name);
                    }
                    if(map.get("phone") != null){
                        phone = Objects.requireNonNull(map.get("phone")).toString();
                        inputPhone.setText(phone);
                    }
                    if(map.get("gender") != null){
                        value = Objects.requireNonNull(map.get("gender")).toString();

                    }
                    if(map.get("location") != null){
                        location = Objects.requireNonNull(map.get("location")).toString();
                        inputLocation.setText(location);

                    }
                    if(map.get("profileImage") != null){
                        imageUrl = map.get("profileImage").toString();

                        switch (imageUrl){
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.img1)
                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .error(R.drawable.__img___google)

                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        }).into(inputProfile);
                                break;

                            default:
                                Glide.with(getApplication()).load(imageUrl)
                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .error(R.drawable.__img___google)

                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                return false;
                                            }
                                        }).into(inputProfile);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void storeUsersData() {
        name = inputName.getText().toString();
        phone = inputPhone.getText().toString();
        location = inputLocation.getText().toString();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("phone", phone);
        userInfo.put("location", location);
        mUsersDatabase.updateChildren(userInfo);


        if(resultUri != null){


            StorageReference pathFile = FirebaseStorage.getInstance().getReference().child("profileImages").child(userid);
            Bitmap bitmap = null ;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);

            byte[] data = byteArrayOutputStream.toByteArray();
            UploadTask uploadTask = pathFile.putBytes(data);

            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Please wait until updating...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> downloadUri =  taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    downloadUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoLink = uri.toString();
                            Map<String, Object> userInfo1 = new HashMap<>();

                            progressDialog.dismiss();
                            userInfo1.put("profileImage", photoLink);
                            mUsersDatabase.updateChildren(userInfo1);
                            finish();
                            Intent intent = new Intent(setting_Activity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
        }else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            final Uri imageUri = data.getData();

            resultUri = imageUri;
            inputProfile.setImageURI(resultUri);
        }
    }
    public void logoutFunction(View v){
        mAuth.signOut();
        Intent intent = new Intent(setting_Activity.this, loginORregister_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        return;
    }
}