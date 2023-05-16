package com.example.database;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class LoginForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed_first;
    private EditText ed_second;
    private EditText ed_message;
    private String mPhone;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget);
        ed_first = findViewById(R.id.et_password_first);
        ed_second = findViewById(R.id.et_password_second);
        ed_message = findViewById(R.id.et_inputMessage);
        findViewById(R.id.btn_getMessage).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        mPhone =getIntent().getStringExtra("phone");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_getMessage:
                if(mPhone.length()!=11){
                    Toast.makeText(this, "请输入正确手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                message = String.format("%06d",new Random().nextInt(999999));
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("请记住验证码");
                builder.setMessage(message);
                builder.setPositiveButton("好的",null);
                AlertDialog dialog=builder.create();
                dialog.show();
                break;
            case R.id.btn_confirm:
                String password_first=ed_first.getText().toString();
                String password_second=ed_second.getText().toString();
                if (!password_first.equals(password_second)){
                    Toast.makeText(this, "请输入一致密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!message.equals(ed_message.getText().toString())){
                    Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra("new_password",password_first);
                setResult(Activity.RESULT_OK,intent);
                finish();
        }
    }
}