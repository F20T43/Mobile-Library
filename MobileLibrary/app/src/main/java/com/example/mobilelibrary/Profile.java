package com.example.mobilelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Profile extends AppCompatActivity {

    Button Edit, Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Edit = (Button) findViewById(R.id.edit);
        Logout = (Button) findViewById(R.id.log_out);




        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_intent = new Intent(Profile.this,EditProfile.class);




                edit_intent.putExtra(       );





                // Go to onActivityResult method
                startActivityForResult(edit_intent, 1);
            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // in onActivityResult, We can do some processing on the data transferred from another activity
        super.onActivityResult(requestCode, resultCode, data);
        // Corresponding to edit button
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {










            }
        }

    }
}