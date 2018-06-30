package com.company.bjs.diary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.company.bjs.diary.DiaryDBHelper.QUERY_SELECT_ALL1;
import static com.company.bjs.diary.DiaryDBHelper.TABLE_NAME1;
import static com.company.bjs.diary.DiaryDBHelper._COLOR;
import static com.company.bjs.diary.DiaryDBHelper._CONTENT;
import static com.company.bjs.diary.DiaryDBHelper._DATE;
import static com.company.bjs.diary.DiaryDBHelper._DATE2;
import static com.company.bjs.diary.DiaryDBHelper._ID;
import static com.company.bjs.diary.DiaryDBHelper._TITLE;

public class ContentActivity extends AppCompatActivity {
    DiaryDBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    int _id;

    LinearLayout llContent;

    TextView tvContent,tvDate,tvDate2;

    public static final int CONTENT_ACTIVITY = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(getResources().getColor(R.color.actionbar));
        }
        tvContent = findViewById(R.id.tvContent);
        tvDate = findViewById(R.id.tvDate_content);
        tvDate2 = findViewById(R.id.tvDate_content2);

        llContent=findViewById(R.id.llcontent);
        Intent it = getIntent();
        _id = it.getIntExtra("id", 0);
//        Toast.makeText(getApplicationContext(),_id+"",Toast.LENGTH_SHORT).show();

        dbHelper = new DiaryDBHelper(this);
        db = dbHelper.getWritableDatabase();

        setContent();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {

            AlertDialog.Builder alt = new AlertDialog.Builder(ContentActivity.this,R.style.AlertDialogCustom);
            alt.setTitle("삭제하시겠습니까?");
            alt.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        db.execSQL(String.format("delete from %s where %s=%s", TABLE_NAME1, _ID, _id));
                        setResult(RESULT_OK);
                        Toast.makeText(ContentActivity.this, "삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(ContentActivity.this, "삭제에 실패하였습니다." + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alt.setNegativeButton("아니요", null);
            alt.show();


        } else if (id == R.id.action_update) {
            Intent it = new Intent(ContentActivity.this, UpdateActivity.class);
            it.putExtra("id", _id);
            startActivityForResult(it, CONTENT_ACTIVITY);
        }

        return super.onOptionsItemSelected(item);
    }
    private void setContent(){
        cursor = db.rawQuery(String.format("select * from %s where %s=%s", TABLE_NAME1, _ID, _id), null);


        cursor.moveToNext();
        String title=cursor.getString(cursor.getColumnIndex(_TITLE));
        String content=cursor.getString(cursor.getColumnIndex(_CONTENT));
        String date1=cursor.getString(cursor.getColumnIndex(_DATE));
        String date2=cursor.getString(cursor.getColumnIndex(_DATE2));
        String color=cursor.getString(cursor.getColumnIndex(_COLOR));
        setTitle(title);
        tvContent.setText(content);
        tvDate.setText(date1);
        if(!date1.equals(date2)){
            tvDate2.setVisibility(View.VISIBLE);
            tvDate2.setText("작성일)"+date2);
        }
        switch (color){
            case "1":
                llContent.setBackgroundColor(getResources().getColor(R.color.color_white));
                break;
            case "2":
                llContent.setBackgroundColor(getResources().getColor(R.color.color_black));
                break;
            case "3":
                llContent.setBackgroundColor(getResources().getColor(R.color.color_red));
                break;
            case "4":
                llContent.setBackgroundColor(getResources().getColor(R.color.color_black));
                break;
            case "5":
                llContent.setBackgroundColor(getResources().getColor(R.color.color_black));
                break;
            default:
                llContent.setBackgroundColor(getResources().getColor(R.color.background));
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            setContent();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }
}
