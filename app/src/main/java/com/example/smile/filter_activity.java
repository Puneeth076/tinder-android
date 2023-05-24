package com.example.smile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class filter_activity extends AppCompatActivity{

    RadioGroup mRadioGroup;
    EditText place, age;
    Button applyFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mRadioGroup = findViewById(R.id.radioGroup);
        place = findViewById(R.id.place);
        age = findViewById(R.id.age);
        applyFilter = findViewById(R.id.applyBtn);

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performFilter();
            }
        });

    }

    private void performFilter() {
         int selectedId = mRadioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = findViewById(selectedId);
            Intent intent = new Intent(filter_activity.this, com.example.smile.filter.MainActivity.class);
            intent.putExtra("interested", radioButton.getText().toString());
            intent.putExtra("age", age.getText().toString());
            intent.putExtra("place", place.getText().toString());

            startActivity(intent);
    }

    public  void mainPage(View view){
        Intent intent = new Intent(filter_activity.this, MainActivity.class);
        startActivity(intent);
    }

}