package info.meizi_retrofit.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import butterknife.Bind;
import butterknife.OnClick;
import info.meizi_retrofit.R;
import info.meizi_retrofit.ui.base.ToolBarActivity;

/**
 * Created by Mr_Wrong on 16/1/24.
 */
public class LoginActivity extends ToolBarActivity {
    @Bind(R.id.etv_user_name)
    EditText mName;
    @Bind(R.id.etv_password)
    EditText nPassWord;
    String name;
    String password;
    AlertDialog dialog;

    @OnClick(R.id.btn_login)
    void _login() {
        showDialog();
        name = mName.getText().toString();
        password = nPassWord.getText().toString();
        AVUser user = new AVUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setMobilePhoneNumber(name);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                login();
//                if (e.getCode() == 214) {//已经注册了,去登录
//                    login();
//                } else {
//                    login();
//                }
            }
        });
    }

    private void login() {
        AVUser.loginByMobilePhoneNumberInBackground(name, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    setResult(666);
                    finish();
                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        ProgressBar progressBar = new ProgressBar(LoginActivity.this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 30);
        progressBar.setLayoutParams(params);
        builder.setView(progressBar);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("登录");
    }

    @Override
    protected int layoutId() {
        return R.layout.login;
    }

}
