package com.cfa.chyrellos.chatfa.Generate;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.cfa.chyrellos.chatfa.ChatFragment;
import com.cfa.chyrellos.chatfa.MainActivity;

// Created by Sencer Seven on 25.12.2015.
public class GenerateFragment {

    FragmentManager manager;
    FragmentTransaction transaction;


   public GenerateFragment(Activity activity, Fragment fragment, int contentViewId, String tag){
       manager = activity.getFragmentManager();
       transaction = manager.beginTransaction();
       transaction.replace(contentViewId, fragment, tag);
       transaction.commit();
   }

}
