package com.kakacat.minitool.globalOutbreak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnClickListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<EpidemicData> epidemicDataList;
    private RecycleViewItemOnClickListener listener;

    public void setOnClickListener(RecycleViewItemOnClickListener listener){
        this.listener = listener;
    }

    public MyAdapter(List<EpidemicData> epidemicDataList) {
        this.epidemicDataList = epidemicDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EpidemicData epidemicData = epidemicDataList.get(position);
        int currentCount = epidemicData.getCurrentConfirmedCount();

        if(currentCount == 0) holder.itemView.setBackgroundResource(R.color.white);
        else if(currentCount <= 9) holder.itemView.setBackgroundResource(R.color.light);
        else if(currentCount <= 49) holder.itemView.setBackgroundResource(R.color.light2);
        else if(currentCount <= 99) holder.itemView.setBackgroundResource(R.color.light3);
        else if(currentCount <= 999) holder.itemView.setBackgroundResource(R.color.light4);
        else if(currentCount <= 9999) holder.itemView.setBackgroundResource(R.color.light5);
        else if(currentCount <= 99999) holder.itemView.setBackgroundResource(R.color.light6);
        else holder.itemView.setBackgroundResource(R.color.light7);
        holder.tvName.setText(epidemicData.getProvinceName());
        holder.tvShortCode.setText(epidemicData.getCountryShortCode());
        if(listener != null){
            holder.itemView.setOnClickListener((v)-> listener.onClick(holder.itemView,position));
        }
    }

    @Override
    public int getItemCount() {
        return epidemicDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private TextView tvShortCode;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_country);
            tvShortCode = itemView.findViewById(R.id.tv_short_code);
        }
    }

}
