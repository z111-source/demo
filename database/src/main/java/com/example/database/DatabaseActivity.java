package com.example.database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import com.example.database.R;

public class DatabaseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_data;
    private String mDatabaseName;
    private String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        findViewById(R.id.database_create).setOnClickListener(this);
        findViewById(R.id.database_delete).setOnClickListener(this);
        tv_data = findViewById(R.id.tv_database);
        mDatabaseName = getFilesDir()+"/test.db";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.database_create:
                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(mDatabaseName,
                        MODE_PRIVATE, null);
                desc = String.format("数据库%s创建%s",sqLiteDatabase.getPath(),
                        (sqLiteDatabase!=null)? "成功":"失败");
                tv_data.setText(desc);
                break;
            case R.id.database_delete:
                boolean b = deleteDatabase(mDatabaseName);
                desc=String.format("数据库%s删除%s",mDatabaseName,b);
                tv_data.setText(desc);
                break;

        }
    }
}