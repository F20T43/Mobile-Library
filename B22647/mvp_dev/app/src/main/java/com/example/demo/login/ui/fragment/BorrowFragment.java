package com.example.demo.login.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.login.R;
import com.example.demo.login.bean.Book;
import com.example.demo.login.bean.MyUser;
import com.example.demo.login.ui.activity.OrderBookActivity;
import com.example.demo.login.ui.activity.SearchBookActivity;
import com.example.demo.login.ui.adapter.MealListAdapter;
import com.example.demo.login.utils.Debug;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class BorrowFragment extends Fragment implements AdapterView.OnItemClickListener {
	Context mContext;
	protected MyUser currentUser;
	SwipeRefreshLayout swipe;
	ListView listView;
	Button addBookButton;
	public static final int ADD = 1;
	Spinner sp_stu = null;
	private String str_stu;
	private List<String> data_list;
	private ArrayAdapter<String> adapter_stu;
	private RecyclerView recyclerView;
	private ArrayList<Book> books = new ArrayList<>();
	private ArrayList<Book> filteredBooks = new ArrayList<>();
	//private BookRecyclerAdapter adapter;
	private List<Book> bookList;
	private String[] lineArray = {"State", "Available",  "Requested","Borrowed"};

	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_borrow, container, false);
		currentUser= MyUser.getCurrentUser(MyUser.class);
		mContext = getActivity();
		addBookButton = view.findViewById(R.id.addBookButton);
		mContext = getActivity();
		sp_stu = (Spinner) view.findViewById(R.id.spinner_stu);
		//adapter = new BookRecyclerAdapter(mContext, filteredBooks);

		listView=view.findViewById(R.id.list_meal);
		listView.setOnItemClickListener(this);
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

				load("Available");
			}
		});
		load("Available");
		addBookButton.setOnClickListener(new View.OnClickListener() {
			/**
			 * When the user selects this button they will be taken to the add books screen
			 * @param view: the current view
			 */
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, SearchBookActivity.class);
				startActivityForResult(intent, ADD);
			}
		});

		initSpinner();
		return view;
	}

	// 自定义一个加载数据库中的全部记录到当前页面的无参方法
	public void load(String type) {
		BmobQuery<Book> bmobQuery = new BmobQuery<Book>();
		if(type.equals("Available")){
			bmobQuery.addWhereEqualTo("type",type);
			bmobQuery.addWhereNotEqualTo("userid", currentUser.getObjectId());
		}else if (type.equals("Requested")) {
			bmobQuery.addWhereEqualTo("userid", currentUser.getObjectId());
			bmobQuery.addWhereEqualTo("type",type);
		}else if (type.equals("Borrowed")) {
			bmobQuery.addWhereEqualTo("from", type);
		}

		//返回50条数据，如果不加上这条语句，默认返回10条数据
		bmobQuery.setLimit( 100 );
		bmobQuery.findObjects(new FindListener<Book>() {
			@Override
			public void done(List<Book> list, BmobException e) {
				if (e==null){
					bookList =list;
					mHandler.obtainMessage(0).sendToTarget();
					// setSharedPreference(objectList);

					//Toast.makeText(getActivity(), "加载成功"+ list.size(), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "load failed:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					MealListAdapter adapter = new MealListAdapter(bookList);
					listView.setAdapter(adapter);
					break;

				default:
					break;
			}
		}

	};

	private void initSpinner() {

		//数据
		data_list = new ArrayList<String>();
		for (int i = 0; i < lineArray.length; i++)
			data_list.add(lineArray[i]);
		//适配器
		adapter_stu = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, data_list);
		//设置样式
		adapter_stu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//加载适配器
		sp_stu.setAdapter(adapter_stu);
		/**选项选择监听*/
		sp_stu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				str_stu = adapter_stu.getItem(position);
				if(position<=1) load("Available");
				else load(str_stu);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception()));

		gotoModifyMeal(view);
	}

	private void gotoModifyMeal(View view) {
		TextView tv_note_id = (TextView) view.findViewById(R.id.item_tv_id);
		String item_id = tv_note_id.getText().toString();
		TextView tv_note_userid = (TextView) view.findViewById(R.id.item_tv_userid);
		String item_userid = tv_note_userid.getText().toString();
		TextView tv_note_title = (TextView) view.findViewById(R.id.BookListItemTitle);
		String item_title = tv_note_title.getText().toString();
		TextView tv_note_desc = (TextView) view.findViewById(R.id.BookListItemStatus);
		String item_desc = tv_note_desc.getText().toString();
		TextView tv_note_author = (TextView) view.findViewById(R.id.ownerUserName);
		String item_author = tv_note_author.getText().toString();
		TextView tv_note_issbn = (TextView) view.findViewById(R.id.tv_issbn);
		String item_issbn = tv_note_issbn.getText().toString();
		Intent intent=null;
		if(str_stu.equals("Available")){
		intent= new Intent(getActivity(), OrderBookActivity.class);
			Book book =new Book(item_userid, item_title, null, item_author,item_desc,null);
			book.setId(Integer.valueOf(item_issbn));
			intent.putExtra("type",str_stu);
			intent.putExtra("book", book);
			intent.putExtra("objectId",item_id);
			this.startActivity(intent);
		}else if (str_stu.equals("Requested")) {
			intent = new Intent(getActivity(), OrderBookActivity.class);
			Book book = new Book(item_id, item_title, null, item_author, item_desc, null);
			book.setId(Integer.valueOf(item_issbn));
			intent.putExtra("type",str_stu);
			intent.putExtra("book", book);
			intent.putExtra("objectId",item_id);
			this.startActivity(intent);
		}else if (str_stu.equals("Borrowed")) {
			intent = new Intent(getActivity(), OrderBookActivity.class);
			Book book = new Book(item_id, item_title, null, item_author, item_desc, null);
			book.setId(Integer.valueOf(item_issbn));
			intent.putExtra("type",str_stu);
			intent.putExtra("book", book);
			intent.putExtra("objectId",item_id);
			this.startActivity(intent);
		}
	}
}
