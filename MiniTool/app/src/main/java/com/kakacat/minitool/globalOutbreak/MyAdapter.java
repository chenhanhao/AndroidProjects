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
        holder.tvName.setText(epidemicData.getProvinceName());
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
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_country);
        }
    }

}
