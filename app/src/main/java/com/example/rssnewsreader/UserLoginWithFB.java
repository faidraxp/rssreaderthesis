package com.example.rssnewsreader;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.rssnewsreader.sqlite.FirebaseConfig;
import com.example.rssnewsreader.sqlite.RecommendationSystem;
import com.example.rssnewsreader.userprofile.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;


public class UserLoginWithFB extends AppCompatActivity {
public static String TAG="USER LOGIN FB";

    CallbackManager callbackManager = CallbackManager.Factory.create();
    FirebaseConfig config;
    private ProfileTracker mProfileTracker;
    private static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_with_fb);

        setAutoLogAppEventsEnabled(true);
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        setAdvertiserIDCollectionEnabled(true);
        checkLogin();
        Log.d(TAG,"apptest 1");
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG,"apptest 3"+loginResult);
                        FirebaseConfig config= new FirebaseConfig();
                        User user;
                        user=getProfile();
                        config.addUser(user);
                        checkLogin();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TAG,"cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TAG,"error");
                        Log.d(TAG,exception.getLocalizedMessage());
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"apptest 2");
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void checkLogin(){

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn==true){

            Log.d(TAG,"is logged in");
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    public void getGraphAccess(){
        User user=new User();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                (object, response) -> {
                    try {
                        user.setName(object.getString("name"));
                        user.setFbID(object.getString("id"));
                        user.setAge("0");
                        user.setGender("N/A");

                        config.addUser(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
        Log.v(TAG,"user test  "+user);


        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
        Log.v(TAG,"apptest 4 "+request);
        Log.v(TAG,"apptest 5 "+parameters);
    }

    public User getProfile(){
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
            config=new FirebaseConfig();
            user.setName(profile.getName());
            user.setFbID(profile.getId());
            user.setAge("0");
            user.setGender("N/A");
            //config.addUser(user);
            Log.v("facebook - profile", profile.getFirstName());
        }
        return user;
    }
}
