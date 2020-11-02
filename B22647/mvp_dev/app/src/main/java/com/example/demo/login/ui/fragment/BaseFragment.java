package com.example.demo.login.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public abstract  class BaseFragment extends Fragment {
    public FragmentActivity mActivity;
    @Override
    public void onAttach(Context context) {

        //当前用户
        super.onAttach(context);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =initView();

        return view;
    }

    //view的初始化
    protected abstract View initView();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }
    /**
     * 初始化数据
     */
    public abstract void initData();
    /**
     * 初始化监听器
     */
    void initListener(){};

}
