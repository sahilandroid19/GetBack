package com.example.sahil.getback.adapters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private String movieId;

    public DetailPagerAdapter(FragmentManager fm, String id) {
        super(fm);
        movieId = id;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch (position) {

            case 0:
                bundle.putString("id", movieId);
                mFragmentList.get(0).setArguments(bundle);
                return mFragmentList.get(0);

            case 1:
                bundle.putString("id", movieId);
                mFragmentList.get(1).setArguments(bundle);
                return mFragmentList.get(1);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
