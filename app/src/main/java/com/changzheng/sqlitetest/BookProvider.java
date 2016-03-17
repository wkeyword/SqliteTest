package com.changzheng.sqlitetest;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by changzheng on 16/3/17.
 */
public class BookProvider extends ContentProvider {
    private DBHelper mdbHelper;
    public static final String TABLE="t_book";
    public static final String ID="_id";
    public static final String NAME="name";
    public static final String PRICE="price";
//    构建一个uri匹配对象,假如都不匹配,则返回一个错误的错误码
    private static UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    private static final String AUTHORITY = "BookProvider";

    private static final int MULTI = 1;
    private static final int SIGLE = 2;

    //    注册或者添加一个合法的Uri
    static {
//    添加uri
        uriMatcher.addURI(AUTHORITY,"book",MULTI);//多行记录
        uriMatcher.addURI(AUTHORITY,"book/#",SIGLE);//单行记录,#来代替数字
}
//    创建内容提供者,初始化操作,创建后,就不会再被创建,内容提供者.  可以通过 getContext来取得上下文

    @Override
    public boolean onCreate() {
        mdbHelper=new DBHelper(getContext());

        return false;
    }
//查询操作 uri统一资源标识符    projection 字段列表 selection条件   selectionArgs 条件参数   sortOrder排序
//                           null 表示所有列
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db=mdbHelper.getReadableDatabase();
//        不能关闭数据库,是游标
        int code = uriMatcher.match(uri);//返回的是匹配码
        switch (code){
            case MULTI:
                return db.query(TABLE,projection,selection,selectionArgs,null,null,sortOrder);
            case SIGLE:
                long id = ContentUris.parseId(uri);//解析uri的后缀,吧后缀的id取出来

                if (!selection.isEmpty()){
                    selection=selection+" and "+ID+"="+id;
                }else {
                    selection=ID+"="+id;
                }
                break;
            default:
                db.close();
                throw new IllegalArgumentException("Uri不合法!");
        }
        return null;

    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
