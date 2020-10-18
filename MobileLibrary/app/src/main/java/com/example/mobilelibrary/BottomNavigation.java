package com.example.mobilelibrary;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigation implements BottomNavigationView.OnNavigationItemSelectedListener{
    Context context;

    public BottomNavigation(Context context){
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;

        switch (item.getItemId()){
            case R.id.home:
                if (context.getClass() == OwnerBook.class){break;}
                i = new Intent(context, OwnerBook.class);
                context.startActivity(i);
                break;
            case R.id.borrow:
                if (context.getClass() == BorrowerBook.class){break;}
                i = new Intent(context, BorrowerBook.class);
                context.startActivity(i);
                break;
            case R.id.notification:
                if (context.getClass() == Notification.class){break;}
                i = new Intent(context, Notification.class);
                context.startActivity(i);
                break;
            case R.id.profile:
                if (context.getClass() == Profile.class){break;}
                i = new Intent(context, Profile.class);
                context.startActivity(i);
                break;
        }
        return true;
    }
}
