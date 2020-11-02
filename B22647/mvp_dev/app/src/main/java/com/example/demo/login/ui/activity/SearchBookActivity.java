package com.example.demo.login.ui.activity;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.demo.login.R;
import com.example.demo.login.bean.Book;
import com.example.demo.login.bean.UserData;
import com.example.demo.login.utils.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class SearchBookActivity extends BaseActivity{


    ListView listView;

    SearchView mSearchView;
    private HashMap<String, Object> mHashMap;
    private SimpleAdapter mSimpleAdapter;
    private AlertDialog delloadDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_searchbook;
    }

    @Override
    protected void initEventAndData() {

        listView = (ListView) this.findViewById(R.id.listview);
        mSearchView = (SearchView) this.findViewById(R.id.searchView);
       // loadComment(null);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                loadComment(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void loadComment(String querry) {

        //从云端加载方式
        BmobQuery<Book> bmobQuery = new BmobQuery<Book>();

        if (querry != null) bmobQuery.addWhereEqualTo("mealname", querry);

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        bmobQuery.setLimit(100);
        //final int finalRandomImage = randomImage;
        bmobQuery.findObjects(new FindListener<Book>() {
            @Override
            public void done(List<Book> list, BmobException e) {
                if (e == null) {
                    // Toast.makeText(getActivity(), "加载数据中，请稍等", Toast.LENGTH_SHORT).show();
                    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
                    for (Book room : list) {
                        mHashMap = new HashMap<>();
                        Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception()) + room.getObjectId());
                        mHashMap.put("name", room.getMealname());
                        mHashMap.put("email", room.getDesc());
                        mHashMap.put("phone", room.getType());
                        mapList.add(mHashMap);
                    }

                    mSimpleAdapter = new SimpleAdapter(getBaseContext(), mapList, R.layout.checkuser_list_item,
                            new String[]{"name", "email", "phone"},
                            new int[]{R.id.tv_type, R.id.tv_title, R.id.tv_content});
                    listView.setAdapter(mSimpleAdapter);
                    mSimpleAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getBaseContext(), "search failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
