package com.qk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qk.R;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 *
 */
public class FourFragment_backup extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_retrieval,null);
        return view;
    }


}
