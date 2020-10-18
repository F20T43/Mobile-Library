package com.example.mobilelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditProfile extends AppCompatActivity {

    Button Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent old_intent = getIntent();
        Save = (Button) findViewById(R.id.save);










        //Save.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {







               // Intent new_intent = new Intent();

                // Pass the data back to Profile
                //new_intent.putExtra(          );
               // setResult(RESULT_OK, new_intent);
                //finish();











           // }
        //});
    }
}