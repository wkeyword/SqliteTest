package com.changzheng.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by changzheng on 16/3/15.
 *
 *数据库操作类的工具类
 */

public class DBUtils {
    private DBHelper dbHelper;
    public static final String TABLE="t_book";
    public static final String ID="_id";
    public static final String NAME="name";
    public static final String PRICE="price";
    public DBUtils(Context context){
        dbHelper=new DBHelper(context);
    }
//    保存操作
    public int save(ContentValues values){


        SQLiteDatabase db=dbHelper.getWritableDatabase();//写数据库
        long id = db.insert(TABLE, null, values);//插入记录
        db.close();
        return (int)id;
    }

    public int delete(int id){
        int effectNum=0;
        SQLiteDatabase db=dbHelper.getWritableDatabase();//写数据库
        effectNum = db.delete(TABLE,ID+"=?",new String[]{String.valueOf(id)});
        db.close();
        return effectNum;
    }

    public int update(ContentValues contentValues){
        int effectNum=0;
        SQLiteDatabase db=dbHelper.getWritableDatabase();//写数据库
        effectNum = db.update(TABLE,contentValues,ID+"=?",null);
        db.close();
        return effectNum;
    }

    public Cursor findCursor(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, "price desc");
//        使用cursor不能关闭数据库

        return cursor;
    }

    public List<Map<String,Object>> find(){
        List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
        SQLiteDatabase db=dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE, null, null, null, null, null, "price desc");
//        if (cursor.getCount()>0){
//            data=new ArrayList<Map<String,Object>>();
//        }
//        遍历游标
        while (cursor.moveToNext()){
            Map<String,Object> map= new HashMap<String ,Object>();
            map.put(ID,cursor.getInt(cursor.getColumnIndex(ID)));
            map.put(NAME,cursor.getInt(cursor.getColumnIndex(NAME)));
            map.put(PRICE,cursor.getInt(cursor.getColumnIndex(PRICE)));

            data.add(map);

        }
        db.close();
        return data;

    }


    public void pay(int fromUser,int toUser,int money){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {

        db.execSQL("update t_book set price=price-" + money + "where " + ID + "=" + fromUser);



        db.execSQL("update t_book set price=price+" + money + "where " + ID + "=" + toUser);

        db.setTransactionSuccessful();//设置该事务为成功这样的标准,默认为不成功,即回滚
        }finally {
            db.endTransaction();//事务的提交

            db.close();
        }




    }


}
