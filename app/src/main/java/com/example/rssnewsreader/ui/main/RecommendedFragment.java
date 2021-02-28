package com.example.rssnewsreader.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rssnewsreader.R;
import com.example.rssnewsreader.RssViewActivity;
import com.example.rssnewsreader.rss.Adapter.FeedAdapter;
import com.example.rssnewsreader.rss.Interface.ItemClickListener;
import com.example.rssnewsreader.rss.Μodel.DataInitialize;
import com.example.rssnewsreader.rss.Μodel.Item;
import com.example.rssnewsreader.rss.Μodel.RSSObject;
import com.example.rssnewsreader.rss.Μodel.UserReads;
import com.example.rssnewsreader.sqlite.FirebaseConfig;
import com.example.rssnewsreader.sqlite.RecommendationSystem;
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
import java.util.Collections;
import java.util.List;

public class RecommendedFragment  extends Fragment implements ItemClickListener {
    private View view;
    RecyclerView recyclerView;
    RSSObject rssObject;
    public static String TAG = "FRAGMENT RECOMMENDED ACTIVITY";
    private DatabaseReference mDatabase ;
    private ProfileTracker mProfileTracker;
    ProgressDialog mDialog;
    public RecommendedFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_recommended, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewrec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecommendationSystem recommendationSystem=new RecommendationSystem();
        recommendationSystem.setRecommendations();
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("Please wait...");
        mDialog.show();
        getRecommendedNews();
        return view;
    }
    @Override
    public void onClick(View view, int position) {
        Log.d(TAG, "rss item clicked" + position);
        addItem(rssObject.getItems().get(position));
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.getItems().get(position).getLink()));
        startActivity(browserIntent);
    }
    public void createRecyclerView(RSSObject rssObject, Context context) {
        Collections.reverse(rssObject.getItems());
        FeedAdapter adapter = new FeedAdapter(rssObject, context, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mDialog.dismiss();

    }
    public void getRecommendedNews(){
        List<Item> dataInitializes = new ArrayList<Item>();
        Profile profile = Profile.getCurrentProfile();
        String fbID = profile.getId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("recommendedNews").child(fbID).orderByChild("time").limitToLast(20);
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.w(TAG, "works."+getActivity());
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    UserReads userReads =userSnapshot.getValue(UserReads.class);
                    dataInitializes.add(userReads.getItem());
                }
                rssObject=new RSSObject();
                rssObject.setItems(dataInitializes);
                createRecyclerView(rssObject, getActivity());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void addItem(Item item){
        String fbID=getUserID();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UserReads userReads=new UserReads();
        userReads.setFbID(fbID);
        userReads.setItem(item);
        userReads.setTime(timestamp.getTime());
        FirebaseConfig config=new FirebaseConfig();
        config.addNewsUser(userReads);
    }
    public String getUserID(){
        String fbID;
        Profile profile = Profile.getCurrentProfile();
        fbID=profile.getId();
        return  fbID;
    }
}
