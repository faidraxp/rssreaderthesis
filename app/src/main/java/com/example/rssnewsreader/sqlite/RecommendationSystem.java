package com.example.rssnewsreader.sqlite;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.rssnewsreader.rss.Μodel.Item;
import com.example.rssnewsreader.rss.Μodel.RSSObject;
import com.example.rssnewsreader.rss.Μodel.UserReads;
import com.example.rssnewsreader.userprofile.User;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecommendationSystem {
    public static String TAG = "FRAGMENT RECOMMENDED ACTIVITY";
    private DatabaseReference mDatabase ;
    private ProfileTracker mProfileTracker;
    private List<Item> userLastReads;
    private List<Item> userRecLastReads;
    private List<Item> allNews;
    private List<User> users;


    public RecommendationSystem() {
        latestNews();
        getUsers();
        getAllNews();

    }
    public void setRecommendations(){

        if(userRecLastReads!=null){
            compareLists(userLastReads,userRecLastReads);
        }
        if(allNews!=null){
            compareLists(userLastReads,allNews);
        }

    }
    private void latestNews(){
        userLastReads = new ArrayList<Item>();
        Profile profile = Profile.getCurrentProfile();
        String fbID = profile.getId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("userReads").child(fbID);
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG, "test1.");

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    UserReads userReads =userSnapshot.getValue(UserReads.class);
                    userLastReads.add(userReads.getItem());
                }
                Log.w(TAG, "test1."+userLastReads);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());

            }
        });
    }
    private void getUsers(){
        User user=getProfile();
        users=new ArrayList<User>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query=mDatabase.child("users").orderByChild("age").equalTo(user.getAge());;
        query.keepSynced(true);
        query. addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "test2.");
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user2 = userSnapshot.getValue(User.class);
                    if((user2!=null)&& !(user2.getFbID().equals(user.getFbID())) &&(user2.getGender().equals(user.getGender())) ){

                        getNews(user2.getFbID());
                    }

                }
                Log.d(TAG, "Value is: " + users);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    private User getProfile(){
        User user=new User();
        if(Profile.getCurrentProfile() == null) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                    Log.v("facebook - profile", currentProfile.getFirstName());
                    mProfileTracker.stopTracking();
                }
            };

        }
        else {
            Profile profile = Profile.getCurrentProfile();
            user.setName(profile.getName());
            user.setFbID(profile.getId());
            user.setAge("0");
            user.setGender("N/A");
            Log.v("facebook - profile", profile.getFirstName());
        }
        return user;
    }
    private void compareLists(List<Item> userList,List<Item> recUserList){
        if (!userList.equals(recUserList)){
            recUserList.removeAll(userList);
            if (userList.size()>0&&userList.size()!=0){
                float percentage =recUserList.size()/userList.size();
                if (Float.compare(percentage,0.4f) == 0||Float.compare(percentage, 0.4f) < 0) {
                    //add to user recommended news list
                    System.out.println("f1=f2");
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    Profile profile = Profile.getCurrentProfile();
                    String fbID = profile.getId();
                    if(fbID!=null){
                        Log.v(TAG,"app test success ");
                        addItems(recUserList,fbID);
                    }
                }
            }

        }
    }
    private void compareLists(List<Item> userList,List<Item> recUserList,boolean nousers){
        if (!userList.equals(recUserList)){
            recUserList.removeAll(userList);
            if (userList.size()>0&&userList.size()!=0){

                mDatabase = FirebaseDatabase.getInstance().getReference();
                Profile profile = Profile.getCurrentProfile();
                String fbID = profile.getId();
                if(fbID!=null){
                    Log.v(TAG,"app test success ");
                    addItems(recUserList,fbID);

                }
            }
        }
    }
    private void getNews(String fbId) {
        userRecLastReads = new ArrayList<Item>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("userReads").child(fbId);
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    UserReads userReads =userSnapshot.getValue(UserReads.class);
                    userRecLastReads.add(userReads.getItem());
                }
                if(userRecLastReads!=null){
                    compareLists(userLastReads,userRecLastReads);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());

            }
        });
    }
    private void getAllNews(){
        allNews=new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("feed").orderByChild("pubDate").limitToLast(20);;
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG, "test1.");

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Item news =userSnapshot.getValue(Item.class);
                    allNews.add(news);
                }

                if(allNews!=null&&userRecLastReads.size()<allNews.size()){
                    allNews.removeAll(userRecLastReads);
                    compareLists(userLastReads,allNews,true);
                }
                Log.w(TAG, "test1."+allNews);
                Log.d(TAG, "Value is: " + userLastReads);
                Log.d(TAG, "Value is: " + userRecLastReads);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());

            }
        });
    }
    private void addItems(List<Item> items,String fbID){
        for(Item item : items){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            UserReads userReads=new UserReads();
            userReads.setFbID(fbID);
            userReads.setItem(item);
            userReads.setTime(timestamp.getTime());
            Log.v(TAG,"app test success ");
            if(fbID!=null&&item.getId()!=null){
                mDatabase.child("recommendedNews").child(fbID).child(item.getId()).setValue(userReads);
            }

        }


    }
}
