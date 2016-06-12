package com.yunsheng.todo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shengyun on 16/6/8.
 */
public class MyDBHelper extends SQLiteOpenHelper {
    // 字段
    public static final String TIME = "time";
    public static final String THING = "thing";
    public static final String STATUS = "status";
    public static final String TABLENAME = "Item";
    public static final String DBNAME = "ToDoList.db";
    public static final int DBVERSION = 3;


    public static final String CREATE_TODOITEM = "create table Item (" +
            "id integer primary key autoincrement, " +
            "time text, " +
            "thing text, " +
            "status integer)";

    private Context mycontext;
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mycontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODOITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("yunsheng","onUpgrade");
        db.execSQL("drop table if exists " + TABLENAME);
        db.execSQL(CREATE_TODOITEM);

        // 这种写法比较好
        if (oldVersion==1 && newVersion==2) {//升级判断,如果再升级就要再加两个判断,从1到3,从2到3
            db.execSQL("ALTER TABLE " + TABLENAME + " ADD " + STATUS + " TEXT;");
        }
    }
}
