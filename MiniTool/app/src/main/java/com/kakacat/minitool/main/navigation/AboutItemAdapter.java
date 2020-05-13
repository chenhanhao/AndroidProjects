package com.kakacat.minitool.main.navigation;

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

public class AboutItemAdapter extends RecyclerView.Adapter<AboutItemAdapter.ViewHolder> {

    private List<AboutItem> list;
    private RecycleViewItemOnClickListener clickListener;
    private LayoutInflater layoutInflater;

    public AboutItemAdapter(List<AboutItem> list) {
        this.list = list;
    }

    public void setOnClickListener(RecycleViewItemOnClickListener listener){
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.about_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AboutItem aboutItem = list.get(position);
        holder.imageView.setImageResource(aboutItem.getIconId());
        holder.textView.setText(aboutItem.getTitle());
        if(clickListener != null){
            holder.itemView.setOnClickListener(v -> clickListener.onClick(holder.itemView,position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_icon);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }

}
