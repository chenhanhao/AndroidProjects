package com.jucceed.minitool.appinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pavlospt.CircleView;
import com.jucceed.minitool.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<Api> apiList;

    public ItemAdapter(List<Api> apiList) {
        this.apiList = apiList;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_api_percent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Api api = apiList.get(position);
        holder.logo.setImageResource(api.getLogoId());
        holder.tvAndroidVersionName.setText(api.getAndroidVersionName());
        holder.tvApiLevel.setText(api.getApiLevel());
        holder.tvAppNum.setText("" + api.getAppNum());
        holder.tvAppPercent.setText(api.getAppPercent());
    }


    @Override
    public int getItemCount() {
        return apiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView logo;
        private TextView tvAndroidVersionName;
        private TextView tvApiLevel;
        private TextView tvAppNum;
        private TextView tvAppPercent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.cv_android_logo);
            tvAndroidVersionName = itemView.findViewById(R.id.tv_android_version_name);
            tvApiLevel = itemView.findViewById(R.id.tv_api_level);
            tvAppNum = itemView.findViewById(R.id.tv_app_num);
            tvAppPercent = itemView.findViewById(R.id.tv_app_percent);
        }
    }
}
