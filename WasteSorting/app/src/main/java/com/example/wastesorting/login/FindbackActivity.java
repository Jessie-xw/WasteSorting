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

public class FindbackActivity extends BaseActivity {

    private EditText accountText;

    private EditText passwordText;

    private EditText confirmText;

    private Button yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findback);

        accountText = (EditText) findViewById(R.id.fB_account);
        passwordText = (EditText) findViewById(R.id.fB_pass);
        confirmText = (EditText) findViewById(R.id.fB_confirm);
        yes = (Button) findViewById(R.id.fB_yes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String accountContent = accountText.getText().toString();
                String passwordContent = passwordText.getText().toString();
                String confirmContent = confirmText.getText().toString();

                boolean canFind = false; //账号存在
                boolean passwordWrong = false; //标记密码格式错误
                boolean confirmWrong = false; //标记前后密码不一致

                if(passwordContent.length() < 6 || passwordContent.length() > 8){
                    passwordWrong = true;
                }
                if(!passwordWrong){
                    for(int i=0; i<passwordContent.length(); i++){
                        if(passwordContent.charAt(i) >= '0' && passwordContent.charAt(i) <= '9'){
                            continue;
                        }else if(passwordContent.charAt(i) >= 'A' && passwordContent.charAt(i) <= 'Z'){
                            continue;
                        }else if(passwordContent.charAt(i) >= 'a' && passwordContent.charAt(i) <= 'z'){
                            continue;
                        }else{
                            passwordWrong = false;
                            break;
                        }
                    }
                }
                if(!passwordWrong){
                    if(!passwordContent.equals(confirmContent)){
                        confirmWrong = true;
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(FindbackActivity.this);
                builder.setTitle("提示");
                builder.setCancelable(false);//不可取消
                List<User> users = DataSupport.findAll(User.class);
                for(User user : users) {
                    if(user.getPhoneNumber().equals(accountContent)) {//找到账号
                        canFind = true;
                        if(!passwordWrong && !confirmWrong){
                            user.setPassword(passwordContent);
                            user.save();
                            builder.setMessage("密码修改成功！");
                        }else{
                            if(passwordWrong){
                                builder.setMessage("密码格式不正确！请重新填写");
                            }else if(confirmWrong){
                                builder.setMessage("前后密码不一致！请重新填写");
                            }
                        }
                        break;
                    }
                }
                if(canFind == false){
                    builder.setMessage("该账号不存在！");
                }

                if(canFind && !passwordWrong && !confirmWrong){
                    builder.setPositiveButton("现在去登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(FindbackActivity.this, LoginActivity.class);
                            intent.putExtra("nullFlag", true);
                            intent.putExtra("account", accountContent);
                            startActivity(intent);
                        }
                    });
                }else{
                    builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                builder.show();

                if(passwordWrong){
                    passwordText.setText("");
                    confirmText.setText("");
                }else if(confirmWrong){
                    confirmText.setText("");
                }
            }
        });
    }
}