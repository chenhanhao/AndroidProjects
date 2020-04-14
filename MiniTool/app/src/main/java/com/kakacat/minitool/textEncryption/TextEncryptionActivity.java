package com.kakacat.minitool.textEncryption;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.R;
import com.kakacat.minitool.util.EncryptionUtil;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;

import java.util.ArrayList;
import java.util.List;

public class TextEncryptionActivity extends AppCompatActivity implements View.OnClickListener{

    private Context context;

    private Toolbar toolbar;

    private ItemAdapter adapter;

    private RecyclerView recyclerView;

    private Button btCode;
    private Button btDecode;
    private Button btDeleteInput;
    private Button btCopy;

    private EditText editText;

    private TextView tvSubTitle;
    private TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_encryption);

        initWidget();
        setListener();
    }


    private void initWidget() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        context = TextEncryptionActivity.this;

        initAdapter();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        btCode = findViewById(R.id.bt_code);
        btDecode = findViewById(R.id.bt_decode);
        btDeleteInput = findViewById(R.id.bt_delete_input);
        btCopy = findViewById(R.id.bt_copy);

        editText = findViewById(R.id.edit_text1);

        tvOutput = findViewById(R.id.tv_output);
        tvSubTitle = findViewById(R.id.tv_sub_title);
    }


    private void setListener(){
        btCode.setOnClickListener(this);
        btDecode.setOnClickListener(this);
        btDeleteInput.setOnClickListener(this);
        btCopy.setOnClickListener(this);

        adapter.setFocusChangeListener((v, hasFocus) -> {
            TextView tv = (TextView) v;
            if(hasFocus){
                tv.setTextSize(16);
                tvSubTitle.setText(tv.getText());
            }
            else tv.setTextSize(12);
        });
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) UiUtil.closeKeyboard(context,v);
        });
    }

    private void initAdapter(){
        List<String> stringList = new ArrayList<>();
        stringList.add("BASE64");
        stringList.add("MD5");
        stringList.add("HmacSHA1");
        adapter = new ItemAdapter(stringList);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_code:{
                String code = editText.getText().toString();
                if(!TextUtils.isEmpty(code)){
                    String decode = getCode(code);
                    if(!TextUtils.isEmpty(decode)){
                        tvOutput.setText(decode);
                        break;
                    }
                }
                Toast.makeText(context,"加密错误",Toast.LENGTH_SHORT).show();
                tvOutput.setText("");
                break;
            }
            case R.id.bt_decode:{

                break;
            }

            case R.id.bt_delete_input:{
                editText.setText("");
                break;
            }
            case R.id.bt_copy:{
                SystemUtil.copyToClipboard(context,"codeContent",tvOutput.getText());
                Snackbar.make(tvOutput,"复制成功",Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
    }


    private String getCode(String content){
        String result = "";
        CharSequence encryptionMethod = tvSubTitle.getText();

        if(encryptionMethod.equals("MD5"))
            result = EncryptionUtil.encryptionMD5(content.getBytes(),false);
        else if(encryptionMethod.equals("BASE64"))
            result = EncryptionUtil.encryptBASE64(content.getBytes());
        else if(encryptionMethod.equals("HmacSHA1"))
            result = EncryptionUtil.encryptHmacSHA1(content.getBytes());

        return result;
    }

}
