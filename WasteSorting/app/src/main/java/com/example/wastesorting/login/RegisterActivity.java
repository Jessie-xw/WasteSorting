package com.example.wastesorting.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wastesorting.BaseActivity;
import com.example.wastesorting.R;
import com.example.wastesorting.user.User;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RegisterActivity extends BaseActivity {

    private EditText setPhoneNumber;

    private EditText setName;

    private EditText setPass;

    private EditText confirmPass;

    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setPhoneNumber = findViewById(R.id.set_phoneNumber);
        setName = findViewById(R.id.set_name);
        setPass = findViewById(R.id.set_pass);
        confirmPass = findViewById(R.id.confirm_pass);
        register = findViewById(R.id.signIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneContent = setPhoneNumber.getText().toString();
                String nameContent = setName.getText().toString();
                String passContent = setPass.getText().toString();
                String confirmContent = confirmPass.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Warning");
                builder.setCancelable(false);//不可取消

                int buttonType = 0;//0表示信息有误，1表示成功注册

                if(phoneContent.length() == 0 && passContent.length() == 0 && confirmContent.length() == 0){
                    builder.setMessage("你还未填写任何信息！请重新填写");
                }else if(passContent.length() == 0){
                    builder.setMessage("你还未设置密码！请重新填写");
                }else if(confirmContent.length() == 0){
                    builder.setMessage("你还未确认密码！请重新填写");
                }else if(phoneContent.length() == 0){
                    builder.setMessage("填写不正确！请重新填写");
                }else{
                    int phoneCorrect = 1;
                    int passCorrect = 0;
                    int passSame = 0;

                    if(phoneContent.length() == 11){
                        for(int i=0; i<phoneContent.length(); i++){
                            if(phoneContent.charAt(i) > '9' || phoneContent.charAt(i) < '0'){
                                phoneCorrect = 0;
                                break;
                            }
                        }
                    }else{
                        phoneCorrect = 0;
                    }

                    if(passContent.length() >= 6 && passContent.length() <= 8){
                        passCorrect = 1;
                    }
                    for(int i=0; i<passContent.length(); i++){
                        if(passContent.charAt(i) < '0' || passContent.charAt(i) > '9'){//不是数字要看是不是字母
                            if(passContent.charAt(i) >= 'a' && passContent.charAt(i) <= 'z'){
                                continue;
                            }else if(passContent.charAt(i) >= 'A' && passContent.charAt(i) <= 'Z'){
                                continue;
                            }else{
                                passCorrect = 0;
                                break;
                            }
                        }
                    }

                    if(passCorrect == 1 && passContent.equals(confirmContent)){
                        passSame = 1;
                    }

                    if(phoneCorrect == 1 && passCorrect == 1 && passSame == 1){
                        //先判断账号是否已经注册过
                        int canContinue = 1;//可继续标志
                        List<User> users = DataSupport.findAll(User.class);
                        for(User user : users){
                            if(user.getPhoneNumber().equals(phoneContent)){
                                canContinue = 0;
                                break;
                            }
                        }

                        if(canContinue == 0) {
                            builder.setMessage("此手机号已经注册过！请更换账号或者找回密码！");
                            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                        }else{
                            //添加数据
                            User user = new User();
                            user.setPhoneNumber(phoneContent);
                            user.setName(nameContent);
                            user.setPassword(passContent);
                            user.setRemember(false);
                            user.save();


                            //提示成功，转回登录页面
                            builder.setMessage("你的账号已注册成功！现在去登录吧！");
                            buttonType = 1;
                        }
                    }else{
                        if(phoneCorrect == 0){
                            builder.setMessage("手机号填写不正确！请重新填写");
                            setPass.setText("");
                            confirmPass.setText("");
                        }else if(passCorrect == 0){
                            builder.setMessage("密码填写不正确！请重新填写");
                            setPass.setText("");
                            confirmPass.setText("");
                        }else if(passSame == 0){
                            builder.setMessage("前后密码不一致！请重新填写");
                            setPass.setText("");
                            confirmPass.setText("");
                        }
                    }
                }

                if(buttonType == 0){
                    builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                }else{
                    builder.setPositiveButton("马上去登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("nullFlag", true);
                            intent.putExtra("account", phoneContent);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
                builder.show();
            }
        });
    }
}