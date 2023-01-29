package com.malfaang.e_culture_tool_a.login;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context=context;
        this.totalTabs =totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    //Collegamento dei due fragment, permette lo switch da parte dell'utente
    public Fragment getItem(int position){
        switch (position){
            case 0:
                LoginFragment loginTabFragment=new LoginFragment();
                return loginTabFragment;
            case 1:
                SignupFragment signupTabFragment=new SignupFragment();
                return signupTabFragment;
            default:
                return null;

        }
    }

}
