package com.kakacat.minitool.textEncryption;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private List<String> stringList;
    private LayoutInflater inflater;
    private View.OnFocusChangeListener onFocusChangeListener;


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
        holder.itemView.setClickable(true);
        if(onFocusChangeListener != null){
            Log.d("hhh","onFocusChangeListener");
            holder.itemView.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }



    public void setFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener){
        this.onFocusChangeListener = onFocusChangeListener;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvEncryptionMethod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEncryptionMethod = itemView.findViewById(R.id.tv_encryption_method);
        }
    }

}
