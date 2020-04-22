package com.kakacat.minitool.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.ui.ItemDecoration;

import java.util.List;

public class MyFragment extends Fragment{

    private RecyclerView recyclerView;
    MyAdapter myAdapter;

    public MyFragment(List<MainItem> itemList) {
        myAdapter = new MyAdapter(itemList);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.main_fragment_layout,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new ItemDecoration(30,30));

        return view;
    }
}
