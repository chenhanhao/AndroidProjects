package com.jucceed.minitool.textEncryption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jucceed.minitool.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String> stringList;
    private LayoutInflater inflater;
    public ItemAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(inflater == null) inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        holder.tvEncryptionMethod.setText(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvEncryptionMethod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEncryptionMethod = itemView.findViewById(R.id.tv_encryption_method);
        }
    }
}
