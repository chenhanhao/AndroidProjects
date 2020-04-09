package com.jucceed.minitool.currencyConversion;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jucceed.minitool.R;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Country> countryList;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }



    public CountryAdapter(List<Country> countryList) {
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_country_layout,null,false);
        return new CountryAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        Country country = countryList.get(position);
        holder.imageView.setImageResource(country.getIconId());
  //      holder.imageView.setBackgroundResource(country.getFlag());
        holder.textView.setText(country.getUnitId());
        if(listener != null){
            holder.itemView.setOnClickListener(v -> listener.onClick(holder.itemView,position));
        }
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_country);
            textView = itemView.findViewById(R.id.tv_country_name);
        }
    }


    public interface OnItemClickListener{
        void onClick(View v, int position);
    }

}
