package com.malfaang.e_culture_tool_a.addElements;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AddElementsAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public AddElementsAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    //Collegamento dei due fragment, permette lo switch da parte dell'utente
    public Fragment getItem(int position){
        switch (position){
            case 0:
                AreaFragment areaTabFragment = new AreaFragment();
                return areaTabFragment;
            case 1:
                ItemFragment itemTabFragment =new ItemFragment();
                return itemTabFragment;
            default:
                return null;

        }
    }
}