package com.kakacat.minitool.cleanFile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.pavlospt.CircleView;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.ui.StringUtil;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter <ItemAdapter.ViewHolder>{

    private List<FileItem> fileList;
    private Context context;
    private LayoutInflater inflater;
    private OnClickListener onClickListener;

    public ItemAdapter(Context context,List<FileItem> fileList) {
        this.context = context;
        this.fileList = fileList;
    }


    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(inflater == null) inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.file_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileItem fileItem = fileList.get(position);
        String result = StringUtil.byteToMegabyte(fileItem.getFile().length());
        holder.cvFileSize.setTitleText(result.substring(0,result.length() - 1));
        holder.cvFileSize.setSubtitleText(String.valueOf(result.charAt(result.length() - 1)));
        holder.tvFileName.setText(fileItem.getFile().getName());
        holder.tvFilePath.setText(fileItem.getFile().getAbsolutePath());
        holder.checkBox.setChecked(fileItem.getChecked());

        if(onClickListener != null){
            holder.itemView.setOnClickListener(v -> onClickListener.onClick(holder.itemView,position));
        }
    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleView cvFileSize;
        private TextView tvFileName;
        private TextView tvFilePath;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvFileSize = itemView.findViewById(R.id.circle_view);
            tvFileName = itemView.findViewById(R.id.tv_file_name);
            tvFilePath = itemView.findViewById(R.id.tv_file_path);
            checkBox = itemView.findViewById(R.id.cb_selected);
        }
    }


    public interface OnClickListener{
        void onClick(View v,int position);
    }
}
