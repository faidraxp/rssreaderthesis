package com.example.rssnewsreader;

import com.example.rssnewsreader.sqlite.FirebaseConfig;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rssnewsreader.rss.Μodel.DataInitialize;

import java.net.URL;

public class AddRecources extends AppCompatActivity {
    Toolbar toolbar;
    Button button;
    TextInputLayout siteName;
    TextInputLayout siteUrl;
    static String TAG="ADD RESOURCES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recources);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Resources");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),HomePage.class));
            finish();
        });
        button=findViewById(R.id.button2);
        siteName=findViewById(R.id.sitename);
        siteUrl=findViewById(R.id.siteurl);

    }
    public void onClick(View view){

        if(siteUrl.getEditText().getText().toString()!=null && siteName.getEditText().getText().toString()!=null && isValid(siteUrl.getEditText().getText().toString())){
            DataInitialize data=new DataInitialize();
            data.setLink(siteUrl.getEditText().getText().toString());
            data.setSearchName(siteName.getEditText().getText().toString());
            FirebaseConfig firebaseConfig=new FirebaseConfig();
            firebaseConfig.addResources(data);
            siteName.getEditText().getText().clear();
            siteUrl.getEditText().getText().clear();
            Toast.makeText(this,"it's added "+data.getSearchName(),Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this,"please add  correct values",Toast.LENGTH_LONG).show();
        }

    }
    public static boolean isValid(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }
        catch (Exception e) {
            Log.d("test exception",e.toString());
            return false;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
