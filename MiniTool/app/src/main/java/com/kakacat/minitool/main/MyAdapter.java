package com.kakacat.minitool.main;

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

    private List<MainItem> list;
    private LayoutInflater layoutInflater;
    private RecycleViewItemOnClickListener listener;

    public void setOnClickListener(RecycleViewItemOnClickListener listener){
        this.listener = listener;
    }

    public MyAdapter(List<MainItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.main_item_layout,parent,false);
        return new MyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MainItem item = list.get(position);
        holder.imageView.setImageResource(item.getIconId());
        holder.title.setText(item.getTitleId());
        holder.note.setText(item.getNoteId());

        if(listener != null){
            holder.itemView.setOnClickListener(v -> {
                if(listener != null){
                    listener.onClick(holder.itemView,position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        TextView note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            note = itemView.findViewById(R.id.note);
        }
    }

    public interface OnItemClickListener{
        void onClick(View v, int position);
    }

}
