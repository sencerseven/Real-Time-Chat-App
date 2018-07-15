package com.cfa.chyrellos.chatfa;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfa.chyrellos.chatfa.Model.ChatRoom;
import com.cfa.chyrellos.chatfa.Model.User;
import com.firebase.client.utilities.Base64;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by chyrellos on 08.05.2017.
 */

public class ChatListFragment extends Fragment {
    private FirebaseListAdapter<ChatRoom> adapter;
    RelativeLayout activity_main;
    Activity thisActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.chat_list_fragment_layout, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thisActivity = getActivity();
        displayUserList();


        final ListView usrList = (ListView) thisActivity.findViewById(R.id.list_of_chat);

       /* usrNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(thisActivity,"okey",Toast.LENGTH_SHORT).show();
            }
        });*/

        usrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView usrName = (TextView) view.findViewById(R.id.chatName_txt);
                //TextView usrData = (TextView) view.findViewById(R.id.usrdata);


                ChatRoom models = (ChatRoom) usrName.getTag();

                ChatFriend.chatWith = models.getUserUID();
                ChatFriend.chatWithName = models.getUserName();
                ChatFriend.username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Intent intent = new Intent(thisActivity.getApplicationContext(),ChatRoomActivity.class);
                startActivity(intent);

            }
        });

    }


    private void displayUserList()  {

        ListView listOfMessage = (ListView) thisActivity.findViewById(R.id.list_of_chat);



        adapter = new FirebaseListAdapter<ChatRoom>(thisActivity, ChatRoom.class, R.layout.list_chat,
                FirebaseDatabase.getInstance().getReference().child("chatList").child(ChatFriend.uID)) {
            TextView messageText, messageUser, messageTime;
            ImageView icon;



            @Override
            protected void populateView(View v, ChatRoom model, int position) {
                messageUser = (TextView) v.findViewById(R.id.chatName_txt);
                icon = (ImageView) v.findViewById(R.id.text_view_user_image);

                messageTime = (TextView) v.findViewById(R.id.chatTime_txt);
                messageText = (TextView) v.findViewById(R.id.Chat_message_txt);
                FirebaseDatabase database  = FirebaseDatabase.getInstance();

                DatabaseReference takenNames = database.getReference("TakenUserNames");
                DatabaseReference userPhoto = takenNames.child(model.getUserUID()).child("userPhoto");



                userPhoto.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue().toString().equals("null")){
                            int images = getResources().getIdentifier("com.cfa.chyrellos.chatfa:drawable/no_picture_user", null, null);
                            icon.setImageResource(images);
                        }else{

                            byte[] decodedString = android.util.Base64.decode(dataSnapshot.getValue().toString(),android.util.Base64.DEFAULT);
                            InputStream inputStream  = new ByteArrayInputStream(decodedString);
                            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
                            icon.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });








                    messageText.setText(model.getLastMessage());
                    messageUser.setText(model.getUserName());
                    messageUser.setTag(model);
                    messageTime.setText(DateFormat.format("HH:mm", model.getMessageTime()));
                }

        };
        listOfMessage.setAdapter(adapter);
    }


}
