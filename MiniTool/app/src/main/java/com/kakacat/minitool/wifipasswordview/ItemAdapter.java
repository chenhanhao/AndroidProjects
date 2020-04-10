package com.kakacat.minitool.wifipasswordview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pavlospt.CircleView;
import com.kakacat.minitool.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Wifi> wifiList;

    public ItemAdapter(List<Wifi> wifiList) {
        this.wifiList = wifiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.wifi_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wifi wifi = wifiList.get(position);
        holder.circleView.setTitleText(wifi.getWifiImage());
        holder.tvWifiName.setText(wifi.getWifiName());
        holder.circleView.setSubtitleText("");
        holder.tvWifiPwd.setText(wifi.getWifiPwd());
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleView circleView;
        private TextView tvWifiName;
        private TextView tvWifiPwd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleView = itemView.findViewById(R.id.circle_view);
            tvWifiName = itemView.findViewById(R.id.tv_wifi_name);
            tvWifiPwd = itemView.findViewById(R.id.tv_wifi_pwd);
        }
    }

}
