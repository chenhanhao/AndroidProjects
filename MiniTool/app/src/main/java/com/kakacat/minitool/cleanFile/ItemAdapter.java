package com.kakacat.minitool.cleanFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.RecycleViewItemOnClickListener;
import com.kakacat.minitool.util.StringUtil;
import com.kakacat.minitool.util.ui.CircleProgressView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter <ItemAdapter.ViewHolder>{

    private List<FileItem> fileList;
    private LayoutInflater inflater;
    private RecycleViewItemOnClickListener onClickListener;

    public ItemAdapter(List<FileItem> fileList) {
        this.fileList = fileList;
    }


    public void setOnClickListener(RecycleViewItemOnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(inflater == null) inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.file_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem fileItem = fileList.get(position);
        String result = StringUtil.byteToMegabyte(fileItem.getFile().length());

        holder.cvFileSize.setText(result);
        holder.tvFileName.setText(fileItem.getFile().getName());
        holder.tvFilePath.setText(fileItem.getFile().getAbsolutePath());
        holder.checkBox.setChecked(fileItem.getChecked());
        holder.btFileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        /*
                        * 这次懒得写..下次再写这里...
                        *
                        * */
            }
        });

        if(onClickListener != null){
            holder.itemView.setOnClickListener(v -> onClickListener.onClick(holder.itemView,position));
        }
    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleProgressView cvFileSize;
        private TextView tvFileName;
        private TextView tvFilePath;
        private CheckBox checkBox;
        private Button btFileDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvFileSize = itemView.findViewById(R.id.circle_progress_view);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
            tvFilePath = itemView.findViewById(R.id.tv_file_path);
            checkBox = itemView.findViewById(R.id.cb_selected);
            btFileDetail = itemView.findViewById(R.id.bt_file_detail);
        }
    }
}
