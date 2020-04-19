package com.kakacat.minitool.globalOutbreak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyPageAdapter extends FragmentPagerAdapter {

    private List<String> titleList;
    private List<MyFragment> myFragmentList;

    public MyPageAdapter(@NonNull FragmentManager fm, List<String> titleList,List<MyFragment> myFragmentList) {
        super(fm);
        this.titleList = titleList;
        this.myFragmentList = myFragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return myFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return myFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
