package com.example.demo.login.ui.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.login.R;
import com.example.demo.login.bean.UserData;
import com.example.demo.login.utils.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


public class EditUserActivity extends BaseActivity{
    Button btnChat;
    Button btnSearch;
    //EditText etID;
    UserData selectUser;
    TextView tvUserName;
    TextView tvUserEmail;
    TextView tvUserPhone;
    protected UserData currentUser;

    @Override
    protected int getLayout() {
        return R.layout.activity_edituser;
    }

    @Override
    protected void initEventAndData() {

        //获取当前账户信息
        currentUser=UserData.getCurrentUser(UserData.class);
        tvUserName=findViewById(R.id.tv_username);
        tvUserEmail=findViewById(R.id.tv_useremail);
        tvUserPhone=findViewById(R.id.tv_userphone);
        tvUserName.setText(currentUser.getUsername());
        tvUserEmail.setText(currentUser.getUserEmail());
        tvUserPhone.setText(currentUser.getUserPhone());
        btnChat=findViewById(R.id.btn_start);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.setUserEmail(tvUserEmail.getText().toString());
                currentUser.setUserPhone(tvUserPhone.getText().toString());
                currentUser.update(currentUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null) {
                            // Toast.makeText(mContext, "修改成功!", Toast.LENGTH_SHORT).show();
                            setResult(3);
                            finish();
                        }
                        else
                            Toast.makeText(mContext, "Edit failed!"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}
