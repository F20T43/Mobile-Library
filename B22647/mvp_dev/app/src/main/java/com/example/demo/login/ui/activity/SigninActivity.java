package com.example.demo.login.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.demo.login.R;
import com.example.demo.login.bean.MyUser;
import com.example.demo.login.bean.UserData;
import com.example.demo.login.bean.UserDataManager;
import com.example.demo.login.utils.LogUtils;
import com.example.demo.login.utils.ProgressUtils;
import com.example.demo.login.utils.RegexUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    private static final int REQUEST_SIGNUP = 0;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;
    private String oldtype;
    EditText inputUsername;
    EditText inputPassword;
    Button btnLogin;
    Button linkSignup;
    boolean exitUser=false;
    private UserData currentUser;

    private UserDataManager mUserDataManager;         //用户数据管理类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mContext=this;
        //判断用户是否已经授权给我们了 如果没有，调用下面方法向用户申请授权，之后系统就会弹出一个权限申请的对话框
        /**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {

            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            //验证是否许可权限
            for (String str : permissions) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            SigninActivity.this, permissions, 101);
                } else {

                }
            }
        }
        initViews();
        initData();
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }
    private void initViews() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        inputUsername = (EditText) findViewById(R.id.input_username);
        inputPassword = (EditText) findViewById(R.id.input_password);
        linkSignup=(Button) findViewById(R.id.link_signup);
        btnLogin.setOnClickListener(this);
        inputUsername.setOnClickListener(this);
        inputPassword.setOnClickListener(this);
        linkSignup.setOnClickListener(this);

        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                String idcard = editable.toString();
                BmobQuery<UserData> query = new BmobQuery<UserData>();
                query.addWhereEqualTo("userName", idcard);
                query.setLimit(1);
                query.findObjects(new FindListener<UserData>() {
                    @Override
                    public void done(List<UserData> list, BmobException e) {
                        if (e == null) {
                            Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+list.size());

                            if (list.size()==1) {
                                exitUser = true;
                                currentUser=list.get(0);
                                RegexUtils.objectId=currentUser.getObjectId();
                                RegexUtils.username=currentUser.getUserName();
                                RegexUtils.userpwd=currentUser.getUserPwd();
                                RegexUtils.usertype=currentUser.getUserType();
                                RegexUtils.useremail=currentUser.getUserEmail();
                                Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+  RegexUtils.username);

                            }
                            else {
                                exitUser = false;
                            }
                        }
                    }
                });
            }
        });

    }
void initData(){
    /**
     * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
     */
    if (Build.VERSION.SDK_INT >= 23) {
        int REQUEST_CODE_CONTACT = 101;
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_LOGS,
        };
        //验证是否许可权限
        for (String str : permissions) {
            if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                return;
            }
        }
    }


}
    /**
     * 登录方法
     */
    public void login() {
        //如果内容不合法，则直接返回，显示错误。
        if (!validate()) {
            Toast.makeText(getApplicationContext(),"input is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        //显示圆形进度条对话框
        final ProgressDialog progressDialog = new ProgressDialog(SigninActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Login...");
        progressDialog.show();
        //获取输入内容
        final String username = inputUsername.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        //  SPUtils.put(LoginActivity.this, SPkey.UserName, username);

        int result=mUserDataManager.findUserByNameAndPwd(username,password);

        if (result==1) {
            RegexUtils.username = username;
            RegexUtils.userpwd = password;
        }
        //onLoginSuccessful(result);
        MyUser myUser=new MyUser();
         MyUser.loginByAccount(username, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                //Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + myUser.getQuest());
                ProgressUtils.dismiss();
                progressDialog.dismiss();
                if (e== null) {
                    RegexUtils.username = username;
                    RegexUtils.userpwd = password;
                    RegexUtils.usertype=myUser.getClassfy();
                    RegexUtils.useremail=myUser.getEmail();
                    Toast.makeText(getBaseContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                    onLoginSuccess();
                } else {
                    onLoginFailed(e);
                    // Toast.makeText(getBaseContext(), "登录失败|"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + myUser.getQuest());
            }
        });
        ProgressUtils.dismiss();
        if (RegexUtils.userpwd.equals(password)) {

            Intent intent = new Intent();
            RegexUtils.sUser =currentUser;

              //  intent.setClass(SigninActivity.this, HomeActivity.class);
            //         intent.putExtra("name", name);
          //  startActivity(intent);
           // finish();
        }else{
            Toast.makeText(getBaseContext(), "account or password is wrong！", Toast.LENGTH_SHORT).show();
        }
    }
    /*
   * 登录成功，按钮置为可用
     * 依据传参不同，进行不同吐司
     */
    public void onLoginSuccessful(int result) {
        if(result==1){

            Toast.makeText(getApplicationContext(),"Login Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }else if(result==0){
           Toast.makeText(getApplicationContext(),"account or password is wrong！", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, RegisterActivity.class));
        }
    }
    /**
     * 登录成功
     */
    public void onLoginSuccess() {
      //  btnLogin.setEnabled(true);
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 登录失败
     */
    public void onLoginFailed(BmobException e) {
        Toast.makeText(getBaseContext(), "Login failed:"+e.getMessage(), Toast.LENGTH_SHORT).show();
     //   btnLogin.setEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    /**
     * 重写返回键的返回方法
     */
    @Override
    public void onBackPressed() {
        // Disable going back to the TrainModeActivity
        moveTaskToBack(true);
    }



    /**
     * @return 判断是否账号密码是否合法
     */
    public boolean validate() {
        //设置初值，默认为合法
        boolean valid = true;

        //获取输入内容
        String email = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        //判断账号
        if (email.isEmpty()) {
            inputUsername.setError("please input your username");
            valid = false;
        } else {
            inputUsername.setError(null);
        }
        //判断密码
        if (password.isEmpty()) {
            inputPassword.setError("please input your password");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            //如果点击了注册链接，则跳转到注册页面
            case R.id.link_signup:
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                break;
        }
    }
}
