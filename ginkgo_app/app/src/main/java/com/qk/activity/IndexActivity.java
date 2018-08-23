package com.qk.activity;

import android.content.Intent;
import android.os.Bundle;

import com.qk.BaseActivity;
import com.qk.R;
import com.qk.fragment.xsgl.OrderRetrievalFragment;
import com.qk.fragment.xsgl.OrderUpdateFragment;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * Fragment的父容器Activity类，当要从一个Activity跳转到一个Fragment时，
 *       先跳转到IndexActivity并携带Fragment的编号，再跳转到相应的Fragment
 */
public class IndexActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Intent intent = getIntent();
        //要跳转的fragment的number
        int fragmentNumber = intent.getIntExtra("fragmentNumber", 0);
        //跳转到订单检索
        if(fragmentNumber == 1) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrderRetrievalFragment())
                    .addToBackStack(null)
                    .commit();
        }
        //跳转到订单修改
        if(fragmentNumber == 2) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrderUpdateFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_index;
    }

    @Override
    public void initView() {}

    @Override
    public void initData() {}

}
