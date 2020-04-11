package com.kakacat.minitool.appInfo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.EncryptionUtil;
import com.kakacat.minitool.util.SystemUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;

    private ActionBar actionBar;

    private TextView tvAppName;
    private TextView tvPackageName;
    private TextView tvVersionName;
    private TextView tvFirstInstallTime;
    private TextView tvLastUpdateTime;
    private TextView tvTargetApi;
    private TextView tvMinApi;
    private TextView tvMd5Signature;
    private TextView tvHeader3;
    private TextView tvPermission;

    private ImageView ivAppIcon;

    private Button btSaveIcon;
    private Button btOpenMarket;
    private Button btShareApk;
    private Button btOpenDetail;
    private Button btCopyMd5;

    private PackageManager pm;

    private PackageInfo packageInfo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        initWidget();
        setData();
        setListener();
    }

    private void initWidget() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        context = AppDetailActivity.this;

        tvAppName = findViewById(R.id.tv_app_name);
        tvPackageName = findViewById(R.id.tv_package_name);
        tvVersionName = findViewById(R.id.tv_version_name);
        tvFirstInstallTime = findViewById(R.id.tv_first_install_time);
        tvLastUpdateTime = findViewById(R.id.tv_last_update_time);
        tvTargetApi = findViewById(R.id.tv_target_api);
        tvMinApi = findViewById(R.id.tv_min_api);
        tvMd5Signature = findViewById(R.id.tv_md5_sign);
        tvPermission = findViewById(R.id.tv_permission);
        tvHeader3 = findViewById(R.id.tv_header3);

        ivAppIcon = findViewById(R.id.iv_app_icon);

        btSaveIcon = findViewById(R.id.bt_save_icon);
        btOpenMarket = findViewById(R.id.bt_open_market);
        btShareApk = findViewById(R.id.bt_share_apk);
        btOpenDetail = findViewById(R.id.bt_open_detail);
        btCopyMd5 = findViewById(R.id.bt_copy_md5);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setData() {
        pm = getPackageManager();
        packageInfo = getIntent().getParcelableExtra("packageInfo");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String firstInstallTime = sdf.format(packageInfo.firstInstallTime);
        String lastUpdateTime = sdf.format(packageInfo.lastUpdateTime);
        String[] permissions = packageInfo.requestedPermissions;

        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvAppName.setText(packageInfo.applicationInfo.loadLabel(pm));
        tvPackageName.setText(packageInfo.packageName);
        tvVersionName.setText(packageInfo.versionName);
        tvFirstInstallTime.setText(firstInstallTime);
        tvLastUpdateTime.setText(lastUpdateTime);
        tvTargetApi.setText(packageInfo.applicationInfo.targetSdkVersion + "");
        tvMinApi.setText(packageInfo.applicationInfo.minSdkVersion + "");
        tvMd5Signature.setText(EncryptionUtil.getSignMd5Str(packageInfo).toUpperCase());
        if(permissions != null){
            tvHeader3.setText("权限声明" + "(" + permissions.length + "个)");
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < permissions.length; i++) sb.append(i + 1 + ". " + permissions[i] + "\n");
            tvPermission.setText(sb.toString());
        }

        ivAppIcon.setBackground(packageInfo.applicationInfo.loadIcon(pm));
    }

    private void setListener() {
        btSaveIcon.setOnClickListener(this);
        btOpenMarket.setOnClickListener(this);
        btShareApk.setOnClickListener(this);
        btOpenDetail.setOnClickListener(this);
        btCopyMd5.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save_icon : {
                saveIconToLocal();
                break;
            }
            case R.id.bt_open_market : {
                openMarket();
                break;
            }
            case R.id.bt_share_apk : {
                shareApk();
                break;
            }
            case R.id.bt_open_detail : {
                openDetail();
                break;
            }
            case R.id.bt_copy_md5 : {
                copyMd5();
                break;
            }
        }
    }



    private void saveIconToLocal(){
        try{
            String path = getExternalCacheDir().getAbsolutePath();
            File file = new File(path,packageInfo.packageName + ".png");
            if(!file.exists()){
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
  //              Bitmap bitmap= ((BitmapDrawable)packageInfo.applicationInfo.loadIcon(pm)).getBitmap();
                Bitmap bitmap = drawableToBitmap(packageInfo.applicationInfo.loadIcon(pm));

                bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                fos.flush();
                fos.close();


                String s = "成功保存在目录 : " + path + packageInfo.packageName + ".png";
                Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"已经保存过该图片!",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openMarket() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + packageInfo.packageName));
        startActivity(intent);
    }

    private void shareApk() {

    }

    private void openDetail() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageInfo.packageName));
        startActivity(intent);
    }


    private void copyMd5() {
        CharSequence content = tvMd5Signature.getText().toString();
        SystemUtil.copyToClipboard(context,"md5",content);
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
            default:
                break;
        }
        return true;
    }


    /**
     * 将Drawable转换为Bitmap
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        //取drawable的宽高
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        //创建对应的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //创建对应的bitmap的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        //把drawable内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

}
