package com.example.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database.database.LoginDBHelper;
import com.example.database.entity.LoginInfo;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {

    private RadioButton rb_mess;
    private RadioButton rb_pass;
    private EditText ed_phone;
    private EditText ed_password;
    private CheckBox ckb_remember;
    private RadioGroup rg_log;
    private TextView tv_pass;
    private Button btn_forget;
    private String mPassword = "111111";
    private ActivityResultLauncher<Intent> register;
    private String message;
    private LoginDBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rg_log = findViewById(R.id.rg_login);
        rg_log.setOnCheckedChangeListener(this);

        rb_mess = findViewById(R.id.rb_message);
        rb_pass = findViewById(R.id.rb_password);

        ed_phone = findViewById(R.id.et_phone);
        ed_password = findViewById(R.id.et_password);

        tv_pass = findViewById(R.id.tv_password);

        btn_forget = findViewById(R.id.bt_forget);
        btn_forget.setOnClickListener(this);

        ckb_remember = findViewById(R.id.ck_remember);
        findViewById(R.id.bt_login).setOnClickListener(this);

        findViewById(R.id.bt_forget).setOnClickListener(this);
        //registerForActivityResult
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                (ActivityResult result) -> {
                    Intent intent = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK && intent != null) {
                        mPassword = intent.getStringExtra("new_password");
                    }
                });
        ed_password.setOnFocusChangeListener(this);
    }


    //重载
    private void reload() {
        LoginInfo info = mHelper.queryTop();
        if (info != null && info.remPass) {
            ed_password.setText(info.password);
            ed_phone.setText(info.phone);
            ckb_remember.setChecked(true);
        }
    }

    protected void onStart() {
        super.onStart();
        mHelper = LoginDBHelper.getInstance(this);   //this
        mHelper.openReadLink();
        mHelper.openWriteLink();

        reload();
    }

    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }

    @Override
    public void onClick(View v) {
        String phone = ed_phone.getText().toString();
        switch (v.getId()) {
            case R.id.bt_forget:
                if (phone.length() != 11) {
                    Toast.makeText(this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rb_pass.isChecked()) {

                    Intent intent = new Intent(this, LoginForgetActivity.class);
                    intent.putExtra("phone", phone);
                    register.launch(intent);
                } else if (rb_mess.isChecked()) {
                    message = String.format("%06d", new Random().nextInt(999999));
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("请记住验证码");
                    builder.setMessage("验证码：" + message);
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.bt_login:
                if (rb_pass.isChecked()) {
                    if (!mPassword.equals(ed_password.getText().toString())) {
                        Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loginSuccess();
                } else if (rb_mess.isChecked()) {
                    if (!message.equals(ed_password.getText().toString())) {
                        Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loginSuccess();
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_password:
                tv_pass.setText("登录密码：");
                ckb_remember.setVisibility(View.VISIBLE);
                ed_password.setHint("请输入密码");
                btn_forget.setText("忘记密码");
                break;
            case R.id.rb_message:
                tv_pass.setText("   验证码： ");
                ckb_remember.setVisibility(View.GONE);
                ed_password.setHint("请输入验证码");
                btn_forget.setText("获取验证码");
                break;
        }
    }

    private void loginSuccess() {
        String desc = String.format("手机号码为%s的用户登录成功", ed_phone.getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录成功");
        builder.setMessage(desc);
        builder.setPositiveButton("好的", (dialog, which) -> finish());
        builder.setNegativeButton("我再看看", null);
        AlertDialog dialog = builder.create();
        dialog.show();

        //保存到数据库
        LoginInfo info = new LoginInfo();
        info.phone = ed_phone.getText().toString();
        info.password = ed_password.getText().toString();
        info.remPass = ckb_remember.isChecked();
        mHelper.save(info);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == R.id.et_password && hasFocus) {
            LoginInfo info = mHelper.queryByPhone(ed_phone.getText().toString());
            if (info != null) {
                ed_password.setText(info.password);
                ckb_remember.setChecked(info.remPass);
            } else {
                ed_password.setText("");
                ckb_remember.setChecked(false);
            }
        }
    }
}