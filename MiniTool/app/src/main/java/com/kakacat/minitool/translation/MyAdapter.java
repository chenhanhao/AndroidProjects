package com.kakacat.minitool.translation;

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

    private List<String> languageList;
    private RecycleViewItemOnClickListener listener;

    public MyAdapter(List<String> languageList) {
        this.languageList = languageList;
    }

    public void setOnClickListener(RecycleViewItemOnClickListener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvLanguage.setText(languageList.get(position));
        if(listener != null){
            holder.itemView.setOnClickListener(v -> listener.onClick(holder.itemView,position));
        }
    }


    @Override
    public int getItemCount() {
        return languageList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvLanguage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLanguage = (TextView)itemView;
        }
    }

}
