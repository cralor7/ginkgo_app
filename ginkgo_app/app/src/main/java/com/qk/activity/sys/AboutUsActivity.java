package com.qk.activity.sys;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.qk.BaseActivity;
import com.qk.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/20
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private QMUIRoundButton btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
    btn = findViewById(R.id.gywm);
    btn.setOnClickListener(this);
    }
    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gywm:
                btn.setTextColor(Color.parseColor("#fe6400"));
                break;
                default:
                    break;
        }
    }
}
