package com.cfa.chyrellos.chatfa;

/**
 * Created by Ceylan on 25.12.2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class GeneralScreenPageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private String[] tabTitles = new String[]{"SOHBETLER", "KİŞİLER"};
    public GeneralScreenPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ChatListFragment profile=new ChatListFragment();
                return profile;
            case 1:
                UserListFragment profil=new UserListFragment();
                return profil;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
