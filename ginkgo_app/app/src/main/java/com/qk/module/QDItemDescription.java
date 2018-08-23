package com.qk.module;

import com.kongzue.baseframework.BaseFragment;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * QDItemDescription
 */
public class QDItemDescription {
    private Class<? extends BaseFragment> mKitDemoClass;
    private String mKitName;
    private String mKitDetailDescription;
    private int mIconRes;

    public QDItemDescription(Class<? extends BaseFragment> kitDemoClass, String kitName){
        this(kitDemoClass, kitName, 0);
    }


    public QDItemDescription(Class<? extends BaseFragment> kitDemoClass, String kitName, int iconRes) {
        mKitDemoClass = kitDemoClass;
        mKitName = kitName;
        mIconRes = iconRes;
    }

    public QDItemDescription(Class<? extends BaseFragment> kitDemoClass, String kitName,
                             String kitDetailDescription, int iconRes) {
        mKitDemoClass = kitDemoClass;
        mKitName = kitName;
        mKitDetailDescription = kitDetailDescription;
        mIconRes = iconRes;
    }

    public void setItemDetailDescription(String kitDetailDescription) {
        mKitDetailDescription = kitDetailDescription;
    }

    public Class<? extends BaseFragment> getDemoClass() {
        return mKitDemoClass;
    }

    public String getName() {
        return mKitName;
    }

    public String getItemDetailDescription() {
        return mKitDetailDescription;
    }

    public int getIconRes() {
        return mIconRes;
    }
}
