package com.malfaang.e_culture_tool_a.addElements;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.malfaang.e_culture_tool_a.R;


public class AddElements extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //Abbinamento del layout con gli oggetti
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_elements);
        ActionBar ab = getSupportActionBar();


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.Areas));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.Item));
        //Riempe l'intero contenitore sia orrizzontalmente che verticalmente
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final AddElementsAdapter adapter = new AddElementsAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Evidenziamento tab selezionata
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    // Chiamata quando viene triggherata invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (viewPager.getCurrentItem()) {
            case 0:
                menu.findItem(R.id.add_new_area).setVisible(true);
                menu.findItem(R.id.add_new_item).setVisible(false);
                break;
            case 1:
                menu.findItem(R.id.add_new_area).setVisible(false);
                menu.findItem(R.id.add_new_item).setVisible(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

}

