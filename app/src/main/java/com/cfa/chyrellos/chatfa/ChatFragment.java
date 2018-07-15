package com.cfa.chyrellos.chatfa;




import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;


import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cfa.chyrellos.chatfa.Model.ChatMessage;
import com.cfa.chyrellos.chatfa.Model.ChatRoom;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

/**
 * Created by chyrellos on 07.05.2017.
 */
public class ChatFragment extends Fragment {

    private static int SIGN_IN_REQUEST_CODE =1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
    FloatingActionButton fab;
    Activity thisActivity;
    private TableLayout tableLayout;
    private DatabaseReference mDatabase;
    private String receiverUID;
    Firebase reference1, reference2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        receiverUID = getArguments().getString("receiverUID");
        View view = inflater.inflate(R.layout.chat_fragment_layout, container, false);

        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thisActivity = getActivity();
        activity_main =(RelativeLayout) thisActivity.findViewById(R.id.chat_fragment_relative);
        fab = (FloatingActionButton) thisActivity.findViewById(R.id.fab);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Firebase.setAndroidContext(thisActivity);
        reference1 = new Firebase("https://test-d7257.firebaseio.com/messages/" + ChatFriend.username + "_" + ChatFriend.chatWith);
        reference2 = new Firebase("https://test-d7257.firebaseio.com/messages/" + ChatFriend.chatWith + "_" + ChatFriend.username);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                 EditText input = (EditText) thisActivity.findViewById(R.id.input);



                String roomID = RandomGenerator();
                FirebaseDatabase.getInstance().getReference().child("message").child(
                        ChatFriend.uID + "_" + ChatFriend.chatWith
                ).push().setValue( new ChatMessage(ChatFriend.uID + "_" + ChatFriend.chatWith
                        ,input.getText().toString(),ChatFriend.uID)
                );
                FirebaseDatabase.getInstance().getReference().child("message").child(
                        ChatFriend.chatWith + "_" + ChatFriend.uID
                ).push().setValue( new ChatMessage(ChatFriend.chatWith + "_" + ChatFriend.uID
                        ,input.getText().toString(),ChatFriend.uID)
                );

                FirebaseDatabase.getInstance().getReference().child("chatList").child(
                        ChatFriend.chatWith).child(ChatFriend.uID)
                        .setValue( new ChatRoom(ChatFriend.uID,ChatFriend.username,input.getText().toString())
                );

                FirebaseDatabase.getInstance().getReference().child("chatList").child(
                        ChatFriend.uID).child(ChatFriend.chatWith)
                        .setValue( new ChatRoom(ChatFriend.chatWith,ChatFriend.chatWithName,input.getText().toString())
                        );


                input.setText("");
            }
        });

        displayChatMessage();




    }

    private void displayChatMessage() {
        ListView listOfMessage = (ListView)thisActivity.findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(thisActivity,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference()
                .child("message").child(ChatFriend.uID + "_" + ChatFriend.chatWith)
                ) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                if(model.getMessageSenderUID() != null && ChatFriend.uID.equals(model.getMessageSenderUID())){
                    ((RelativeLayout) v.findViewById(R.id.green_layout)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.green_text)).setVisibility(View.VISIBLE);



                    ((TextView) v.findViewById(R.id.green_text)).setText(model.getMessageText());
                    ((TextView)v.findViewById(R.id.green_date)).setText(DateFormat.format(
                            "HH:mm"
                            ,model.getMessageTime()));
                    // hidden another view
                    ((RelativeLayout) v.findViewById(R.id.white_layout)).setVisibility(View.GONE);
                    ((TextView) v.findViewById(R.id.white_text)).setVisibility(View.GONE);

                }else{
                    ((RelativeLayout) v.findViewById(R.id.white_layout)).setVisibility(View.VISIBLE);
                    ((TextView) v.findViewById(R.id.white_text)).setVisibility(View.VISIBLE);




                    ((TextView) v.findViewById(R.id.white_text)).setText(model.getMessageText());
                    ((TextView)v.findViewById(R.id.white_date)).setText(DateFormat.format(
                            "HH:mm"
                            ,model.getMessageTime()));
                    // hidden another view
                    ((RelativeLayout) v.findViewById(R.id.green_layout)).setVisibility(View.GONE);
                    ((TextView) v.findViewById(R.id.green_text)).setVisibility(View.GONE);
                }
            }
        };
        listOfMessage.setAdapter(adapter);
    }


    public String RandomGenerator(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890!_-/=?".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }


}
