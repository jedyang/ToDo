package com.yunsheng.todo.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yunsheng.todo.MainActivity;
import com.yunsheng.todo.R;
import com.yunsheng.todo.dao.ItemDO;

import android.graphics.Paint;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shengyun on 16/6/10.
 */
public class ItemAdapter extends ArrayAdapter<ItemDO> {
    private int resourceId;
    private Context superContext;
    View view;
    ViewHolder viewHolder;

    // 用来控制checkBox的选中情况
    private HashMap<Integer, Boolean> checkMap;

    public ItemAdapter(Context context, int textViewResourceId, List<ItemDO> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
        this.superContext = context;
        this.checkMap = new HashMap<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemDO item = getItem(position);

        if (null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.item_id);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(item.getThing());
        viewHolder.checkBox.setChecked(2 == item.getStatus());

        // checkbox上添加事件
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haveDone(position);

//                // 完成的字体加删除线
////                viewHolder.textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                MainActivity mainActivity = (MainActivity) superContext;
                ItemAdapter adapter = mainActivity.getAdapter();
                mainActivity.assembleList();
                adapter.notifyDataSetChanged();

            }
        });

        return view;

    }


    private void haveDone(int position) {
        MyDBHelper myDBHelper = new MyDBHelper(superContext, MyDBHelper.DBNAME, null, MyDBHelper.DBVERSION);

        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", 2);
        ItemDO item = this.getItem(position);
        String id = String.valueOf(item.getId());
        int result = database.update(MyDBHelper.TABLENAME, values, "id = ?", new String[]{id});
        Log.i("yunsheng", result + " has been update");
    }

}
