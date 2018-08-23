package com.qk.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.R;
import com.qk.adapter.QDSimpleAdapter;
import com.qk.module.QDItemDescription;
import com.qk.util.RefreshLayout;
import com.qmuiteam.qmui.alpha.QMUIAlphaButton;
import com.qmuiteam.qmui.util.QMUIDeviceHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIPackageHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.QMUIAnimationListView;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 上拉加载更多显示列表数据
 */
public class FiveFragment extends Fragment implements View.OnClickListener {
    private RefreshLayout refresh;
    private Context context;
    private ListView listView;
    private ListView listView3;
    private BaseAdapter adapter;
    private List<Object> list = new ArrayList<>();
    private View  view;
    private Boolean scrollFlag = false;
    private QMUIEmptyView mEmptyView;
    private QMUIRoundButton qmuiRoundButton,add,delete,tiaozhuantab;
    private QMUIAlphaButton albutton;
    private ListView mListView;
    private QMUIGroupListView mGroupListView;
    private TextView textView;
    private QMUITopBar mTopBar;
    private QMUITabSegment mTabSegment;
    private ViewPager mContentViewPager;
    private QMUIRoundButton charulistview;
    private QMUIAnimationListView qmlist;
    private List<String> mData = new ArrayList<>();
    private int img[] = {R.mipmap.xinge, R.mipmap.rege, R.mipmap.yaogunge, R.mipmap.oumeige, R.mipmap.xinge, R.mipmap.rege, R.mipmap.yaogunge, R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige};

    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private QDItemDescription mQDItemDescription;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_one,null,false);
        listView3 = view.findViewById(R.id.listview3);
        qmlist = view.findViewById(R.id.qmlist);
        mEmptyView = view.findViewById(R.id.emptyView);
        qmuiRoundButton =view.findViewById(R.id.zhidingdaxiao);
        tiaozhuantab =view.findViewById(R.id.tiaozhuantab);
        tiaozhuantab.setOnClickListener(this);
        albutton =view.findViewById(R.id.alphabutton);
        albutton.setOnClickListener(this);
        add =view.findViewById(R.id.add);
        add.setOnClickListener(this);
        delete =view.findViewById(R.id.delete);
        delete.setOnClickListener(this);
        charulistview =view.findViewById(R.id.charulistiview);
        charulistview.setOnClickListener(this);
        mListView=view.findViewById(R.id.listview);
        mGroupListView = view.findViewById(R.id.groupListView);
        textView = view.findViewById(R.id.verison);
        String str="APP版本号----"+QMUIPackageHelper.getAppVersion(getContext());
        textView.setText(str);
        QMUILoadingView loadingView = new QMUILoadingView(context);
        loadingView.setColor(Color.RED);
        loadingView.setSize(QMUIDisplayHelper.dp2px(context, 22));
        LinearLayout.LayoutParams loadingViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingView.setLayoutParams(loadingViewLP);
        initContent();
        initListView2();
        initListView3();
        mGroupListView.addView(loadingView);
      /*  final  QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("灯泡ID输入")
                .setPlaceholder("请在此输入灯泡ID")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                . addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {

                            Toast.makeText(getActivity(), "请填入合法灯泡ID", Toast.LENGTH_SHORT).show();

                    }
                }).show();


        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("标题")
                .setMessage("确定要删除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();*/

        qmuiRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new QMUIBottomSheet.BottomListSheetBuilder(getActivity())
                        .addItem(getResources().getString(R.string.emptyView_mode_title_double_text))
                        .addItem(getResources().getString(R.string.emptyView_mode_title_single_text))
                        .addItem(getResources().getString(R.string.emptyView_mode_title_loading))
                        .addItem(getResources().getString(R.string.emptyView_mode_title_single_text_and_button))
                        .addItem(getResources().getString(R.string.emptyView_mode_title_double_text_and_button))
                        .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                            @Override
                            public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                dialog.dismiss();
                                switch (position) {
                                    case 0:
                                        mEmptyView.show(getResources().getString(R.string.emptyView_mode_desc_double), getResources().getString(R.string.emptyView_mode_desc_detail_double));
                                        break;
                                    case 1:
                                        mEmptyView.show(getResources().getString(R.string.emptyView_mode_desc_single), null);
                                        break;
                                    case 2:
                                        mEmptyView.show(true);
                                        break;
                                    case 3:
                                        mEmptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), null, getResources().getString(R.string.emptyView_mode_desc_retry), null);
                                        break;
                                    case 4:
                                        mEmptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), null);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .build()
                        .show();
            }
        });

        initListView();

        return view;
    }

    private void showSimpleBottomSheetGrid() {
        final int TAG_SHARE_WECHAT_FRIEND = 0;
        final int TAG_SHARE_WECHAT_MOMENT = 1;
        final int TAG_SHARE_WEIBO = 2;
        final int TAG_SHARE_CHAT = 3;
        final int TAG_SHARE_LOCAL = 4;
        QMUIBottomSheet.BottomGridSheetBuilder builder = new QMUIBottomSheet.BottomGridSheetBuilder(getActivity());
        builder.addItem(R.mipmap.icon_more_operation_share_friend, "分享到微信", TAG_SHARE_WECHAT_FRIEND, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_moment, "分享到朋友圈", TAG_SHARE_WECHAT_MOMENT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_weibo, "分享到微博", TAG_SHARE_WEIBO, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_share_chat, "分享到私信", TAG_SHARE_CHAT, QMUIBottomSheet.BottomGridSheetBuilder.FIRST_LINE)
                .addItem(R.mipmap.icon_more_operation_save, "保存到本地", TAG_SHARE_LOCAL, QMUIBottomSheet.BottomGridSheetBuilder.SECOND_LINE)
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomGridSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView) {
                        dialog.dismiss();
                        int tag = (int) itemView.getTag();
                        switch (tag) {
                            case TAG_SHARE_WECHAT_FRIEND:
                                Toast.makeText(getActivity(), "分享到微信", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_WECHAT_MOMENT:
                                Toast.makeText(getActivity(), "分享到朋友圈", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_WEIBO:
                                Toast.makeText(getActivity(), "分享到微博", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_CHAT:
                                Toast.makeText(getActivity(), "分享到私信", Toast.LENGTH_SHORT).show();
                                break;
                            case TAG_SHARE_LOCAL:
                                Toast.makeText(getActivity(), "保存到本地", Toast.LENGTH_SHORT).show();
                                break;
                            default:break;
                        }
                    }
                }).build().show();

    }

    private void initListView() {
        String[] listItems = new String[]{
                "消息类型对话框（蓝色按钮）",
                "消息类型对话框（红色按钮）",
                "消息类型对话框 (很长文案)",
                "菜单类型对话框",
                "带 Checkbox 的消息确认框",
                "单选菜单类型对话框",
                "多选菜单类型对话框",
                "多选菜单类型对话框(item 数量很多)",
                "带输入框的对话框",
                "高度适应键盘升降的对话框"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        mListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_list_item, data));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showMessagePositiveDialog();
                        break;
                    case 1:
                        showMessageNegativeDialog();
                        break;
                    case 2:
                        showLongMessageDialog();
                        break;
                    case 3:
                        showMenuDialog();
                        break;
                    case 4:
                        showConfirmMessageDialog();
                        break;
                    case 5:
                        showSingleChoiceDialog();
                        break;
                    case 6:
                        showMultiChoiceDialog();
                        break;
                    case 7:
                        showNumerousMultiChoiceDialog();
                        break;
                    case 8:
                        showEditTextDialog();
                        break;
                    case 9:
                        showAutoDialog();
                        break;
                    default:break;
                }
            }
        });
    }

    /**
     * 生成不同类型的对话框
     */
    private void showMessagePositiveDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("标题")
                .setMessage("确定要发送吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT).show();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showMessageNegativeDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("标题")
                .setMessage("确定要删除吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showLongMessageDialog() {
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("标题")
                .setMessage("这是一段很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很" +
                        "长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                        "很长很长很长很长很很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                        "很长很长很长很长很长很长很长很长很长很长很长很长很长很长长很长的文案")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showConfirmMessageDialog() {
        new QMUIDialog.CheckBoxMessageDialogBuilder(getActivity())
                .setTitle("退出后是否删除账号信息?")
                .setMessage("删除账号信息")
                .setChecked(true)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("退出", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showMenuDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3"};
        new QMUIDialog.MenuDialogBuilder(getActivity())
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showSingleChoiceDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3"};
        final int checkedIndex = 1;
        new QMUIDialog.CheckableDialogBuilder(getActivity())
                .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showMultiChoiceDialog() {
        final String[] items = new String[]{"选项1", "选项2", "选项3", "选项4", "选项5", "选项6"};
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getActivity())
                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("提交", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String result = "你选择了 ";
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.create(mCurrentDialogStyle).show();
    }

    private void showNumerousMultiChoiceDialog() {
        final String[] items = new String[]{
                "选项1", "选项2", "选项3", "选项4", "选项5", "选项6",
                "选项7", "选项8", "选项9", "选项10", "选项11", "选项12",
                "选项13", "选项14", "选项15", "选项16", "选项17", "选项18"
        };
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getActivity())
                .setCheckedItems(new int[]{1, 3})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("提交", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                String result = "你选择了 ";
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    result += "" + builder.getCheckedItemIndexes()[i] + "; ";
                }
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.create(mCurrentDialogStyle).show();
    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        builder.setTitle("标题")
                .setPlaceholder("在此输入您的昵称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请填入昵称", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    private void showAutoDialog() {
        QMAutoTestDialogBuilder autoTestDialogBuilder = (QMAutoTestDialogBuilder) new QMAutoTestDialogBuilder(getActivity())
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(getActivity(), "你点了确定", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        autoTestDialogBuilder.create(mCurrentDialogStyle).show();
        QMUIKeyboardHelper.showKeyboard(autoTestDialogBuilder.getEditText(), true);
    }

    class QMAutoTestDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
        private EditText mEditText;

        public QMAutoTestDialogBuilder(Context context) {
            super(context);
            mContext = context;
        }

        public EditText getEditText() {
            return mEditText;
        }

        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);
            mEditText = new EditText(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mEditText, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            mEditText.setHint("输入框");
            LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(getContext(), 15);
            mEditText.setLayoutParams(editTextLP);
            layout.addView(mEditText);
            TextView textView = new TextView(mContext);
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(getContext(), 4), 1.0f);
            textView.setText("观察聚焦输入框后，键盘升起降下时 dialog 的高度自适应变化。\n\n" +
                    "QMUI Android 的设计目的是用于辅助快速搭建一个具备基本设计还原效果的 Android 项目，" +
                    "同时利用自身提供的丰富控件及兼容处理，让开发者能专注于业务需求而无需耗费精力在基础代码的设计上。" +
                    "不管是新项目的创建，或是已有项目的维护，均可使开发效率和项目质量得到大幅度提升。");
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.app_color_description));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(textView);
            return layout;
        }
    }


    private void showBottomSheet() {
        new QMUIBottomSheet.BottomListSheetBuilder(getContext())
                .addItem("使用 QMUI 默认 Dialog 样式")
                .addItem("自定义样式")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        switch (position) {
                            case 0:
                                mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
                                break;
                            case 1:
                                mCurrentDialogStyle =R.style.ReleaseDialogTheme;
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .build().show();
    }
    private void initContent() {
        String isTabletText = booleanToString(QMUIDeviceHelper.isTablet(getContext()));
        QMUIGroupListView.newSection(getContext())
                .addItemView(mGroupListView.createItemView(getString(R.string.deviceHelper_tablet_title)), null)
                .addItemView(mGroupListView.createItemView(getFormatItemValue(String.format("当前设备%1$s平板设备", isTabletText))), null)
                .addTo(mGroupListView);

        String isFlymeText = booleanToString(QMUIDeviceHelper.isFlyme());
        QMUIGroupListView.newSection(getContext())
                .addItemView(mGroupListView.createItemView(getString(R.string.deviceHelper_flyme_title)), null)
                .addItemView(mGroupListView.createItemView(getFormatItemValue(String.format("当前设备%1$s Flyme 系统", isFlymeText))), null)
                .addTo(mGroupListView);

        String isMiuiText = booleanToString(QMUIDeviceHelper.isMIUI());
        QMUIGroupListView.newSection(getContext())
                .addItemView(mGroupListView.createItemView(getString(R.string.deviceHelper_miui_title)), null)
                .addItemView(mGroupListView.createItemView(getFormatItemValue(String.format("当前设备%1$s MIUI 系统", isMiuiText))), null)
                .addTo(mGroupListView);

        String isMeizuText = booleanToString(QMUIDeviceHelper.isMeizu());
        QMUIGroupListView.newSection(getContext())
                .addItemView(mGroupListView.createItemView(getString(R.string.deviceHelper_meizu_title)), null)
                .addItemView(mGroupListView.createItemView(getFormatItemValue(String.format("当前设备%1$s魅族手机", isMeizuText))), null)
                .addTo(mGroupListView);
    }
    private String booleanToString(boolean b) {
        return b ? "是" : "不是";
    }
    private SpannableString getFormatItemValue(CharSequence value) {
        SpannableString result = new SpannableString(value);
        result.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.qmui_config_color_gray_5)), 0, value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return result;
    }
    private void initGroupListView() {
        QMUICommonListItemView normalItem = mGroupListView.createItemView("Item 1");
        normalItem.setOrientation(QMUICommonListItemView.VERTICAL);

        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView("Item 2");
        itemWithDetail.setDetailText("在右方的详细信息");

        QMUICommonListItemView itemWithDetailBelow = mGroupListView.createItemView("Item 3");
        itemWithDetailBelow.setOrientation(QMUICommonListItemView.VERTICAL);
        itemWithDetailBelow.setDetailText("在标题下方的详细信息");

        QMUICommonListItemView itemWithChevron = mGroupListView.createItemView("Item 4");
        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView itemWithSwitch = mGroupListView.createItemView("Item 5");
        itemWithSwitch.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemWithSwitch.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getActivity(), "checked = " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        QMUICommonListItemView itemWithCustom = mGroupListView.createItemView("Item 6");
        itemWithCustom.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        QMUILoadingView loadingView = new QMUILoadingView(getActivity());
        itemWithCustom.addAccessoryCustomView(loadingView);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {
                    CharSequence text = ((QMUICommonListItemView) v).getText();
                    Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
                }
            }
        };
        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 1: 默认提供的样式")
                .setDescription("Section 1 的描述")
                .addItemView(normalItem, onClickListener)
                .addItemView(itemWithDetail, onClickListener)
                .addItemView(itemWithDetailBelow, onClickListener)
                .addItemView(itemWithChevron, onClickListener)
                .addItemView(itemWithSwitch, onClickListener)
                .addTo(mGroupListView);

        QMUIGroupListView.newSection(getContext())
                .setTitle("Section 2: 自定义右侧 View")
                .addItemView(itemWithCustom, onClickListener)
                .addTo(mGroupListView);
    }

    private void initListView2() {
        for (int i = 0; i < 20; i++) {
            mData.add("item " + (i + 1));
        }
        MyAdapter adapter = new MyAdapter(getContext(), mData);
        qmlist.setAdapter(adapter);
    }


    private static class MyAdapter extends QDSimpleAdapter {
        public MyAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alphabutton:
                showSimpleBottomSheetGrid();
                break;
            case R.id.charulistiview:
                initGroupListView();
                break;
            case R.id.add:
                qmlist.manipulate(new QMUIAnimationListView.Manipulator<MyAdapter>() {
                    @Override
                    public void manipulate(MyAdapter adapter) {
                        int position = qmlist.getFirstVisiblePosition();
                        if(mData.size() > position + 4){
                            mData.remove(position + 1);
                            mData.remove(position + 3);
                        }else{
                            Toast.makeText(getContext(), "item 已经很少了，不如先添加几个？", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.delete:
                qmlist.manipulate(new QMUIAnimationListView.Manipulator<MyAdapter>() {
                    @Override
                    public void manipulate(MyAdapter adapter) {
                        int position = qmlist.getFirstVisiblePosition();
                        long current = System.currentTimeMillis();
                        mData.add(position + 1, "item add" + (current + 1));
                        mData.add(position + 2, "item add" + (current + 2));
                        mData.add(position + 3, "item add" + (current + 3));
                    }
                });
                break;
            case R.id.tiaozhuantab:
//                showFragment(this,new QmuiTabSegment2());
                FragmentManager fm = getActivity().getFragmentManager();
                fm.beginTransaction()
                        //替换为TwoFragment
                        .replace(R.id.viewPager,new QMUITabSegment1())
                        .commit();
                break;
default:
    break;
        }
    }
    private void initListView3() {
        String[] listItems = new String[]{
                "Loading 类型提示框",
                "成功提示类型提示框",
                "失败提示类型提示框",
                "信息提示类型提示框",
                "单独图片类型提示框",
                "单独文字类型提示框",
                "自定义内容提示框"
        };
        List<String> data = new ArrayList<>();

        Collections.addAll(data, listItems);

        listView3.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_list_item, data));
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final QMUITipDialog tipDialog;
                switch (position) {
                    case 0:
                        tipDialog = new QMUITipDialog.Builder(getContext())
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                .setTipWord("正在加载")
                                .create();
                        break;
                    case 1:
                        tipDialog = new QMUITipDialog.Builder(getContext())
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                .setTipWord("发送成功")
                                .create();
                        break;
                    case 2:
                        tipDialog = new QMUITipDialog.Builder(getContext())
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                .setTipWord("发送失败")
                                .create();
                        break;
                    case 3:
                        tipDialog = new QMUITipDialog.Builder(getContext())
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
                                .setTipWord("请勿重复操作")
                                .create();
                        break;
                    case 4:
                        tipDialog = new QMUITipDialog.Builder(getContext())
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                .create();
                        break;
                    case 5:
                        tipDialog = new QMUITipDialog.Builder(getContext())
                                .setTipWord("请勿重复操作")
                                .create();
                        break;
                    case 6:
                        tipDialog = new QMUITipDialog.CustomBuilder(getContext())
                                .setContent(R.layout.tipdialog_custom)
                                .create();
                        break;
                    default:
                        tipDialog = new QMUITipDialog.Builder(getContext())
                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                                .setTipWord("正在加载")
                                .create();
                }
                tipDialog.show();
                listView3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, 1500);
            }
        });
    }

    /**
     * 公共方法： 从碎片fragment1跳转到碎片fragment2
     * @param fragment1  当前fragment
     * @param fragment2  跳转后的fragment
     */
    private void showFragment(Fragment fragment1, Fragment fragment2) {
        // 获取 FragmentTransaction  对象
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        //如果fragment2没有被添加过，就添加它替换当前的fragment1
        if (!fragment2.isAdded()) {
            transaction.add(R.id.actionbarLayoutId,fragment2)
                    //加入返回栈，这样你点击返回键的时候就会回退到fragment1了
                    .addToBackStack(null)
                    // 提交事务
                    .commitAllowingStateLoss();

        } else { //如果已经添加过了的话就隐藏fragment1，显示fragment2
            transaction
                    // 隐藏fragment1，即当前碎片
                    .hide(fragment1)
                    // 显示已经添加过的碎片，即fragment2
                    .show(fragment2)
                    // 加入返回栈
                    .addToBackStack(null)
                    // 提交事务
                    .commitAllowingStateLoss();
        }
    }


}
