package com.kakacat.minitool.bingPic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnClickListener;
import com.kakacat.minitool.util.RecycleViewItemOnLongClickListener;
import com.kakacat.minitool.util.RecycleViewOnTouchListener;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    private RecycleViewItemOnClickListener onClickListener;
    private RecycleViewItemOnLongClickListener onLongClickListener;
    private RecycleViewOnTouchListener onTouchListener;

    ImageAdapter(List<String> list) {
        this.list = list;
    }


    void setOnClickListener(RecycleViewItemOnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    void setOnLongClickListener(RecycleViewItemOnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    void setOnTouchListener(RecycleViewOnTouchListener onTouchListener){
        this.onTouchListener = onTouchListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(context == null) context = parent.getContext();
        if(inflater == null) inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bing_pic_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(list.get(position))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView);
        if(onClickListener != null)
            holder.imageView.setOnClickListener(v -> onClickListener.onClick(holder.itemView,position));
        if(onLongClickListener != null){
            holder.imageView.setOnLongClickListener(v -> {
                onLongClickListener.onLongClick(holder.imageView,position);
                return false;
            });
        }
        if(onTouchListener != null){
            holder.imageView.setOnTouchListener((v, event) -> {
                onTouchListener.onTouch(v,event);
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
