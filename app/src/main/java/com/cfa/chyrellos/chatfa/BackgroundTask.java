package com.cfa.chyrellos.chatfa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cfa.chyrellos.chatfa.Model.User;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chyrellos on 10.05.2017.
 */

public class BackgroundTask extends AsyncTask<String,String,String> {

Activity context;
//    ProgressDialog pDialog;
    public BackgroundTask(Activity context){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(String s) {
        //pDialog.dismiss();

    }


    @Override
    protected String doInBackground(String... params) {
        FirebaseUser name = FirebaseAuth.getInstance().getCurrentUser();

        ChatFriend.username = name.getDisplayName().toString();
        ChatFriend.email = name.getEmail().toString();
        ChatFriend.uID = name.getUid().toString();

        SharedPreferences savedValue = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String image = savedValue.getString("userPhoto","null");
        FirebaseDatabase database  = FirebaseDatabase.getInstance();


        DatabaseReference takenNames = database.getReference("TakenUserNames");
        ChatFriend.userPhoto = takenNames.child(ChatFriend.uID).child("userPhoto").getKey().toString();



            takenNames.child(ChatFriend.uID).setValue(new User(ChatFriend.uID,
                    ChatFriend.username,
                    ChatFriend.email,true,image.toString()));





      return null;
    }



}
