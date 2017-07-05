package com.yunsheng.todo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yunsheng.todo.dao.ItemDO;
import com.yunsheng.todo.helper.ItemAdapter;
import com.yunsheng.todo.helper.MyDBHelper;
import com.yunsheng.todo.helper.MyPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button addbtn;
    private ItemAdapter adapter;
    private MyDBHelper myDBHelper;
    private ArrayList<ItemDO> data = new ArrayList<>();

    // 卡片切换效果
    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
//    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        InitTextView();
        InitViewPager();

        // mycode
        addbtn = (Button) findViewById(R.id.addBtn);

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
                // TODO
            }
        });


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
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//
//        return true;

        //在右上角加一个按钮
        MenuItem mun1=menu.add(0, 0, 0, "Item 1");
        {
            mun1.setIcon(android.R.drawable.sym_def_app_icon);
            mun1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case 0:
                //此处写点击这个按钮的事件
                Log.i("yunsheng","menu click");
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化头标
     */
    private void InitTextView() {
        t1 = (TextView) findViewById(R.id.text1);
        t2 = (TextView) findViewById(R.id.text2);

        t1.setOnClickListener(new MyOnClickListener(0));
        t2.setOnClickListener(new MyOnClickListener(1));
    }

    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        listViews.add(mInflater.inflate(R.layout.todo_list, null));
        listViews.add(mInflater.inflate(R.layout.done_list, null));
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
//        cursor = (ImageView) findViewById(R.id.cursor);
//        bmpW = BitmapFactory.decodeResource(getResources(), android.graphics.drawable.Animatable)
//                .getWidth();// 获取图片宽度
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels;// 获取分辨率宽度
//        offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        cursor.setImageMatrix(matrix);// 设置动画初始位置
    }


    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                    }
                    break;
                case 1:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, one, 0, 0);
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                    }
                    break;
                case 2:
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(offset, two, 0, 0);
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
//            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public ItemAdapter getAdapter() {
        return adapter;
    }
}
