package com.company.bjs.diary;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.company.bjs.diary.DiaryDBHelper.TABLE_NAME1;
import static com.company.bjs.diary.DiaryDBHelper._COLOR;
import static com.company.bjs.diary.DiaryDBHelper._CONTENT;
import static com.company.bjs.diary.DiaryDBHelper._NOTE;
import static com.company.bjs.diary.DiaryDBHelper._TITLE;

public class InsertActivity extends AppCompatActivity {
    BackPressCloseHandler back;
    EditText etTitle, etContent;
    DatePicker datePicker;

    DiaryDBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        setTitle("");

        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }

        dbHelper = new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();
        back = new BackPressCloseHandler(this, "작성이 취소");

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        datePicker = findViewById(R.id.datePicker);
    }

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
                if (datePicker.getMonth()+1 < 10)
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

                String query=String.format("insert into %s values(null,'%s','%s','%s','%s','%s','%s')", TABLE_NAME1,title,date,now,content,null,null);
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
