package com.kakacat.minitool.bingPic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.R;
import com.kakacat.minitool.main.MainActivity;
import com.kakacat.minitool.util.HttpCallbackListener;
import com.kakacat.minitool.util.HttpUtil;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

public class BingPicActivity extends AppCompatActivity {

    private Context context;
    private List<String> list;
    private ImageAdapter imageAdapter;
    private OptionPopupWindow myPopupWindow;
    private View popupView;
    private ImageView bigImageView;

    private int d = 0;
    private int width = 320;
    private int height = 240;
    private int loadNum = 10;
    private int currentX;
    private int currentY;

    private static final int DOWNLOAD_COMPLETE = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == DOWNLOAD_COMPLETE){
                UiUtil.showHint(popupView,"保存成功");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bing_pic);

        initView();
    }


    private void initView(){
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        fillList();
        imageAdapter = new ImageAdapter(list);

        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            View view = nestedScrollView.getChildAt(0);
            int bottom = view.getBottom();
            bottom -= nestedScrollView.getHeight() + nestedScrollView.getScrollY();
            if(bottom == 0){
                Log.d("hhh","加载更多");
                fillList();
                imageAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == SCROLL_STATE_IDLE && Glide.with(context).isPaused())
                    Glide.with(context).resumeRequests();
                else if(newState == SCROLL_STATE_DRAGGING){
                    Glide.with(context).pauseRequests();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        imageAdapter.setOnClickListener((v, position) -> {
            ImageView imageView = v.findViewById(R.id.image_view);
            if(popupView == null){
                popupView = LayoutInflater.from(BingPicActivity.this).inflate(R.layout.big_image_layout,null,false);
                bigImageView = popupView.findViewById(R.id.image_view);
            }
            if(myPopupWindow == null){
                myPopupWindow = new OptionPopupWindow(BingPicActivity.this,popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                myPopupWindow.setAlpha(0.0f);
            }
            Drawable drawable = imageView.getDrawable();
            if(drawable != null){
                bigImageView.setImageDrawable(drawable);
                myPopupWindow.showAtLocation(recyclerView, Gravity.CENTER,0,0);
            }
        });

        imageAdapter.setOnLongClickListener((v, position) -> {
            OptionPopupWindow optionPopupWindow = OptionPopupWindow.getInstance(context,R.layout.option_layout, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            requestPermissions();
            optionPopupWindow.showAtLocation(v,Gravity.NO_GRAVITY,currentX,currentY);
        });
        imageAdapter.setOnTouchListener((v, event) -> {
            currentX = (int) event.getRawX();
            currentY = (int) event.getRawY();
            return false;
        });

    }


    private String getNextAddress(){
        return "https://bing.ioliu.cn/v1/rand" +
                "?d=" + (d++) +
                "?w=" + width +
                "height" + height;
    }

    private void fillList(){
        for(int i = 0; i < loadNum; i++)
            list.add(getNextAddress());
    }


    private void requestPermissions(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(permissions,REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,"权限获取失败,后期会导致保存图片失败,其他功能不受影响",Toast.LENGTH_SHORT).show();
            }
        }
    }

    static class OptionPopupWindow extends com.kakacat.minitool.util.ui.MyPopupWindow{

        private static OptionPopupWindow optionPopupWindow;
        private Context context;
        private View contentView;
        private ImageView imageView;

        OptionPopupWindow(Context context, View contentView, int width, int height) {
            super(context, contentView, width, height);
        }

        static OptionPopupWindow getInstance(Context context,int resId, int width, int height){
            if(optionPopupWindow == null){
                View contentView = View.inflate(context,resId,null);
                optionPopupWindow = new OptionPopupWindow(context,contentView,width,height);
                optionPopupWindow.context = context;
                optionPopupWindow.contentView = contentView;
                optionPopupWindow.setAlpha(1.0f);
                optionPopupWindow.initView();
            }
            return optionPopupWindow;
        }

        public void showAtLocation(View parent, int gravity, int x, int y) {
            super.showAtLocation(parent, gravity, x, y);
            this.imageView = parent.findViewById(R.id.image_view);
        }

        private void initView(){
            TextView tvSaveImage = contentView.findViewById(R.id.tv_save_image);
            tvSaveImage.setOnClickListener(v -> saveImage());
        }

        private void saveImage(){
            Log.d("hhh","saveImage");
            try{
                String path = "/storage/emulated/0/Pictures/MiniTool/" + System.currentTimeMillis() + ".png";
                File file = new File(path);
                File parentFile = file.getParentFile();
                if(!parentFile.exists())
                    parentFile.mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(path);
                Bitmap bitmap = UiUtil.drawableToBitmap(imageView.getDrawable());
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

                Log.d("hhh",imageView.getDrawable().hashCode() + "");

                fos.flush();
                fos.close();

                dismiss();
                SystemUtil.vibrate(context,50);
                Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(context,"保存失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
