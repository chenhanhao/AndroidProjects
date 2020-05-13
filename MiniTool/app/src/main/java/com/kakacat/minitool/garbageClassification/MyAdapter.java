package com.kakacat.minitool.garbageClassification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnClickListener;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Garbage> garbageList;
    private RecycleViewItemOnClickListener listener;

    public void setOnClickListener(RecycleViewItemOnClickListener listener){
        this.listener = listener;
    }

    public MyAdapter(List<Garbage> garbageList) {
        this.garbageList = garbageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.garbage_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Garbage garbage = garbageList.get(position);
        int type = garbage.getType();
        holder.tvName.setText(garbage.getName());
        holder.tvType.setText(TypeMap.getTypeName(type));
        holder.ivIcon.setBackgroundResource(TypeMap.getIcon(type));
        if(listener != null){
            holder.itemView.setOnClickListener((v-> listener.onClick(holder.itemView,position)));
        }
    }

    @Override
    public int getItemCount() {
        return garbageList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivIcon;
        private TextView tvName;
        private TextView tvType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvType = itemView.findViewById(R.id.tv_type);
        }
    }

}
