package com.example.database;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.database.database.UserDBHelper;
import com.example.database.entity.User;
import com.example.database.util.ToastUtil;

import java.util.List;

public class SQLiteHelperActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_name;
    private EditText ed_age;
    private EditText ed_height;
    private EditText ed_weight;
    private CheckBox ck_married;
    private UserDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_helper);
        findViewById(R.id.bt_add).setOnClickListener(this);
        findViewById(R.id.bt_delete).setOnClickListener(this);
        findViewById(R.id.bt_update).setOnClickListener(this);
        findViewById(R.id.bt_query).setOnClickListener(this);
        ck_married = findViewById(R.id.ck_married);
        ed_name = findViewById(R.id.e_name);
        ed_age = findViewById(R.id.e_age);
        ed_height = findViewById(R.id.e_height);
        ed_weight = findViewById(R.id.e_weight);
    }

    protected void onStart() {
        super.onStart();
        mHelper = UserDBHelper.getInstance(this);   //this
        mHelper.openReadLink();
        mHelper.openWriteLink();
    }

    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }


    @Override
    public void onClick(View v) {
        String name = ed_name.getText().toString();
        String age = ed_age.getText().toString();
        String height = ed_height.getText().toString();
        String weight = ed_weight.getText().toString();
        User user;

        switch (v.getId()) {
            case R.id.bt_add:
                user = new User(name,
                        Integer.parseInt(age),
                        Long.parseLong(height),
                        Float.parseFloat(weight),
                        ck_married.isChecked());
                if (mHelper.insert(user) > 0) {
                    ToastUtil.show(this, "添加成功");
                }
                break;
            case R.id.bt_delete:
                if (mHelper.deleteByName(name) > 0) {
                    ToastUtil.show(this, "删除成功");
                }
                break;
            case R.id.bt_update:
                user = new User(name,
                        Integer.parseInt(age),
                        Long.parseLong(height),
                        Float.parseFloat(weight),
                        ck_married.isChecked());
                if (mHelper.update(user) > 0) {
                    ToastUtil.show(this, "更新成功");
                }
                break;
            case R.id.bt_query:
                List<User> list = mHelper.queryByName(name);
                for (User u : list) {
                    Log.d("myLog", u.toString());
                }
                break;
        }
    }
}