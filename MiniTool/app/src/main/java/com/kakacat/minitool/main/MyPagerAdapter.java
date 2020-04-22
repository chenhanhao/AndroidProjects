package com.kakacat.minitool.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kakacat.minitool.R;

import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<MyTab> myTabList;
    private List<MyFragment> myFragmentList;

    public MyPagerAdapter(@NonNull FragmentManager fm, Context context,List<MyTab> myTabList, List<MyFragment> myFragmentList) {
        super(fm);
        this.context = context;
        this.myTabList = myTabList;
        this.myFragmentList = myFragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return myFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return myTabList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return myTabList.get(position).getTitle();
    }


    public View getTabView(int position){
        MyTab myTab = myTabList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout,null);
        TextView textView = view.findViewById(R.id.tv_tab);
        ImageView imageView = view.findViewById(R.id.iv_tab);

        textView.setText(myTab.getTitle());
        imageView.setImageResource(myTab.getIconId());

        return view;
    }
}
