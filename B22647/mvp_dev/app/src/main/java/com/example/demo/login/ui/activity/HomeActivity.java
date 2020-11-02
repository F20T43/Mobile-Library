package com.example.demo.login.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.login.R;
import com.example.demo.login.ui.adapter.FragmentAdapter;
import com.example.demo.login.ui.fragment.BorrowFragment;
import com.example.demo.login.ui.fragment.NoticeFragment;
import com.example.demo.login.ui.fragment.HomeFragment;
import com.example.demo.login.ui.fragment.MeFragment;

import java.util.ArrayList;


public class HomeActivity extends FragmentActivity implements OnClickListener,
        ViewPager.OnPageChangeListener {

    private static HomeActivity activity;
    private ArrayList<TextView> tv_menus;
    private ArrayList<ImageView> imgv_menus;
    private ViewPager mViewPager;
    private final static String TAB = "USER";

    private ArrayList<Fragment> mFragments;
    private FragmentAdapter mMainMenuAdapter;

    public static HomeActivity getActivity() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取当前账户信息
        activity = this;
        // 去除标题栏
        // 去除标题栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_home);

        initView();
        initData();
        initEvent();

    }


    // 初始化控件
    private void initView() {
        tv_menus = new ArrayList<TextView>();
        TextView _tv_bottomMenu_chat=(TextView) findViewById(R.id.tv_bottomMenu_chat);
        TextView _tv_bottomMenu_addressbook=(TextView) findViewById(R.id.tv_bottomMenu_addressbook);
        TextView _tv_bottomMenu_discovery=(TextView) findViewById(R.id.tv_bottomMenu_discovery);
        TextView _tv_bottomMenu_me=(TextView) findViewById(R.id.tv_bottomMenu_me);

        tv_menus.add(_tv_bottomMenu_chat);
        tv_menus.add(_tv_bottomMenu_addressbook);
        tv_menus.add(_tv_bottomMenu_discovery);
        tv_menus.add(_tv_bottomMenu_me);

        imgv_menus = new ArrayList<ImageView>();
        imgv_menus.add((ImageView) findViewById(R.id.imgv_bottomMenu_home));
        imgv_menus.add((ImageView) findViewById(R.id.imgv_bottomMenu_addressbook));
        imgv_menus.add((ImageView) findViewById(R.id.imgv_bottomMenu_discovery));
        imgv_menus.add((ImageView) findViewById(R.id.imgv_bottomMenu_me));
        mViewPager = (ViewPager) findViewById(R.id.vp_main_menuContent);
    }

    // 初始化数据
    private void initData() {
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new HomeFragment());
        mFragments.add(new BorrowFragment());
        mFragments.add(new NoticeFragment());
        mFragments.add(new MeFragment());
        mMainMenuAdapter = new FragmentAdapter(getSupportFragmentManager(),
                mFragments);
        setMenuSelector(0); // 默认选中第一个菜单项
    }

    // 初始化事件
    private void initEvent() {
        mViewPager.setAdapter(mMainMenuAdapter);
        mViewPager.setOnPageChangeListener(this);
        findViewById(R.id.ll_bottomMenu_chat).setOnClickListener(this);
        findViewById(R.id.ll_bottomMenu_addressBook).setOnClickListener(this);
        findViewById(R.id.ll_bottomMenu_discovery).setOnClickListener(this);
        findViewById(R.id.ll_bottomMenu_me).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bottomMenu_chat:

                setMenuSelector(0);
                break;
            case R.id.ll_bottomMenu_addressBook:
                setMenuSelector(1);
                break;
            case R.id.ll_bottomMenu_discovery:
                setMenuSelector(2);
                break;
            case R.id.ll_bottomMenu_me:
                setMenuSelector(3);
                break;

        }
    }

    /**
     * 选中指定的菜单项并显示对应的Fragment
     *
     * @param index
     */
    private void setMenuSelector(int index) {
        reSetSelected();
        tv_menus.get(index).setSelected(true);
        imgv_menus.get(index).setSelected(true);
        mViewPager.setCurrentItem(index);
    }

    /**
     * 重置底部菜单所有ImageView和TextView为未选中状态
     */
    private void reSetSelected() {
        for (int i = 0; i < tv_menus.size(); i++) {
            tv_menus.get(i).setSelected(false);
            imgv_menus.get(i).setSelected(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setMenuSelector(arg0);
    }

}
