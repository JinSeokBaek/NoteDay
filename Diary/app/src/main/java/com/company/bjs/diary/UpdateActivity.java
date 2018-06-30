package com.company.bjs.diary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.company.bjs.diary.DiaryDBHelper.TABLE_NAME1;
import static com.company.bjs.diary.DiaryDBHelper._COLOR;
import static com.company.bjs.diary.DiaryDBHelper._CONTENT;
import static com.company.bjs.diary.DiaryDBHelper._DATE;
import static com.company.bjs.diary.DiaryDBHelper._DATE2;
import static com.company.bjs.diary.DiaryDBHelper._ID;
import static com.company.bjs.diary.DiaryDBHelper._TITLE;

public class UpdateActivity extends AppCompatActivity {
    BackPressCloseHandler back;
    EditText etTitle, etContent;
    DatePicker datePicker;

    DiaryDBHelper dbHelper;
    SQLiteDatabase db;

    LinearLayout llupdate;

    //RadioButton rb1,rb2,rb3,rb4,rb5;

    int _id;

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }

        dbHelper = new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();

        back = new BackPressCloseHandler(this, "수정이 취소");
        etTitle = findViewById(R.id.etTitle_upgrade);
        etContent = findViewById(R.id.etContent_upgrade);
        datePicker = findViewById(R.id.datePicker_upgrade);

        llupdate=findViewById(R.id.llupdate);
        /* COLOR */
//        rb1=findViewById(R.id.rb1);
//        rb2=findViewById(R.id.rb2);
//        rb3=findViewById(R.id.rb3);
//        rb4=findViewById(R.id.rb4);
//        rb5=findViewById(R.id.rb5);
//
//        rb1.setOnCheckedChangeListener(new ColorChangeListener(1));
//        rb2.setOnCheckedChangeListener(new ColorChangeListener(2));
//        rb3.setOnCheckedChangeListener(new ColorChangeListener(3));
//        rb4.setOnCheckedChangeListener(new ColorChangeListener(4));
//        rb5.setOnCheckedChangeListener(new ColorChangeListener(5));

        Intent it = getIntent();
        _id = it.getIntExtra("id", 0);
//        Toast.makeText(getApplicationContext(),_id+"",Toast.LENGTH_SHORT).show();

        dbHelper = new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery(String.format("select * from %s where %s=%s", TABLE_NAME1, _ID, _id), null);

        cursor.moveToNext();
        String title=cursor.getString(cursor.getColumnIndex(_TITLE));
        String content=cursor.getString(cursor.getColumnIndex(_CONTENT));
        String date1=cursor.getString(cursor.getColumnIndex(_DATE));
        String color=cursor.getString(cursor.getColumnIndex(_COLOR));
        setTitle(title);
        etTitle.setText(title);
        etContent.setText(content);
        try {
            String date[]=date1.split("\\.");
            datePicker.updateDate(Integer.parseInt(date[0]),Integer.parseInt(date[1])-1,Integer.parseInt(date[2]));

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),title+" "+content+" "+date1,Toast.LENGTH_SHORT).show();
        }


    }
    //int color=1;
//    private class ColorChangeListener implements CompoundButton.OnCheckedChangeListener {
//        int colorcode;
//        public ColorChangeListener(int colorcode) {
//            super();
//            this.colorcode=colorcode;
//        }
//
//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean i) {
//            switch (colorcode){
//                case 1:
//                    llupdate.setBackgroundColor(Color.WHITE);
//                    color=1;
//                    break;
//                case 2:
//                    llupdate.setBackgroundColor(getResources().getColor(R.color.color_black));
//                    color=2;
//                    break;
//                case 3:
//                    llupdate.setBackgroundColor(getResources().getColor(R.color.color_red));
//                    color=3;
//                    break;
//                case 4:
//                    llupdate.setBackgroundColor(getResources().getColor(R.color.color_black));
//                    color=4;
//                    break;
//                case 5:
//                    llupdate.setBackgroundColor(getResources().getColor(R.color.color_black));
//                    color=5;
//                    break;
//
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        back.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.action_submit) {
            try {
                String title = etTitle.getText().toString();
                if(title.length()==0)
                    title="무제";
                String content = etContent.getText().toString();
                if(content.length()==0){
                    Toast.makeText(getApplicationContext(),"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return false;
                }
                String date = "";
                date += datePicker.getYear() + ".";
                if (datePicker.getMonth() < 10)
                    date += "0" + (datePicker.getMonth()+1) + ".";
                else
                    date += (datePicker.getMonth()+1) + ".";
                if (datePicker.getDayOfMonth() < 10)
                    date += "0" + datePicker.getDayOfMonth();
                else
                    date += datePicker.getDayOfMonth();
                SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat( "yyyy.MM.dd", Locale.KOREA );
                Date currentTime = new Date();

                String now=mSimpleDateFormat.format ( currentTime );

                String query=String.format("update %s " +
                        "set " +
                        "%s='%s'," +
                        "%s='%s'," +
                        "%s='%s'," +
                        "%s='%s' " +
                        "where %s=%s", TABLE_NAME1,_TITLE,title,_DATE,date,_CONTENT,content,_COLOR,null,_ID,_id);
                exeQuery(query);
                setResult(RESULT_OK);
                finish();
//                Toast.makeText(getApplicationContext(),date+title+content,Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "저장에 실패하였습니다." + e, Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean exeQuery(String query) {
        try {
            db.execSQL(query);
            return true;
        } catch (Exception e) {
            Toast.makeText(getApplication(),e+"",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
