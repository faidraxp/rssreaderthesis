package com.example.rssnewsreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.rssnewsreader.rss.Adapter.FeedAdapter;
import com.example.rssnewsreader.rss.Common.HTTPDataHandler;
import com.example.rssnewsreader.rss.Interface.ItemClickListener;
import com.example.rssnewsreader.rss.Μodel.DataInitialize;
import com.example.rssnewsreader.rss.Μodel.Item;
import com.example.rssnewsreader.rss.Μodel.RSSObject;
import com.example.rssnewsreader.rss.Μodel.UserReads;
import com.example.rssnewsreader.sqlite.FirebaseConfig;
import com.example.rssnewsreader.userprofile.User;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class RssViewActivity extends AppCompatActivity implements ItemClickListener {
    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSObject rssObject;
    public static String TAG = "RSS FEED ACTIVITY";
    DataInitialize  data;
    //RSS link
    private final String RSS_to_Json_API = "https://api.rss2json.com/v1/api.json?rss_url=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getIntent().getExtras() != null) {
            data= (DataInitialize) getIntent().getSerializableExtra("data");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_view);

        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("News");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),ListOfResources.class));
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadRSS();

    }

    private void loadRSS() {
        AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {

            ProgressDialog mDialog = new ProgressDialog(RssViewActivity.this);

            @Override
            protected void onPreExecute() {
                mDialog.setMessage("Please wait...");
                mDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.GetHTTPData(params[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                mDialog.dismiss();
                rssObject = new Gson().fromJson(s, RSSObject.class);
                createRecyclerView(rssObject, getBaseContext());
            }
        };

        StringBuilder url_get_data = new StringBuilder(RSS_to_Json_API);
        url_get_data.append(data.getLink());
        loadRSSAsync.execute(url_get_data.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("test back","test id "+item.getItemId());
        if (item.getItemId() == R.id.menu_refresh) {
            loadRSS();
        }
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onClick(View v, int position) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.getItems().get(position).getLink()));
        addItem(rssObject.getItems().get(position));
        startActivity(browserIntent);
    }

    public void createRecyclerView(RSSObject rssObject, Context context) {
        Collections.sort(rssObject.getItems());
        Collections.reverse(rssObject.getItems());
        addItems(rssObject.getItems());
        FeedAdapter adapter = new FeedAdapter(rssObject, context, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public String getUserID(){
        String fbID;
        Profile profile = Profile.getCurrentProfile();
        fbID=profile.getId();
        return  fbID;
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
    public void addItems(List<Item> items){
        for(Item item : items){
            String title=item.getTitle().replaceAll("[ .#$]","-");
            String date=item.getPubDate().replace(" ","-");;
            item.setId(title+"-"+date);
            FirebaseConfig config=new FirebaseConfig();
            config.addNews(item);
        }

    }


}



