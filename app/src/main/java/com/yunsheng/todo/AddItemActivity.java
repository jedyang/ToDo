package com.yunsheng.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yunsheng.todo.helper.MyDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {

    private MyDBHelper dbHelper;

    private EditText thingEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        dbHelper = new MyDBHelper(this, MyDBHelper.DBNAME, null, MyDBHelper.DBVERSION);

        Button doneBtn = (Button) findViewById(R.id.done_btn);
        thingEt = (EditText) findViewById(R.id.todothing);


        assert doneBtn != null;
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thing = thingEt.getText().toString();
                if (!(null == thing || thing.trim().equals(""))) {
                    // 保存数据
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(MyDBHelper.THING, thingEt.getText().toString());
                    String now = getNow();
                    values.put(MyDBHelper.TIME, now);
                    values.put(MyDBHelper.STATUS, 1);
                    database.insert(MyDBHelper.TABLENAME, null, values);
                }
                // 返回列表
                Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }

    private String getNow() {
        Date date = new Date(
                System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sdf.format(date);

        return now;

    }

    @Override
    protected void onStop() {
        super.onStop();
        thingEt.setText(null, null);
    }
}
