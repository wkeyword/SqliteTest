package com.changzheng.sqlitetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by changzheng on 16/3/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "book_data.db";
    private static final int DB_VERSION = 2;

    //context上下文   name  数据库名字  factory 游标工厂  version 数据库版本 大于0的整数
    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DB_VERSION);
    }


//    数据库初始化回调方法,一般做数据库初始化的初始化操作,比如创建表 添加初始数据等

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("wkeyword","oncreate");
//        创建表
        db.execSQL("create table t_book(_id integer primary key autoincrement,name text,price integer)");

    }


//    当数据库版本更新时,回调该方法,newversion>oldversion,一般添加表,改表结构,删除表等操作
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("wkeyword","onUpgrade");

//        删除原来的表,
        db.execSQL("drop table if exists t_book");
        db.execSQL("create table t_book(_id integer primary key autoincrement,name text,price integer)");

    }


}
