package com.qk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * SQLiteHelper
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String dbname = "REBOT.db";
    private static final int version = 1;
    private static SQLiteHelper dbHelper;
    /**
     * 也可以不指定字段的类型、长度，因为int类型也可以保存Char类型的创建学生表
     */
    private final String createTb = "CREATE TABLE User (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR2, avatar BLOB)";

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public SQLiteHelper(Context context) {
        super(context, dbname, null, version);
    }
    public static SQLiteHelper getInstance(Context context) {
        //单例模式
        if (dbHelper == null) {
            dbHelper = new SQLiteHelper(context);
        }
        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建一个数据库表 User ，字段：_id、name、avatar。
        db.execSQL(createTb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}