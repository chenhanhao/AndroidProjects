package com.kakacat.minitool;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.material.snackbar.Snackbar;
import com.kakacat.minitool.util.SystemUtil;
import com.kakacat.minitool.util.ui.MyPopupWindow;

public class FakeBatteryView extends MyPopupWindow {

    private static FakeBatteryView fakeBatteryView;
    private Activity activity;
    private View parentView;
    private View contentView;
    private EditText etBattery;
    private SeekBar seekBarBattery;

    private FakeBatteryView(Activity activity, View contentView, int width, int height) {
        super(activity, contentView, width, height);
        this.activity = activity;
        this.contentView = contentView;
    }

    public static FakeBatteryView getInstance(Activity activity, View parentView,View contentView,int width,int height){
        if(fakeBatteryView == null){
            fakeBatteryView = new FakeBatteryView(activity, contentView, width, height);
            fakeBatteryView.parentView = parentView;
            fakeBatteryView.initView();
        }

        return fakeBatteryView;
    }


    private void initView(){
        etBattery = contentView.findViewById(R.id.et_current_battery);
        seekBarBattery = contentView.findViewById(R.id.seek_bar_battery);
        Button btFakeBattery = contentView.findViewById(R.id.bt_fake_battery);
        Button btResetBattery = contentView.findViewById(R.id.bt_reset_battery);
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
            if(val < 0 || val > 100)
                Snackbar.make(parentView,"你正常点好吗???",Snackbar.LENGTH_SHORT).show();
            else {
                SystemUtil.setBatteryLevel(val + "");
                seekBarBattery.setProgress(val);
            }
        });
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        int batteryLevel = SystemUtil.getElectricity(activity);
        etBattery.setText(batteryLevel + "");
        seekBarBattery.setProgress(batteryLevel);
        super.showAtLocation(parent, gravity, x, y);
    }
}
