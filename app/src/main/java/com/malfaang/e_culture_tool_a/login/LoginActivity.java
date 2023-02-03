package com.malfaang.e_culture_tool_a.login;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.malfaang.e_culture_tool_a.R;

public class LoginActivity extends AppCompatActivity {
    private TabLayout tablayout;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        tablayout =findViewById(R.id.tab_layout);
        viewpager =findViewById(R.id.view_pager);
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.login)));
        tablayout.addTab(tablayout.newTab().setText(getResources().getString(R.string.registrati)));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final LoginAdapter adapter= new LoginAdapter(getSupportFragmentManager(),this, tablayout.getTabCount());
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablayout));
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
