package com.qk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qk.R;
import com.qk.module.QDItemDescription;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * QMUITabSegment
 */
public class QMUITabSegment1 extends Fragment {

    @SuppressWarnings("FieldCanBeLocal") private final int TAB_COUNT = 10;

    private View view;
    private QMUITabSegment mTabSegment;
    private ViewPager mContentViewPager;
    private Context ctx;
    private Map<ContentPage, View> mPageMap = new HashMap<>();
    private ContentPage mDestPage = ContentPage.Item1;
    private QDItemDescription mQDItemDescription;
    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return ContentPage.SIZE;
        }

        @NonNull
        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            ContentPage page = ContentPage.getPage(position);
            View view = getPageView(page);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, params);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_viewpager_layout, null);
        mTabSegment = view.findViewById(R.id.tabSegment);
        initTabAndPager();
        return view;
    }

    private void initTabAndPager() {
        mContentViewPager.setAdapter(mPagerAdapter);
        mContentViewPager.setCurrentItem(mDestPage.getPosition(), false);
        for (int i = 0; i < TAB_COUNT; i++) {
            mTabSegment.addTab(new QMUITabSegment.Tab("Item " + (i + 1)));
        }
        int space = QMUIDisplayHelper.dp2px(ctx, 16);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setupWithViewPager(mContentViewPager, false);
        mTabSegment.setPadding(space, 0, space, 0);
    }

    private View getPageView(ContentPage page) {
        View view = mPageMap.get(page);
        if (view == null) {
            TextView textView = new TextView(ctx);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setTextColor(ContextCompat.getColor(ctx, R.color.app_color_description));
            String text= "这是第 " + (page.getPosition() + 1) + " 个 Item 的内容区";
            textView.setText(text);
            view = textView;
            mPageMap.put(page, view);
        }
        return view;
    }

public enum ContentPage {
    Item1(0),
    Item2(1),
    Item3(2),
    Item4(3),
    Item5(4),
    Item6(5),
    Item7(6),
    Item8(7),
    Item9(8),
    Item10(9);
    public static final int SIZE = 10;
    private final int position;

    ContentPage(int pos) {
        position = pos;
    }

    public static ContentPage getPage(int position) {
        switch (position) {
            case 0:
                return Item1;
            case 1:
                return Item2;
            case 2:
                return Item3;
            case 3:
                return Item4;
            case 4:
                return Item5;
            case 5:
                return Item6;
            case 6:
                return Item7;
            case 7:
                return Item8;
            case 8:
                return Item9;
            case 9:
                return Item10;
            default:
                return Item1;
        }
    }
    public int getPosition() {
        return position;
    }
}
}