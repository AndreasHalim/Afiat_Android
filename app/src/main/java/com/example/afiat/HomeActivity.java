package com.example.afiat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.afiat.screen.achievement.AchievementBus;
import com.example.afiat.screen.achievement.AchievementFragment;
import com.example.afiat.screen.achievement.AchievementPresenter;
import com.example.afiat.screen.main.MainBus;
import com.example.afiat.screen.main.MainFragment;
import com.example.afiat.screen.main.MainPresenter;
import com.example.afiat.screen.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends FragmentActivity {
    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabs;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            }
        };

        PagerAdapter pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabs.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private class CustomPagerAdapter extends FragmentStatePagerAdapter {
        private static final int TABS_COUNT = 3;

        CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MainBus mainBus = new MainBus();
                    MainFragment mainFragment = MainFragment.newInstance(mainBus);
                    MainPresenter mainPresenter = new MainPresenter(mainFragment, getApplicationContext());
                    mainBus.setSubscriber(mainPresenter);
                    return mainFragment;
                case 1:
                    AchievementBus achievementBus = new AchievementBus();
                    AchievementFragment achievementFragment = AchievementFragment.newInstance(achievementBus);
                    AchievementPresenter achievementPresenter = new AchievementPresenter(achievementFragment, getApplicationContext());
                    achievementBus.setPresenter(achievementPresenter);
                    return achievementFragment;
                case 2:
                    return ProfileFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Main";
                case 1: return "History";
                case 2: return "Profile";
            }
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

//    private void setClickListener() {
//        btnSensor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(HomeActivity.this, SampleActivity.class));
//                finish();
//            }
//        });
//
//        btnProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
//                finish();
//            }
//        });
//
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//            }
//        });
//    }
}
