package com.cfa.chyrellos.chatfa;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfa.chyrellos.chatfa.Generate.GenerateFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by chyrellos on 08.05.2017.
 */

public class ChatRoomActivity extends AppCompatActivity {
    String receiverUID;
    ImageView toolbarImage;
    TextView toolbarName,toolbarStatusDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);



        Intent intent = getIntent();
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference takenNames = database.getReference("TakenUserNames");
        DatabaseReference userPhoto = takenNames.child(ChatFriend.chatWith.toString());
        takenNames.child(ChatFriend.uID).child("status").setValue(true);

        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
        toolbarName =  (TextView) findViewById(R.id.toolbar_userName_view);
        toolbarStatusDate = (TextView) findViewById(R.id.toolbar_statusDate_view);

        toolbarName.setText(ChatFriend.chatWithName);
        userPhoto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getApplication(), "degisti", Toast.LENGTH_SHORT).show();
                if(dataSnapshot.child("userPhoto").getValue().toString().equals("null")){
                    int images = getResources().getIdentifier("com.cfa.chyrellos.chatfa:drawable/no_picture_user", null, null);
                    toolbarImage.setImageResource(images);
                }else{

                    byte[] decodedString = android.util.Base64.decode(dataSnapshot.child("userPhoto").getValue().toString(),android.util.Base64.DEFAULT);
                    InputStream inputStream  = new ByteArrayInputStream(decodedString);
                    Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
                    toolbarImage.setImageBitmap(bitmap);
                }

                if(dataSnapshot.child("status").getValue().toString().equals("false")){
                    toolbarStatusDate.setText("Son görülme:" + DateFormat.format(
                            "HH:mm dd:MM:yyyy"
                            ,(long)dataSnapshot.child("statusDate").getValue()));
                }else{
                    toolbarStatusDate.setText("Çevrim içi");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });











        Bundle bundle = new Bundle();
        bundle.putString("receiverUID",receiverUID);
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        new GenerateFragment(this,chatFragment,R.id.myChatRoomFrameLayout,"tag");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
