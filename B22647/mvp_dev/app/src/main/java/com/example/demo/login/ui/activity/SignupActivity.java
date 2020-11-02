package com.example.demo.login.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.login.R;
import com.example.demo.login.bean.MyUser;
import com.example.demo.login.bean.UserData;
import com.example.demo.login.bean.UserDataManager;
import com.example.demo.login.utils.LogUtils;
import com.example.demo.login.utils.RegexUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    //@BindView(R.id.input_name)
    EditText _nameText;
    //@BindView(R.id.input_password)
    EditText _passwordText;
   // @BindView(R.id.input_reEnterPassword)
    EditText _phoneText;
    EditText _emailText;

    private RadioGroup group;
    Button _signupButton;
    //TextView _linkSignin;
    private UserDataManager mUserDataManager;         //用户数据管理类
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }
    private void initViews() {

        _signupButton = (Button) findViewById(R.id.btn_signup);
        _nameText = (EditText) findViewById(R.id.input_name);
        _phoneText = (EditText) findViewById(R.id.SignUpPhoneNumber);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _emailText = (EditText) findViewById(R.id.input_email);

       // _linkSignin=(TextView) findViewById(R.id.link_login);
        _signupButton.setOnClickListener(this);
        _nameText.setOnClickListener(this);
        _passwordText.setOnClickListener(this);
        _phoneText.setOnClickListener(this);
      //  _linkSignin.setOnClickListener(this);
    }
    public void signup() {
        //判断是否合法
        if (!validate()) {
            onSignupFailed(0);
            return;
        }
        _signupButton.setEnabled(false);

        //显示圆形进度条对话框
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Create New Account...");
        progressDialog.show();
        //获取数据
        String username = _nameText.getText().toString();
        String password = _passwordText.getText().toString();
      //  String selectClassfy = ((RadioButton) findViewById(group
      //          .getCheckedRadioButtonId())).getText().toString();
        String email=_emailText.getText().toString();
        String phone=_phoneText.getText().toString();
        UserData mUser = new UserData(username,password,phone);
        mUser.setUserEmail(email);

        UserData myUser =new UserData();
        myUser.setUsername(username);
        myUser.setPassword(password);
        myUser.setUserEmail(email);
        myUser.setUserPhone(phone);
        Log.v(LogUtils.filename(new Exception()),LogUtils.funAndLine(new Exception()));
        myUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                progressDialog.dismiss();
                if (e != null) {
                    Toast.makeText(getApplicationContext(),"Create Failed:"+e.getMessage(), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(),"Create Successfully,Please Login", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, SigninActivity.class));
                    finish();
                }
            }
        });



     mUserDataManager.openDataBase();
        long flag = mUserDataManager.insertUserData(mUser); //注册用户信息
       // progressDialog.dismiss();
       /* if (flag == -1) {
            Toast.makeText(getApplicationContext(),"Create Failed", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(),"Create Successfully,Please Login", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        }*/

    }


    /**
     * 注册失败，按钮置为可用
     * 依据传参不同，进行不同吐司
     */
    public void onSignupFailed(int i) {
        if (i == 1) {
            Toast.makeText(getBaseContext(), "Account already Exist", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getBaseContext(), "Create Failed", Toast.LENGTH_LONG).show();
        }
        _signupButton.setEnabled(true);
    }

    /**
     * @return 输入内容是否合法
     */
    public boolean validate() {
        boolean valid = true;
//      从控件中获取数据
        String name = _nameText.getText().toString();
        String password = _passwordText.getText().toString();
        //检测账号是否正确
        if (name.isEmpty()) {
            _nameText.setError("UserId is empty");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        //检测密码是否正确
        if (password.isEmpty()) {
            _passwordText.setError("please input password");
            valid = false;
        }else{
            _passwordText.setError(null);
        }
        return valid;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                signup();
                break;
        }
    }
}