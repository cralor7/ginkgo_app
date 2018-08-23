package com.qk.qqface;

import android.content.Context;
import android.util.AttributeSet;

import com.qk.QDQQFaceManager;
import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler;
import com.qmuiteam.qmui.qqface.QMUIQQFaceView;


/**
 * @author cginechen
 * @date 2016-12-24
 */

public class QDQQFaceView extends QMUIQQFaceView {
    public QDQQFaceView(Context context) {
        this(context, null);
    }

    public QDQQFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCompiler(QMUIQQFaceCompiler.getInstance(QDQQFaceManager.getInstance()));
    }
}
