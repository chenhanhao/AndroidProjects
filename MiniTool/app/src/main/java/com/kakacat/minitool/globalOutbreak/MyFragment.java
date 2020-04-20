package com.kakacat.minitool.globalOutbreak;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.ui.UiUtil;

import java.util.List;

public class MyFragment extends Fragment {
    private List<EpidemicData> epidemicDataList;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private TextView tvCurrentConfirmedCount;
    private TextView tvConfirmedCount;
    private TextView tvSuspectedCount;
    private TextView tvDeadCount;
    private boolean isCreateView;

    public MyFragment(List<EpidemicData> epidemicDataList) {
        this.epidemicDataList = epidemicDataList;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout2,container,false);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        myAdapter = new MyAdapter(epidemicDataList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        tvCurrentConfirmedCount = rootView.findViewById(R.id.tv_current_confirmed_count);
        tvConfirmedCount = rootView.findViewById(R.id.tv_confirmed_count);
        tvSuspectedCount = rootView.findViewById(R.id.tv_suspected_count);
        tvDeadCount = rootView.findViewById(R.id.tv_dead_count);

        tvCurrentConfirmedCount.setText(getCurrentConfirmedCount());
        tvConfirmedCount.setText(getConfirmedCount());
        tvSuspectedCount.setText(getSuspectedCount());
        tvDeadCount.setText(getDeadCount());
        myAdapter.setOnClickListener((v, position) -> {
            EpidemicData data = epidemicDataList.get(position);
            View view = inflater.inflate(R.layout.click_layout,null,false);
            PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            UiUtil.initPopupWindow(getActivity(),popupWindow);
            TextView tv1 = view.findViewById(R.id.tv_country_current_confirmed_count);
            TextView tv2 = view.findViewById(R.id.tv_country_confirmed_count);
            TextView tv3 = view.findViewById(R.id.tv_country_cured_count);
            TextView tv4 = view.findViewById(R.id.tv_country_dead_count);
            TextView tv5 = view.findViewById(R.id.tv_country_dead_rate);
            TextView tv6 = view.findViewById(R.id.tv_country_dead_count_rank);
            tv1.setText(String.valueOf(data.getCurrentConfirmedCount()));
            tv2.setText(String.valueOf(data.getConfirmedCount()));
            tv3.setText(String.valueOf(data.getSuspectedCount()));
            tv4.setText(String.valueOf(data.getDeadCount()));
            tv5.setText(data.getDeadCountRate() + "%");
            tv6.setText(String.valueOf(data.getDeadCountRank()));

            popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        });

        isCreateView = true;
        return rootView;
    }

    public void refresh(){
        if(isCreateView){
            myAdapter.notifyDataSetChanged();
            tvCurrentConfirmedCount.setText(getCurrentConfirmedCount());
            tvConfirmedCount.setText(getConfirmedCount());
            tvSuspectedCount.setText(getSuspectedCount());
            tvDeadCount.setText(getDeadCount());
        }
    }


    private String getCurrentConfirmedCount(){
        int num = 0;
        for(EpidemicData data : epidemicDataList){
            num += data.getCurrentConfirmedCount();
        }
        return String.valueOf(num);
    }

    private String getConfirmedCount(){
        int num = 0;
        for(EpidemicData data : epidemicDataList){
            num += data.getConfirmedCount();
        }
        return String.valueOf(num);
    }

    private String getSuspectedCount(){
        int num = 0;
        for(EpidemicData data : epidemicDataList){
            num += data.getSuspectedCount();
        }
        return String.valueOf(num);
    }

    private String getDeadCount(){
        int num = 0;
        for(EpidemicData data : epidemicDataList){
            num += data.getDeadCount();
        }
        return String.valueOf(num);
    }
}
