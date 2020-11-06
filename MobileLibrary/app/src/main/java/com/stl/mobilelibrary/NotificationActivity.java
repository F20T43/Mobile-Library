package com.stl.mobilelibrary;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The notification activity which show all notifications
 */
public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = NotificationActivity.class.getSimpleName();
    private NotificationRecyclerAdapter adapter;
    private Context mContext;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private RecyclerView recyclerView;

    /**
     * Bind UI elements
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(new NavigationHandler(this));
        navigation.setSelectedItemId(R.id.notice);

        recyclerView = findViewById(R.id.notification_recycler_view);
        mContext = getApplicationContext();

        adapter = new NotificationRecyclerAdapter(this, notifications);

        getNotifications();
        initRecyclerView();
    }



    private void getNotifications() {
        final DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.getDatabaseReference().child("Notifications")
                .orderByChild("userName")
                .equalTo(CurrentUser.getInstance().getUserName())
                .addChildEventListener(new ChildEventListener() {
                    /**
                     * When a new child is added, add it to the list of notifications
                     * @param dataSnapshot: the current snapshot
                     * @param s: the ID
                     */
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Notification notification = dataSnapshot.getValue(Notification.class);
                        notifications.add(notification);
                        adapter.notifyDataSetChanged();
                    }

                    /**
                     * When a child is changes update them
                     * @param dataSnapshot: the current snapshot
                     * @param s: the ID
                     */
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Notification notification = dataSnapshot.getValue(Notification.class);
                        Integer idToReplace = null;
                        for (int i = 0; i < notifications.size(); i++){
                            if (notifications.get(i).getUuid().equals(notification.getUuid())){
                                idToReplace = i;
                                break;
                            }
                        }

                        if (idToReplace != null){
                            notifications.set(idToReplace,notification);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    /**
                     * If a child is deleted delete them from the list of our notifications
                     * @param dataSnapshot: the current snapshot
                     */
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Book book = dataSnapshot.getValue(Book.class);
                        Integer idToRemove = null;
                        for (int i = 0; i < notifications.size(); i++){
                            if (notifications.get(i).getUuid().equals(book.getUuid())){
                                idToRemove = i;
                                break;
                            }
                        }

                        if (idToRemove != null){
                            notifications.remove(notifications.get(idToRemove));
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }


    private void initRecyclerView(){
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onBackPressed() {
    }

}
