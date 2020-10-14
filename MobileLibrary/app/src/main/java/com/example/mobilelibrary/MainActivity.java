package com.example.mobilelibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button Owner, Borrower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Owner = (Button) findViewById(R.id.owner);
        Borrower = (Button) findViewById(R.id.borrower);

        Owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OwnerLogin.class);
                startActivity(i);
            }
        });

        Borrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, BorrowerLogin.class);
                startActivity(i);
            }
        });

    }
}