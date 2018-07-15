package com.cfa.chyrellos.chatfa;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cfa.chyrellos.chatfa.Model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


/**
 * Created by chyrellos on 08.05.2017.
 */

public class UserListFragment extends Fragment {
    private FirebaseListAdapter<User> adapter;
    RelativeLayout activity_main;
    Activity thisActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.user_fragment_layout, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thisActivity = getActivity();
        displayUserList();

        final ListView usrList = (ListView) thisActivity.findViewById(R.id.list_of_user);
        usrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView usrName = (TextView) view.findViewById(R.id.userName_txt);

                Toast.makeText(getActivity(),usrName.getTag().toString(),Toast.LENGTH_SHORT).show();
                User models = (User) usrName.getTag();

                ChatFriend.chatWith = models.getuID();
                ChatFriend.chatWithName = models.getUsername();
                ChatFriend.username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Intent intent = new Intent(thisActivity.getApplicationContext(),ChatRoomActivity.class);
                startActivity(intent);

            }
        });

    }

    private void displayUserList() {

        ListView listOfMessage = (ListView) thisActivity.findViewById(R.id.list_of_user);
        adapter = new FirebaseListAdapter<User>(thisActivity, User.class, R.layout.list_user, FirebaseDatabase.getInstance().getReference().child("TakenUserNames")) {
            @Override
            protected void populateView(View v, User model, int position) {


                    //Get references to the views of list item.xml
                    TextView messageText, messageUser, messageTime, usernameData,status,statusDate;
                    ImageView icon;



                    messageUser = (TextView) v.findViewById(R.id.userName_txt);
                    icon = (ImageView) v.findViewById(R.id.user_list_image);
                    status = (TextView) v.findViewById(R.id.user_login_status);
                    statusDate = (TextView) v.findViewById(R.id.user_statusDate);

                    if(model.getUserPhoto().equals("null")){
                        int images = getResources().getIdentifier("com.cfa.chyrellos.chatfa:drawable/no_picture_user", null, null);
                        icon.setImageResource(images);
                    }else{

                        byte[] decodedString = android.util.Base64.decode(model.getUserPhoto(),android.util.Base64.DEFAULT);
                        InputStream inputStream  = new ByteArrayInputStream(decodedString);
                        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
                        icon.setImageBitmap(bitmap);
                    }

                    if(model.isStatus()) {
                        status.setBackgroundResource(R.drawable.circle_status_true);
                        statusDate.setText("Çevrimiçi");
                    }else {
                        status.setBackgroundResource(R.drawable.circle_status_false);
                        statusDate.setText(DateFormat.format("HH:mm dd:MM:yyyy ", model.getStatusDate()));
                    }

                    messageUser.setText(model.getUsername().toString());
                    messageUser.setTag(model);
                }

        };
        listOfMessage.setAdapter(adapter);
    }

}
