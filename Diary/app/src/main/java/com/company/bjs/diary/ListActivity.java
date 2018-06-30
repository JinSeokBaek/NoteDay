package com.company.bjs.diary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static com.company.bjs.diary.DiaryDBHelper.QUERY_SELECT_ALL1;
import static com.company.bjs.diary.DiaryDBHelper._COLOR;
import static com.company.bjs.diary.DiaryDBHelper._DATE;
import static com.company.bjs.diary.DiaryDBHelper._DATE2;
import static com.company.bjs.diary.DiaryDBHelper._ID;
import static com.company.bjs.diary.DiaryDBHelper._TITLE;

public class ListActivity extends AppCompatActivity {
    BackPressCloseHandler back;
    DiaryDBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ListDBAdapter listDBAdapter;
    ListView lvList;

    final static int LISTACTIVITY=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }


        lvList = findViewById(R.id.lvList);

        dbHelper = new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery(QUERY_SELECT_ALL1, null);
        if (cursor.moveToNext())
            lvList.setVisibility(View.VISIBLE);
        else
            lvList.setVisibility(View.GONE);

        listDBAdapter=new ListDBAdapter(this,cursor);
        lvList.setAdapter(listDBAdapter);
        back = new BackPressCloseHandler(this, "종료");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(ListActivity.this,InsertActivity.class);
                startActivityForResult(it,LISTACTIVITY);
            }
        });

    }
    public void refreshDB() {
        cursor = db.rawQuery(String.format(QUERY_SELECT_ALL1), null);
        listDBAdapter.changeCursor(cursor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK)
            refreshDB();
        cursor = db.rawQuery(QUERY_SELECT_ALL1, null);
        if (cursor.moveToNext())
            lvList.setVisibility(View.VISIBLE);
        else
            lvList.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDB();
    }

    @Override
    public void onBackPressed() {
        back.onBackPressed();
    }

    private class ListDBAdapter extends CursorAdapter {
        public ListDBAdapter(Context context, Cursor c) {
            super(context, c, true);
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.layout_list, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvTitle=view.findViewById(R.id.tvTitle);
            TextView tvDate=view.findViewById(R.id.tvDate);
            TextView tvDate2=view.findViewById(R.id.tvDate2);
            LinearLayout ll_list=view.findViewById(R.id.ll_list);

            final int id=cursor.getInt(cursor.getColumnIndex(_ID));
            String title=cursor.getString(cursor.getColumnIndex(_TITLE));
            String date1=cursor.getString(cursor.getColumnIndex(_DATE));
            String date2=cursor.getString(cursor.getColumnIndex(_DATE2));
            String color=cursor.getString(cursor.getColumnIndex(_COLOR));

            tvTitle.setText(title);
            tvDate.setText(date1);
            if(!date1.equals(date2)){
                tvDate2.setVisibility(View.VISIBLE);
                tvDate2.setText("작성일)"+date2);
            }
            switch (color){
                case "1":
                    ll_list.setBackgroundColor(getResources().getColor(R.color.color_default));
                    break;
                case "2":
                    ll_list.setBackgroundColor(getResources().getColor(R.color.color_black));
                    break;
                case "3":
                    ll_list.setBackgroundColor(getResources().getColor(R.color.color_red));
                    break;
                case "4":
                    ll_list.setBackgroundColor(getResources().getColor(R.color.color_black));
                    break;
                case "5":
                    ll_list.setBackgroundColor(getResources().getColor(R.color.color_black));
                    break;
            }
            ll_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it=new Intent(ListActivity.this,ContentActivity.class);
                    it.putExtra("id",id);
                    startActivityForResult(it,LISTACTIVITY);
                }
            });
        }
    }
}
