package com.example.rssnewsreader;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.rssnewsreader.rss.Adapter.RecourcesAdapter;
import com.example.rssnewsreader.rss.Interface.ItemClickListener;
import com.example.rssnewsreader.rss.Μodel.DataInitialize;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListOfResources extends AppCompatActivity implements ItemClickListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    static String TAG="ListOfResources";
    private DatabaseReference mDatabase ;
    RecourcesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_resources);
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("List of Resources");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        });
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

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
                setAdapter(dataInitializes,getBaseContext());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void setAdapter(List<DataInitialize> list,Context context){
        adapter=new RecourcesAdapter(list,context,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view, int position) {

        DataInitialize clickedResource=adapter.getClickedItem(position);
        Intent intent = new Intent(this, RssViewActivity.class);
        intent.putExtra("data",clickedResource);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
