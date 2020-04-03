package com.jucceed.minitool.appinfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jucceed.minitool.R;

import java.util.List;

public class ItemAdapter2 extends RecyclerView.Adapter<ItemAdapter2.ViewHolder> {

    private List<AppDetail> appDetailList;

    public ItemAdapter2(List<AppDetail> appDetailList) {
        this.appDetailList = appDetailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_app_detail_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppDetail appDetail = appDetailList.get(position);
        holder.ivAppIcon.setBackground(appDetail.getAppIcon());
        holder.tvAppName.setText(appDetail.getAppName());
        holder.tvPackageName.setText(appDetail.getPackageName());
        holder.tvAppVersionCode.setText(appDetail.getAppVersionCode());
        holder.tvAppAndroidVersion.setText(appDetail.getAppAndroidVersion());
        holder.tvAppApiLevel.setText(appDetail.getAppApiLevel());
    }

    @Override
    public int getItemCount() {
        return appDetailList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivAppIcon;
        private TextView tvAppName;
        private TextView tvPackageName;
        private TextView tvAppVersionCode;
        private TextView tvAppAndroidVersion;
        private TextView tvAppApiLevel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAppIcon = itemView.findViewById(R.id.iv_app_icon);
            tvAppName = itemView.findViewById(R.id.tv_app_name);
            tvPackageName = itemView.findViewById(R.id.tv_app_package_name);
            tvAppVersionCode = itemView.findViewById(R.id.tv_app_version_code);
            tvAppAndroidVersion = itemView.findViewById(R.id.tv_app_android_version);
            tvAppApiLevel = itemView.findViewById(R.id.tv_app_api_level);
        }
    }

}
