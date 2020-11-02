package com.example.demo.login.ui.activity;


import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.demo.login.R;
import com.example.demo.login.bean.Book;
import com.example.demo.login.bean.Notice;
import com.example.demo.login.utils.Debug;

import java.io.File;
import java.io.Serializable;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class OrderBookActivity extends BaseActivity implements OnClickListener{


    ImageView imageView;
    TextView ownerText;
    TextView issbn;

    TextView idText;
    TextView titleText;
    TextView authorText;
    TextView descText;
    Button btnAdd;
    String objectId;
    String type;
    String title;
    private static final int DATE_PICKER_ID = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_orderbook;
    }

    @Override
    protected void initEventAndData() {

        idText=(TextView)findViewById(R.id.tv_id);
        imageView=(ImageView) findViewById(R.id.image_view);
        titleText= (TextView) findViewById(R.id.tv_title);
        issbn =(TextView) findViewById(R.id.tv_issbn);
        authorText =(TextView)findViewById(R.id.tv_author);
        descText=(TextView) findViewById(R.id.tv_desc);
        ownerText =(TextView) findViewById(R.id.tv_owner);
        btnAdd=(Button) findViewById(R.id.AddBook) ;
        btnAdd.setOnClickListener(this);
        //获取当前套餐数据
        Intent intent = getIntent();
        objectId=intent.getStringExtra("objectId");
        type=intent.getStringExtra("type");
        if (type.equals("Available"))  btnAdd.setText("BORROW");
        else  if (type.equals("Requested"))  btnAdd.setText("ACCEPT");
        else  if (type.equals("Borrowed"))  btnAdd.setText("RETURN");
        Serializable serial = intent.getSerializableExtra("book");
        Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+serial);
            Book s = (Book) serial;
       // adminID=s.getAdmin_id();
            //tvTitle.setText("预订套餐");
            Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception()));
            showEditUI(s);

    }
    //显示预订套餐信息更新的UI
    private void showEditUI(Book book) {
        String issbnId=book.getId()+"";
        String userId=book.getUserid();
        String fromId=book.getFrom();
         title = book.getMealname();
        String type = book.getType();
        String author= book.getAuthor();
        String content = book.getDesc();
        // 还原数据

        titleText.setText(title);
        issbn.setText(issbnId);
        authorText.setText(author);
        ownerText.setText(userId);
        descText.setText(content);

        String path = Environment.getExternalStorageDirectory().getPath()+"/"+objectId+".PNG";
        Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+path);
        File file=new File(path);
        // 如果本地已有缓存，就从本地读取，否则从网络请求数据
        if (file.exists()) {
            Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception()) + "local ");
            // holder.iv.setImageDrawable( mImageCache.get( player.getIcon().getFileUrl() ) );
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            imageView.setImageBitmap(bitmap);
        }else {

            Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+"network");
            Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+path);
           // downloadImage( product.getImage(), path );


        }
    }

    /**
     * 监听点击事件
     *
     */

    @Override
    public void onClick(View v) {
        Book book = new Book();
        book.setFrom(currentUser.getObjectId());
        String status=null;
        if (type.equals("Available"))  status="Requested";
        else  if (type.equals("Requested"))   status="Borrowed";
        else  if (type.equals("Borrowed")) {
            status="Available";
            book.setFrom("none");
        }
        book.setType(status);
        final String notice_status=status;
        book.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Notice notice=new Notice();
                   notice.setType(notice_status);
                    notice.setMealname(title);
                    notice.setDesc("user:"+currentUser.getUsername()+" "+notice_status+"Book:"+title);
                    notice.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                finish();
                              //  Toast.makeText(getApplication(), "add successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplication(), "notice failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            // hideLoading();
                        }
                    });
                    Toast.makeText(getApplication(), "Requested successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "Requested failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
            finish();

    }



}