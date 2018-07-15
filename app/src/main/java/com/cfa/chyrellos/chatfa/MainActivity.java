package com.cfa.chyrellos.chatfa;





import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cfa.chyrellos.chatfa.Generate.GenerateFragment;
import com.cfa.chyrellos.chatfa.Login.UserLogin;
import com.cfa.chyrellos.chatfa.Model.User;
import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final DrawerLayout drawerLay = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(drawerLay,"You have been signed out",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    private Firebase firebaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                BackgroundTask bTask = new BackgroundTask(MainActivity.this);
                bTask.execute();
            }


        });
        Thread th = new Thread();
        try {
            th.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        GeneralScreenFragment chatFragment = new GeneralScreenFragment();
        new GenerateFragment(this,chatFragment,R.id.myFrameLayout,"tag");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        TextView txt =(TextView) navigationView.getHeaderView(0).findViewById(R.id.txtname);
        ImageView imageView =(ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_profile_image);
        txt.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString());



        SharedPreferences savedValue = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String image = savedValue.getString("userPhoto","null");

        if(image.equals("null")){
            int images = getResources().getIdentifier("com.cfa.chyrellos.chatfa:drawable/no_picture_user", null, null);
            imageView.setImageResource(images);
        }else{

            byte[] decodedString = android.util.Base64.decode(image,android.util.Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(decodedString);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        }




        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                1);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {


                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference takenNames = database.getReference("TakenUserNames");
        takenNames.child(ChatFriend.uID).child("status").setValue(false);
        database.getReference("TakenUserNames");
        takenNames.child(ChatFriend.uID).child("statusDate").setValue(new Date().getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference takenNames = database.getReference("TakenUserNames");
        takenNames.child(ChatFriend.uID).child("status").setValue(true);
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference takenNames = database.getReference("TakenUserNames");
        takenNames.child(ChatFriend.uID).child("status").setValue(false);
        database.getReference("TakenUserNames");
        takenNames.child(ChatFriend.uID).child("statusDate").setValue(new Date().getTime());
    }
*/
    @Override
    protected void onPause() {
        super.onPause();
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference takenNames = database.getReference("TakenUserNames");
        takenNames.child(ChatFriend.uID).child("status").setValue(false);
        database.getReference("TakenUserNames");
        takenNames.child(ChatFriend.uID).child("statusDate").setValue(new Date().getTime());

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profil) {
            new GenerateFragment(this,new UserProfileFragment(),R.id.myFrameLayout,"fragment_profil");
        }else if(id == R.id.nav_mesaj){
            new GenerateFragment(this,new GeneralScreenFragment(),R.id.myFrameLayout,"fragment_mesaj");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
