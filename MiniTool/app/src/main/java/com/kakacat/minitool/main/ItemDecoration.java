package com.kakacat.minitool.main;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private int horizontal;
    private int vertical;

    public ItemDecoration(int horizontal, int vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {

            outRect.top = vertical;
            outRect.left = horizontal;
            outRect.right = horizontal;
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                outRect.right = horizontal;
            }
            outRect.top = vertical;
            outRect.left = horizontal;
            outRect.bottom = vertical;
        }
    }
}

