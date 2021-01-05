package com.example.wastesorting.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wastesorting.BaseActivity;
import com.example.wastesorting.MainActivity;
import com.example.wastesorting.R;
import com.example.wastesorting.user.User;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends BaseActivity {

    private EditText accountText;
    private EditText passwordText;
    private Button login;
    private TextView register;
    private TextView forgotPass;
    private CheckBox rememberPass;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //获取实例
        accountText = (EditText) findViewById(R.id.account_text);
        passwordText = (EditText) findViewById(R.id.password_text);
        register = (TextView) findViewById(R.id.register);
        forgotPass = (TextView) findViewById(R.id.forgot_pass);
        login = (Button) findViewById(R.id.login);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);

        //自动填补账号密码
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = pref.getBoolean("remember_information", false);
        if(isRemember) {
            //将账号密码自动设置到文本框
            String accountContent = pref.getString("account", "");
            String passwordContent = pref.getString("password", "");
            accountText.setText(accountContent);
            passwordText.setText(passwordContent);
        }

        //create DB
        LitePal.getDatabase();

        //跳转注册页面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //跳转找回密码页面
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindbackActivity.class);
                startActivity(intent);
            }
        });
        //执行自动填补密码,动态监测
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String accountContent = accountText.getText().toString();
                //若账号存在，看是否需要填补密码
                List<User> users = DataSupport.findAll(User.class);
                for(User user : users){
                    if(user.getPhoneNumber().equals(accountContent)){
                        if(user.isRemember()){
                            String passWordContent = user.getPassword();
                            passwordText.setText(passWordContent);
                        }
                        break;
                    }
                }
            }
        };
        accountText.addTextChangedListener(watcher);
        //登录按钮逻辑
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Warning");
                int flag = 0;
                String accountContent = accountText.getText().toString();
                String passwordContent = passwordText.getText().toString();
                List<User> users = DataSupport.findAll(User.class);
                for(User user : users) {
                    if(user.getPhoneNumber().equals(accountContent)) {
                        if(user.getPassword().equals(passwordContent)){//密码正确
                            //记住密码按钮
                            rememberPass = (CheckBox) findViewById(R.id.remember_pass);
                            editor = pref.edit();
                            if(rememberPass.isChecked()){
                                //修改该账号的isRemember为true
                                user.setRemember(true);
                                user.updateAll("phoneNumber=?", accountContent);

                                editor.putBoolean("remember_information", true);
                                editor.putString("account", accountContent);
                                editor.putString("password", passwordContent);
                            }else{
                                editor.clear();
                            }
                            editor.apply();
                            flag = 2;

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", user.getName());
                            startActivity(intent);
                            finish();
                        }else{//密码错误
                            builder.setMessage("密码错误！请重新输入" + '\n' + "(忘记密码？点击下方找回密码)");
                            flag = 1;
                            break;
                        }
                    }
                }
                //找不到账号
                if(flag == 0){
                    builder.setMessage("该账号未注册！点击下方按钮注册吧！");
                }
                if(flag != 2){
                    builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            }
        });
    }
}