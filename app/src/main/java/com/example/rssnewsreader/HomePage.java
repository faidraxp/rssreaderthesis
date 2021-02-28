package com.example.rssnewsreader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rssnewsreader.rss.Μodel.DataInitialize;
import com.example.rssnewsreader.sqlite.FirebaseConfig;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rssnewsreader.ui.main.PlaceholderFragment;
import com.example.rssnewsreader.ui.main.RecommendedFragment;
import com.example.rssnewsreader.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

public class HomePage extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Home Page");
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new RecommendedFragment(),"Recommended News");
        sectionsPagerAdapter.addFragment(new PlaceholderFragment(),"Last Read News");
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("test back","test id "+item.getItemId());

        if (item.getItemId() == R.id.seach_view) {
            startActivity(new Intent(getApplicationContext(),ListOfResources.class));
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.add_news){
            startActivity(new Intent(getApplicationContext(),AddRecources.class));
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.logout){
            setAutoLogAppEventsEnabled(false);
            FacebookSdk.setAutoInitEnabled(false);
//            FacebookSdk.fullyInitialize();
//            setAdvertiserIDCollectionEnabled(false);
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getApplicationContext(),UserLoginWithFB.class));
            finish();
            return true;
        }

        return(super.onOptionsItemSelected(item));
    }
}