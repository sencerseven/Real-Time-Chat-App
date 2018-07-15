package com.cfa.chyrellos.chatfa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;



/**
 * Created by chyrellos on 12.05.2017.
 */

public class UserProfileFragment extends Fragment {

    private ImageView imageView;
    private Button editButton;
    Activity fragmentActivity;
    private static final int PICK_IMAGE = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.user_profile_fragment_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentActivity = getActivity();
        imageView = (ImageView) fragmentActivity.findViewById(R.id.profile_image);
        editButton = (Button) fragmentActivity.findViewById(R.id.profile_edit_image);
        SharedPreferences savedValue = PreferenceManager.getDefaultSharedPreferences(fragmentActivity.getApplicationContext());
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

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             openGallery();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK ) {
            if (requestCode == PICK_IMAGE){

                try{
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = fragmentActivity.getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String encoded = Base64.encodeToString(byteArray,Base64.DEFAULT);


                    FirebaseDatabase database  = FirebaseDatabase.getInstance();
                    DatabaseReference takenNames = database.getReference("TakenUserNames");
                    takenNames.child(ChatFriend.uID).child("userPhoto").setValue(encoded);
                    SharedPreferences saveValue = PreferenceManager.getDefaultSharedPreferences(fragmentActivity.getApplicationContext());
                    SharedPreferences.Editor editor = saveValue.edit();
                    editor.putString("userPhoto",encoded);
                    editor.commit();
                    ChatFriend.userPhoto = encoded;
                    imageView.setImageBitmap(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(fragmentActivity,"hata pick image",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(fragmentActivity,"hata result ok",Toast.LENGTH_SHORT).show();
        }
    }
}
