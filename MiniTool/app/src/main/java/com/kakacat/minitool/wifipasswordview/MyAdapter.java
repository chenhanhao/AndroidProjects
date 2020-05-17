package com.kakacat.minitool.wifipasswordview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnLongClickListener;
import com.kakacat.minitool.util.ui.CircleProgressView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Wifi> wifiList;
    private RecycleViewItemOnLongClickListener onLongClickListener;

    public MyAdapter(List<Wifi> wifiList) {
        this.wifiList = wifiList;
    }


    public void setOnLongClickListener(RecycleViewItemOnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(inflater == null) inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.wifi_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wifi wifi = wifiList.get(position);
        holder.circleProgressView.setText(wifi.getWifiImage());
        holder.tvWifiName.setText(wifi.getWifiName());
        holder.tvWifiPwd.setText(wifi.getWifiPwd());
        if(onLongClickListener != null){
            holder.itemView.setOnLongClickListener(v -> {
                onLongClickListener.onLongClick(v,position);
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleProgressView circleProgressView;
        private TextView tvWifiName;
        private TextView tvWifiPwd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleProgressView = itemView.findViewById(R.id.circle_progress_view);
            tvWifiName = itemView.findViewById(R.id.tv_wifi_name);
            tvWifiPwd = itemView.findViewById(R.id.tv_wifi_pwd);
        }
    }

}
