package com.example.rssnewsreader.sqlite;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.rssnewsreader.rss.Μodel.DataInitialize;
import com.example.rssnewsreader.rss.Μodel.Feed;
import com.example.rssnewsreader.rss.Μodel.Item;
import com.example.rssnewsreader.rss.Μodel.UserReads;
import com.example.rssnewsreader.userprofile.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirebaseConfig {

    static  String TAG="FIREBASE APP TEST";
    private DatabaseReference  mDatabase ;

    public void addUser(User user){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(user!=null){
            mDatabase.child("users").child(user.getFbID()).setValue(user);
        }
    }

    public void addResources(DataInitialize data){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(data!=null){
            mDatabase.child("news").push().setValue(data);
        }

    }
    public void addNews(Item item){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(item.getId()!=null){
            mDatabase.child("feed").child(item.getId()).setValue(item);
        }
        else if(item!=null){
            mDatabase.child("feed").push().setValue(item);

        }
    }
    public void addNewsUser(UserReads userReads){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(userReads!=null){
            Log.v(TAG,"app test success ");
            mDatabase.child("userReads").child(userReads.getFbID()).child(userReads.getItem().getId()).setValue(userReads);
        }
    }
    public void addRecommendedNews(UserReads userReads){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(userReads!=null){
            Log.v(TAG,"app test success ");
            mDatabase.child("recommendedNews").child(userReads.getFbID()).push().setValue(userReads);
        }
    }
    public void getUsers(String fbID){
        List<User> users=new ArrayList<User>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query=mDatabase.child("users").orderByChild("fbID").equalTo(fbID);
        query.keepSynced(true);
        // Read from the database
        query. addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user!=null){
                        users.add(user);
                    }
                }
                Log.d(TAG, "Value is: " + users);

                //return users;
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getResources(){
        List<DataInitialize> dataInitializes =new ArrayList<DataInitialize>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("news");
        mDatabase. addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    DataInitialize data = userSnapshot.getValue(DataInitialize.class);
                    if(data!=null){
                        dataInitializes.add(data);
                    }
                }
                Log.d(TAG, "Value is: " + dataInitializes);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
