package com.qk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.qk.BaseActivity;
import com.qk.GApp;
import com.qk.R;
import com.qk.activity.sys.SetActivity;
import com.qk.activity.sys.UserActivity;
import com.qk.girdview.MyBaseExpandableListWithRecycleAdapter;
import com.qk.module.SetListViewItem;
import com.qk.util.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 主页面Activity
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Context ctx;
    public static MainActivity instance = null;
    private String username;
    private TextView tevUsername;
    private TextView tevCompany;
    private TextView tevOffice;
    private ExpandableListView expandableListView;
    private ImageView imvSet;
    private ImageView imvUserInformation;
    private List<SetListViewItem> list = new ArrayList<>();
    /**
     * 定义父列表项List数据集合
     */
    List<Map<String, Object>> parentMapList = new ArrayList<>();
    /**
     * 定义子列表项List数据集合
     */
    List<List<SetListViewItem>> childMapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ctx = this;
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(intiLayout());
        initView();
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        tevUsername = findViewById(R.id.usernmae_text);
        tevCompany = findViewById(R.id.company_text);
        tevOffice = findViewById(R.id.office_text);
        imvSet = findViewById(R.id.main_set_img);
        imvUserInformation = findViewById(R.id.user_message_right);
        imvSet.setOnClickListener(this);
        imvUserInformation.setOnClickListener(this);
        expandableListView = findViewById(R.id.expandableListView);

        username = DataUtils.getLocalData(ctx, "username", "你好");
        String company = "归属公司:"+DataUtils.getLocalData(ctx, "company", "济南轻卡销售部");
        String office = "归属部门:"+DataUtils.getLocalData(ctx, "office", "济南轻卡");
        tevUsername.setText(username);
        tevCompany.setText(company);
        tevOffice.setText(office);

        MyBaseExpandableListWithRecycleAdapter myBaseExpandableListWithGridViewAdapter
                =new MyBaseExpandableListWithRecycleAdapter(this, parentMapList, childMapList);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(myBaseExpandableListWithGridViewAdapter);
        /* 控制是否点击一个一级菜单的时候其他一级菜单都关闭*/
        setListViewKG();

    }

    @Override
    public void initData() {
        for (int i = 0; i <  GApp.parentMenu.length; i++) {
            list.clear();
            //提供父列表的数据
            Map<String, Object> parentMap = new HashMap<>();
            parentMap.put("parentName",  GApp.parentMenu[i]);
            parentMap.put("parentIcon",  GApp.parentImage[i]);
            parentMapList.add(parentMap);
            //提供当前父列的子列数据
            List<SetListViewItem> list = new ArrayList<>();
            for (int j = 0; j < GApp.childMenu[i].length; j++) {
                SetListViewItem item = new SetListViewItem();
                item.setImg(GApp.childImage[i][j]);
                item.setText( GApp.childMenu[i][j]);
                list.add(item);
            }
            childMapList.add(list);
        }
    }
    /**
     * 每次展开一个分组后，关闭其他的分组
     * @param expandedPosition expandedPosition
     * @return boolean
     */
    private boolean expandOnlyOne(int expandedPosition) {
        boolean result = true;
        int groupLength = expandableListView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupLength; i++) {
            if (i != expandedPosition && expandableListView.isGroupExpanded(i)) {
                result &= expandableListView.collapseGroup(i);
            }
        }
        return result;
    }

    private void setListViewKG(){
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {


            @Override
            public void onGroupExpand(int groupPosition) {
                // 把除了自己外的，其它全部关闭
                for(int i = 0; i < parentMapList.size(); i++){
                    if(i != groupPosition){
                        expandableListView.collapseGroup(i);
                    }
                }
            }

        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_set_img:
                Intent intent = new Intent(ctx, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.user_message_right:
                Intent intent2 = new Intent(ctx, UserActivity.class);
                startActivity(intent2);
                break;
            default:break;
        }
    }
}
