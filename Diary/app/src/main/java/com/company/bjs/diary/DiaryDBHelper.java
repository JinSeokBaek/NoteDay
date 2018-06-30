package com.company.bjs.diary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDBHelper extends SQLiteOpenHelper {
    final static String _ID="_id";
    final static String _TITLE="title";
    final static String _DATE="date";
    final static String _DATE2="date2";
    final static String _CONTENT="_content";
    final static String _COLOR="_color";
    final static String _NOTE="_note";
    final static String TABLE_NAME1="diary";


    final static String QUERY_SELECT_ALL1=String.format("select * from %s order by %s asc",TABLE_NAME1,_ID);


    public DiaryDBHelper(Context context) {
        super(context, "MyData.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1=String.format("create table %s(" +
                "%s integer primary key autoincrement," +
                "%s text," +
                "%s text," +
                "%s text," +
                "%s text," +
                "%s text," +
                "%s text);",TABLE_NAME1,_ID,_TITLE,_DATE,_DATE2,_CONTENT,_COLOR,_NOTE);

        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
