package com.cfa.chyrellos.chatfa.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cfa.chyrellos.chatfa.BackgroundTask;
import com.cfa.chyrellos.chatfa.MainActivity;
import com.cfa.chyrellos.chatfa.Model.User;
import com.cfa.chyrellos.chatfa.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chyrellos on 08.05.2017.
 */

public class UserLogin extends Activity{
    private static int SIGN_IN_REQUEST_CODE =1;
    RelativeLayout activity_main;
    private Firebase firebaseRef;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Snackbar.make(activity_main,"Successfully signed in. Welcome!",Snackbar.LENGTH_SHORT).show();





              /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("username",FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());
                editor.commit();*/


                Intent intent = new Intent(this,UserLogin.class);
                startActivity(intent);



                finish();
            }else{
                Snackbar.make(activity_main,"We couldn't sign you in. Please try again later",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        activity_main = (RelativeLayout) findViewById(R.id.login_secreen);

        boolean nullDisplayName;
        try{
            if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() == null){
                nullDisplayName = true;
            }else{
                nullDisplayName = false;
            }
        }catch (Exception e){
            nullDisplayName = true;
        }


        if(nullDisplayName)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);


        }
        else
        {

            Snackbar.make(activity_main,"Welcome" + FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();


            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

        }


    }

}
