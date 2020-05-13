package com.kakacat.minitool.appInfo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<PackageInfo> packageInfoList;
    private PackageManager pm;
    private RecycleViewItemOnClickListener clickListener;


    public void setOnClickListener(RecycleViewItemOnClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    public ItemAdapter(List<PackageInfo> packageInfoList, PackageManager pm) {
        this.packageInfoList = packageInfoList;
        this.pm = pm;
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
        PackageInfo packageInfo = packageInfoList.get(position);
        int n = packageInfo.applicationInfo.targetSdkVersion;
        holder.ivAppIcon.setBackground(packageInfo.applicationInfo.loadIcon(pm));
        holder.tvAppName.setText(packageInfo.applicationInfo.loadLabel(pm));
        holder.tvPackageName.setText(packageInfo.packageName);
        holder.tvAppVersionCode.setText(packageInfo.versionName);
        holder.tvAppAndroidVersion.setText(AppInfoActivity.androidApiMap[n]);
        holder.tvAppApiLevel.setText(n + "");
        if(clickListener != null){
            holder.itemView.setOnClickListener(v-> clickListener.onClick(holder.itemView,position));
        }
    }

    @Override
    public int getItemCount() {
        return packageInfoList.size();
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
