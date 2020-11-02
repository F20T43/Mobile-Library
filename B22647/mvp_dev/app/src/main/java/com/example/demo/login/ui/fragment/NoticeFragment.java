package com.example.demo.login.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.demo.login.R;
import com.example.demo.login.bean.Notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class NoticeFragment extends Fragment {
	Context mContext;
	private List<Notice> bookList;
	ListView listView;
	private HashMap<String, Object> mHashMap;
	private SimpleAdapter mSimpleAdapter;
	SwipeRefreshLayout swipe;
	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_notice,container, false);

		mContext=getActivity();
		listView=view.findViewById(R.id.list_meal);
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

				load();
			}
		});
		load();
		return view;
	}
	// 自定义一个加载数据库中的全部记录到当前页面的无参方法
	public void load() {
		BmobQuery<Notice> bmobQuery = new BmobQuery<Notice>();


		//返回50条数据，如果不加上这条语句，默认返回10条数据
		bmobQuery.setLimit( 100 );
		bmobQuery.findObjects(new FindListener<Notice>() {
			@Override
			public void done(List<Notice> list, BmobException e) {
				if (e==null){
					// Toast.makeText(getActivity(), "加载数据中，请稍等", Toast.LENGTH_SHORT).show();
					List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
					for (Notice room : list) {
						mHashMap = new HashMap<>();
						mHashMap.put("type", room.getType());
						mHashMap.put("title", room.getMealname());
						mHashMap.put("content", room.getDesc());
						mapList.add(mHashMap);
					}

					mSimpleAdapter = new SimpleAdapter(getActivity(), mapList, R.layout.notice_list_item,
							new String[]{"type","title","content"},
							new int[]{ R.id.tv_type, R.id.tv_title, R.id.tv_content});
					listView.setAdapter(mSimpleAdapter);
					mSimpleAdapter.notifyDataSetChanged();
					//Toast.makeText(getActivity(), "加载成功"+ list.size(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "load failed:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

}
