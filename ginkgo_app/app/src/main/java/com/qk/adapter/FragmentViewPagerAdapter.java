package com.qk.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * FragmentViewPager Adapter
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
