package com.example.rssnewsreader.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.rssnewsreader.R;
import com.example.rssnewsreader.rss.Adapter.FeedAdapter;
import com.example.rssnewsreader.rss.Interface.ItemClickListener;
import com.example.rssnewsreader.rss.Μodel.DataInitialize;
import com.example.rssnewsreader.rss.Μodel.Item;
import com.example.rssnewsreader.rss.Μodel.RSSObject;
import com.example.rssnewsreader.rss.Μodel.UserReads;
import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements ItemClickListener {
    RecyclerView recyclerView;
    RSSObject rssObject;
    public static String TAG = "FRAGMENT LAST SEEN ACTIVITY";
    //private static final String ARG_SECTION_NUMBER = "section_number";
    private DatabaseReference mDatabase ;
    //private PageViewModel pageViewModel;

    private View view;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        //bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home_page, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewhm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getLastNews();
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onClick(View view, int position) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.getItems().get(position).getLink()));
        startActivity(browserIntent);
    }

    public void createRecyclerView(RSSObject rssObject, Context context) {
        Collections.reverse(rssObject.getItems());
        FeedAdapter adapter = new FeedAdapter(rssObject, context, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void getLastNews() {
        List<Item> dataInitializes = new ArrayList<Item>();
        Profile profile = Profile.getCurrentProfile();
        String fbID = profile.getId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("userReads").child(fbID).orderByChild("time").limitToLast(20);
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
}