package com.kakacat.minitool.cleanFile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;

import java.util.List;

public class MyFragment extends Fragment {

    private Context context;
    private ItemAdapter itemAdapter;
    private List<FileItem> fileItemList;

    public MyFragment(Context context,ItemAdapter itemAdapter,List<FileItem> fileItemList) {
        this.context = context;
        this.itemAdapter = itemAdapter;
        this.fileItemList = fileItemList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout,container,false);
        RecyclerView rv = view.findViewById(R.id.rv_file);
        itemAdapter.setOnClickListener((v, position) -> {
            FileItem fileItem = fileItemList.get(position);
            CheckBox checkBox = v.findViewById(R.id.cb_selected);
            if(fileItem.getChecked()){
                checkBox.setChecked(false);
                fileItem.setChecked(false);
            }else{
                checkBox.setChecked(true);
                fileItem.setChecked(true);
            }
        });

        rv.setAdapter(itemAdapter);
        rv.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }
}
