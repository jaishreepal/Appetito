package com.foodapp.appetito.appetito;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton loginButton;
    ProfilePictureView profilePictureView;


    //    TextView textView, tfirst_name, tlast_name, temail;
    Button register_button;
    String userId;
    FacebookCallback<LoginResult> callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager= CallbackManager.Factory.create();
        loginButton=(LoginButton)findViewById(R.id.fb_login);
        loginButton.setReadPermissions("email","public_profile");//2
//        textView=(TextView)findViewById(R.id.text);
        register_button=(Button) findViewById(R.id.reg_btn);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                userId=loginResult.getAccessToken().getUserId();
//                textView.setText("Login Successful\n"+ loginResult.getAccessToken().getUserId()+"\n"+loginResult.getAccessToken().getToken());
                GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {//2
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        displayUserInfo(object);//2
                    }
                });
                profilePictureView = (ProfilePictureView)findViewById(R.id.profile_picture);
                profilePictureView.setProfileId(userId);

                Bundle parameters = new Bundle();//2
                parameters.putString("fields", "first_name, last_name, email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

//                textView.setText("Login Cancelled");
                Toast.makeText(MainActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
            }
        });


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }



    public void displayUserInfo(JSONObject object)//2
    {
        String first_name = null,last_name = null,email = null;
        ProfilePictureView profile_icon;
        try {
            first_name = object.getString("first_name");
            last_name = object.getString("last_name");
            email = object.getString("email");
//            profile_icon = object.getString("profile_icon");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        tfirst_name=(TextView) findViewById(R.id.first_name);
//        tlast_name=(TextView) findViewById(R.id.last_name);
//        temail=(TextView) findViewById(R.id.email);
//
//        tfirst_name.setText("First name : "+ first_name);
//        tlast_name.setText("Last name : "+last_name);
//        temail.setText("Email id : "+email);

        Intent intent=new Intent(getApplicationContext(),Register.class);
        intent.putExtra("first_name",first_name);
        intent.putExtra("last_name", last_name);
        intent.putExtra("email",email);
        startActivity(intent);

    }
}