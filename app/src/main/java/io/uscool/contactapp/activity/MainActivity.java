package io.uscool.contactapp.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import io.uscool.contactapp.R;
import io.uscool.contactapp.fragment.ContactFragment;
import io.uscool.contactapp.fragment.MessageFragment;

/**
 * @author Ujjawal Annad
 *
 * Launcher Activity, uses view pager to create tabbed view
 */

public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initViewPagerAndTabs();
    }

    /**
     * creates and initialise the toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

    }

    /**
     * initialise the viewpager and populates with the given fragments.
     */
    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        // shows list of contacts, populated from json file
        pagerAdapter.addFragment(ContactFragment.newInstance(), "Contact");
        // shows list of sent messages, populates from sqlite database
        pagerAdapter.addFragment(MessageFragment.newInstance(), "Message");

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            // To-do create a setting activity and call it
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * class used to populate fragments in viewpager extends FragmentPagerAdapter
     */
    private static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        private PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        /**
         * Call to add new fragment in the view pager
         * @param fragment the fragment you want to add
         * @param title title of fragment
         */
        private void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
