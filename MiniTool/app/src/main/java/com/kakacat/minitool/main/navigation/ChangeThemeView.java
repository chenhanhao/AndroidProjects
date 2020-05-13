package com.kakacat.minitool.main.navigation;

import android.app.Activity;
import android.view.View;
import android.widget.RadioGroup;

import com.kakacat.minitool.R;
import com.kakacat.minitool.util.ui.MyPopupWindow;

public class ChangeThemeView extends MyPopupWindow {

    private static ChangeThemeView themeView;
    private View contentView;

    private ChangeThemeView(Activity activity,View contentView, int width, int height) {
        super(activity, contentView,width, height);
        this.contentView = contentView;
    }


    public static ChangeThemeView getInstance(Activity activity,View contentView,int width, int height){
        if(themeView == null){
            themeView = new ChangeThemeView(activity, contentView,width, height);
            themeView.initView();
        }

        return themeView;
    }


    private void initView(){
        RadioGroup radioGroup = contentView.findViewById(R.id.radio_group_theme);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.theme_blue:{
                    break;
                }
                case R.id.theme_purple:{
                    break;
                }
                case R.id.theme_pink:{

                    break;
                }
                default:
                    break;
            }
        });
    }

}
