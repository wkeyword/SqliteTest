package com.changzheng.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ListView mBooklistView;
    private SimpleCursorAdapter simpleCursorAdapter;
    private EditText mName;
    private EditText mPrice;
    private Context context;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        mBooklistView= (ListView) findViewById(android.R.id.list);
        mName= (EditText) findViewById(R.id.name_et);
        mPrice= (EditText) findViewById(R.id.price);

//        ListView需要展示数据,列表项布局   初始数据,来自数据库 适配simplecursorAdapter   listview设置坚挺
         cursor=new DBUtils(this).findCursor();

//        游标适配器
        simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.book_item,cursor,
                new String[]{DBUtils.ID,DBUtils.NAME,DBUtils.PRICE},
                new int[]{R.id.book_id_tv,R.id.book_name_tv,R.id.book_price_tv},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        mBooklistView.setAdapter(simpleCursorAdapter);

//        长按监听
        mBooklistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DBUtils dbUtils=new DBUtils(context);


//                cursor.moveToPosition(position);
//                int id1=cursor.getInt(cursor.getColumnIndex("_id"));

//                Toast.makeText(getApplicationContext(),"长按"+id ,Toast.LENGTH_SHORT).show();
                dbUtils.delete((int)id);
                cursor.requery();//查询数据库的话,建议开辟子线程
                simpleCursorAdapter.notifyDataSetChanged();//数据集改变的通知,关联的listview就会重新刷新


                return false;
            }
        });
    }

    public void add(View view){
        String name=mName.getText().toString().trim();
        String price=mPrice.getText().toString().trim();
        DBUtils dbUtils=new DBUtils(context);
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBUtils.NAME,name);
        contentValues.put(DBUtils.PRICE,Integer.valueOf(price));
        dbUtils.save(contentValues);
        cursor.requery();
        simpleCursorAdapter.notifyDataSetChanged();
        mName.setText(null);
        mPrice.setText(null);
    }

    public void delete(View v){
        TextView bookid= (TextView) findViewById(R.id.book_id_tv);
        String id=bookid.getText().toString().trim();
        DBUtils dbUtils=new DBUtils(context);
        dbUtils.delete(Integer.valueOf(id));
        cursor.requery();
        simpleCursorAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        System.out.println("ondestory");
        if (cursor!=null){
            cursor.close();
            cursor=null;
        }
        super.onDestroy();

    }
}
