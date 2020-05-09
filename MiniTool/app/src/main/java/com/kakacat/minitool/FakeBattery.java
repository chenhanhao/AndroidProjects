package com.kakacat.minitool;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.UiUtil;

public class FakeBattery {

    private static FakeBattery fakeBattery;
    private Activity activity;
    private View parentView;
    private PopupWindow popupWindow;
    private EditText etBattery;
    private SeekBar seekBarBattery;

    public static FakeBattery getInstance(Activity activity,View parentView){
        if(fakeBattery == null){
            fakeBattery = new FakeBattery();
            fakeBattery.activity = activity;
            fakeBattery.parentView = parentView;
            fakeBattery.initView();
        }
        return fakeBattery;
    }


    private void initView(){
        View popupWindowView = View.inflate(activity.getApplicationContext(), R.layout.popupwindow_fake_battery, null);
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        etBattery = popupWindowView.findViewById(R.id.et_current_battery);
        Button btFakeBattery = popupWindowView.findViewById(R.id.bt_fake_battery);
        Button btResetBattery = popupWindowView.findViewById(R.id.bt_reset_battery);
        seekBarBattery = popupWindowView.findViewById(R.id.seek_bar_battery);
        UiUtil.initPopupWindow(activity,popupWindow);
        seekBarBattery.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                etBattery.setText(progress + "");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        btResetBattery.setOnClickListener(v -> {
            SystemUtil.resetBattery();
            int val = SystemUtil.getElectricity(activity);
            seekBarBattery.setProgress(val);
        });
        btFakeBattery.setOnClickListener(v -> {
            int val = Integer.parseInt(etBattery.getText().toString());
            if(val < 0 || val > 100) Snackbar.make(parentView,"你正常点好吗???",Snackbar.LENGTH_SHORT).show();
            else {
                SystemUtil.setBatteryLevel(val + "");
                seekBarBattery.setProgress(val);
            }
        });
    }

    public void start(){
        int batteryLevel = SystemUtil.getElectricity(activity);
        etBattery.setText(batteryLevel + "");
        seekBarBattery.setProgress(batteryLevel);
        popupWindow.showAtLocation(parentView, Gravity.CENTER,0,0);
    }

}
