package com.kakacat.minitool.currencyConversion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnClickListener;

import java.util.List;

public class KeyBoardAdapter extends RecyclerView.Adapter<KeyBoardAdapter.ViewHolder> {

    private List<Character> characterList;
    private RecycleViewItemOnClickListener listener;

    public void setOnClickListener(RecycleViewItemOnClickListener listener){
        this.listener = listener;
    }

    public KeyBoardAdapter(List<Character> characterList) {
        this.characterList = characterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.key_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.button.setText(characterList.get(position).toString());
        if(listener != null){
            holder.itemView.setOnClickListener((v)->listener.onClick(holder.itemView,position));
        }
    }

    @Override
    public int getItemCount() {
        return characterList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.bt_key);
        }
    }

}
