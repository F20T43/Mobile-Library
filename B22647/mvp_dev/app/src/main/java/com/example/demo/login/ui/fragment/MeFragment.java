package com.example.demo.login.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo.login.R;
import com.example.demo.login.bean.UserData;
import com.example.demo.login.ui.activity.SearchUserActivity;
import com.example.demo.login.ui.activity.EditUserActivity;
import com.example.demo.login.utils.Debug;


public class MeFragment extends Fragment {
	Button btnChat;
	Button btnSearch;
	//EditText etID;
	UserData selectUser;
	TextView tvUserName;
	TextView tvUserEmail;
	TextView tvUserPhone;
	protected UserData currentUser;
	SwipeRefreshLayout swipe;
	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_me,container, false);

		//获取当前账户信息
		currentUser=UserData.getCurrentUser(UserData.class);
		tvUserName=view.findViewById(R.id.tv_username);
		tvUserEmail=view.findViewById(R.id.tv_useremail);
		tvUserPhone=view.findViewById(R.id.tv_userphone);
		swipe=view.findViewById(R.id.swipe);
		//改变加载显示的颜色
		swipe.setColorSchemeColors(getResources().getColor(R.color.text_red), getResources().getColor(R.color.text_red));
		//设置向下拉多少出现刷新
		swipe.setDistanceToTriggerSync(200);
		//设置刷新出现的位置
		swipe.setProgressViewEndTarget(false, 200);
		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipe.setRefreshing(false);

				loadMsg();
			}
		});
		btnChat=view.findViewById(R.id.btn_start);
		btnSearch=view.findViewById(R.id.btn_search);
		btnChat.setOnClickListener(new View.OnClickListener() {
			/**
			 * When the user selects this button they will be taken to the add books screen
			 * @param view: the current view
			 */
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), EditUserActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception()) );
				Intent intent=new Intent(getActivity(), SearchUserActivity.class);
				startActivity(intent);
			}
		});
		loadMsg();

		return view;
	}

	private void loadMsg() {
		tvUserName.setText(currentUser.getUsername());
		tvUserEmail.setText(currentUser.getUserEmail());
		tvUserPhone.setText(currentUser.getUserPhone());
	}
@Override
public void onResume(){
		super.onResume();
		loadMsg();
}
	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
		if (requestCode == 1 && resultCode == 3) {
			loadMsg();
		}
	}
}
