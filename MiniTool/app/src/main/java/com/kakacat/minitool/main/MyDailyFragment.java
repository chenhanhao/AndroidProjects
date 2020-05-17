package com.kakacat.minitool.main;

import android.content.Intent;
import android.view.View;

import com.kakacat.minitool.R;
import com.kakacat.minitool.appInfo.AppInfoActivity;
import com.kakacat.minitool.bingPic.BingPicActivity;
import com.kakacat.minitool.cleanFile.CleanFileActivity;
import com.kakacat.minitool.currencyConversion.CurrencyConversionActivity;
import com.kakacat.minitool.garbageClassification.GarbageClassificationActivity;
import com.kakacat.minitool.globalOutbreak.GlobalOutbreakActivity;
import com.kakacat.minitool.phoneArtribution.PhoneAttributionActivity;
import com.kakacat.minitool.todayInHistory.TodayInHistoryActivity;
import com.kakacat.minitool.translation.TranslationActivity;
import com.kakacat.minitool.util.RecycleViewItemOnClickListener;
import com.kakacat.minitool.wifipasswordview.WifiPwdViewActivity;

import java.util.List;

public class MyDailyFragment extends MyFragment implements RecycleViewItemOnClickListener {

    MyDailyFragment(List<MainItem> itemList) {
        super(itemList);
        super.myAdapter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v, int position) {
        Intent intent = null;
        switch (position){
            case 0 :{
                intent = new Intent(getContext(), CurrencyConversionActivity.class);
                break;
            }
            case 1:{
                intent = new Intent(getContext(), PhoneAttributionActivity.class);
                break;
            }
            case 2:{
                intent = new Intent(getContext(), TodayInHistoryActivity.class);
                break;
            }
            case 3:{
                intent = new Intent(getContext(), WifiPwdViewActivity.class);
                break;
            }
            case 4:{
                intent = new Intent(getContext(), AppInfoActivity.class);
                break;
            }
            case 5:{
                intent = new Intent(getContext(), CleanFileActivity.class);
                break;
            }
            case 6:{
                intent = new Intent(getContext(), GarbageClassificationActivity.class);
                break;
            }
            case 7:{
                intent = new Intent(getContext(), GlobalOutbreakActivity.class);
                break;
            }
            case 8:{
                intent = new Intent(getContext(), TranslationActivity.class);
                break;
            }
            case 9:{
                intent = new Intent(getContext(), BingPicActivity.class);
                break;
            }
        }

        if(intent != null) {
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.activity_open,R.anim.activity_enter_anim);
        }
    }
}
