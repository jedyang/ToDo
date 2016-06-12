package com.yunsheng.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.yunsheng.todo.dao.ItemDO;
import com.yunsheng.todo.helper.ItemAdapter;
import com.yunsheng.todo.helper.MyDBHelper;
import com.yunsheng.todo.helper.ViewHolder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton addbtn;
    private ItemAdapter adapter;
    private MyDBHelper myDBHelper;
    private ArrayList<ItemDO> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mycode
        addbtn = (ImageButton) findViewById(R.id.addBtn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);

            }
        });

        // 查数据库，组装
        myDBHelper = new MyDBHelper(this, MyDBHelper.DBNAME, null, MyDBHelper.DBVERSION);
        assembleList();

        adapter = new ItemAdapter(MainActivity.this, R.layout.item_list, data);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder tag = (ViewHolder) parent.getTag();
                // 改变checkbox的状态
//                tag.checkBox.toggle();
                hasDone(position);
                // 刷新一下
                assembleList();
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void hasDone(int position) {
        MyDBHelper myDBHelper = new MyDBHelper(this, MyDBHelper.DBNAME, null, MyDBHelper.DBVERSION);

        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", 2);
        ItemDO item = adapter.getItem(position);
        String id = String.valueOf(item.getId());
        int result = database.update(MyDBHelper.TABLENAME,values,"id = ?",new String[] {id});
        Log.i("yunsheng", result + " has been update");
    }

    public void assembleList() {
        //  清空
        data.clear();

        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        Cursor cursor = database.query(MyDBHelper.TABLENAME, null, "status = ?", new String[]{"1"}, null, null, null);
        if (cursor.moveToFirst()) {

            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int status = cursor.getInt(cursor.getColumnIndex(MyDBHelper.STATUS));
                String thing = cursor.getString(cursor.getColumnIndex(MyDBHelper.THING));
                String time = cursor.getString(cursor.getColumnIndex(MyDBHelper.TIME));
                ItemDO item = new ItemDO();
                item.setId(id);
                item.setThing(thing);
                item.setStatus(status);
                item.setTime(time);
                data.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
