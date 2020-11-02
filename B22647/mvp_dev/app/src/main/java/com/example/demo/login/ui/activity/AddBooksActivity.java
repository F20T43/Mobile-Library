package com.example.demo.login.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.demo.login.R;
import com.example.demo.login.bean.Book;
import com.example.demo.login.bean.MyUser;
import com.example.demo.login.utils.Debug;
import com.example.demo.login.utils.HeadDialog;
import com.example.demo.login.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This activity is used to add a book. It is called from MyBooksActivity and calls ScannerActivity.
 */
public class AddBooksActivity extends AppCompatActivity implements View.OnClickListener {

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;//本地
    private static final int CODE_CAMERA_REQUEST = 0xa1;//拍照
    private static final int CODE_RESULT_REQUEST = 0xa2;//最终裁剪后的结果

    final public static String TAG = AddBooksActivity.class.getSimpleName();
    final public static int PICK_BOOK_IMAGE = 2;
    private ImageView headImage = null;
    private Uri imageUri;
    private String imagePath;
    private String objectID;
    private Context mContext;
    private TextView title;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookISBN;
    private EditText bookDesc;
    private RecyclerView bookImageRecyclerview;
    private boolean isAdd=true;
    private Button addBookImageButton;
    protected MyUser currentUser;
    private CircleImageView newProfileImage;
    private boolean isClickCamera;

    /**
     * OnCreate Bind UI elements, set up the image RecyclerViews and listeners
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbooks);
        //获取当前账户信息
        currentUser= MyUser.getCurrentUser(MyUser.class);
        mContext = getApplicationContext();
        // Bind UI elements
        title = (TextView) findViewById( R.id.tvTitle);
        bookTitle = findViewById(R.id.AddBookTitle);
        bookAuthor = findViewById(R.id.AddBookAuthor);
        bookISBN = findViewById(R.id.AddBookISBN);
        bookDesc = findViewById(R.id.AddBookDesc);
        addBookImageButton=findViewById(R.id.AddBook);
        addBookImageButton.setOnClickListener(this);
        newProfileImage=findViewById(R.id.newProfileImage);
        Intent intent=getIntent();
        Serializable serial = intent.getSerializableExtra("book");
        if (serial!=null) {
            isAdd=false;
            title.setText("Modify Book");
            addBookImageButton.setText("MODIFY");
            Book product =(Book)serial;
            objectID= product.getUserid();
            Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+objectID);
            bookISBN.setText(product.getId()+"");
            bookTitle.setText(product.getMealname());
            bookDesc.setText(product.getDesc());
            bookAuthor.setText(product.getAuthor());
        }
        newProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHeadDialog();
            }
        });
    }

    private void openHeadDialog() {
        final HeadDialog headDialog = new HeadDialog(this);
        headDialog.show();
        headDialog.setClicklistener(new HeadDialog.ClickListenerInterface() {
            @Override
            public void doGetCamera() {
                // 相机
                headDialog.dismiss();
                isClickCamera = true;
                // Toast.makeText(getApplicationContext(), "相机", Toast.LENGTH_SHORT).show();
                openCamera();
            }

            @Override
            public void doGetPic() {
                // 图库
                headDialog.dismiss();
                // Toast.makeText(getApplicationContext(), "tuku", Toast.LENGTH_SHORT).show();
                isClickCamera = false;
                if (ContextCompat.checkSelfPermission(AddBooksActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(AddBooksActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                } else {

                    selectFromAlbum();
                }
                // gallery(); // 只是打开图库选一张照片
            }

            @Override
            public void doCancel() {
                // 取消
                headDialog.dismiss();
            }
        });
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CODE_GALLERY_REQUEST);
    }

    /**
     * 打开系统相机
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开相机
        File oriPhotoFile = null;
        try {
            oriPhotoFile = createOriImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oriPhotoFile != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(oriPhotoFile);
            } else {
                imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", oriPhotoFile);
            }

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//私有目录读写权限
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            startActivityForResult(intent, CODE_CAMERA_REQUEST);
        }
    }

    /**
     * 裁剪图片
     *
     * @param uri 需要 裁剪图像的Uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        File cropPhotoFile = null;
        try {
            cropPhotoFile = createCropImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cropPhotoFile != null) {
            //7.0 安全机制下不允许保存裁剪后的图片
            //所以仅仅将File Uri传入MediaStore.EXTRA_OUTPUT来保存裁剪后的图像
            Uri imgUriCrop = Uri.fromFile(cropPhotoFile);

            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUriCrop);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, CODE_RESULT_REQUEST);
        }
    }

    /**
     * 创建原图像保存的文件
     *
     * @return
     * @throws IOException
     */
    private File createOriImageFile() throws IOException {
        String imgNameOri = "HomePic_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File pictureDirOri = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/OriPicture");
        if (!pictureDirOri.exists()) {
            pictureDirOri.mkdirs();
        }
        File image = File.createTempFile(
                imgNameOri,         /* prefix */
                ".jpg",             /* suffix */
                pictureDirOri       /* directory */
        );
        // imgPathOri = image.getAbsolutePath();
        return image;
    }

    /**
     * 创建裁剪图像保存的文件
     *
     * @return
     * @throws IOException
     */
    private File createCropImageFile() throws IOException {
        String imgNameCrop = "HomePic_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File pictureDirCrop = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/CropPicture");
        if (!pictureDirCrop.exists()) {
            pictureDirCrop.mkdirs();
        }
        File image = File.createTempFile(
                imgNameCrop,         /* prefix */
                ".jpg",             /* suffix */
                pictureDirCrop      /* directory */
        );
        // imgPathCrop = image.getAbsolutePath();
        return image;
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }

        cropPhoto(imageUri);
    }

    ////////////andoird 4.4之前
    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto(imageUri);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;

        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {//取消
            Toast.makeText(getApplication(), "Cancel", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST://如果是来自本地的
                //cropRawPhoto(intent.getData());//直接裁剪图片
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(intent);
                } else {
                    handleImageBeforeKitKat(intent);
                }
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    if (resultCode == RESULT_OK) {
                        cropPhoto(imageUri);
                    }
                } else {
                    Toast.makeText(this, "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
               /* if (intent != null) {
                    setImageToHeadView(intent);//设置图片框
                }*/
                Bitmap bitmap = null;
                try {
                    if (isClickCamera) {

                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);
                    }
                    displayImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }


    private void displayImage(Bitmap bitmap) {
        if (bitmap != null) {
            newProfileImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
    /**
     * On back pressed go to MyBooksActivity.
     */
    @Override
    public void onBackPressed() {

       finish();
    }

    @Override
    public void onClick(View v) {
        if (!checkUIInput()) {// 界面输入验证
            return;
        }
        if (isAdd)
            new DownTask().execute("AddBook");
        else
            new DownTask().execute("UpdateBook");
    }

    /*
     * 异步任务执行数据提交
     * */
    public class DownTask extends AsyncTask<String, Void, Bitmap> {
        //上面的方法中，第一个参数：网络图片的路径，第二个参数的包装类：进度的刻度，第三个参数：任务执行的返回结果
        @Override
        //在界面上显示进度条
        protected void onPreExecute() {
            //dialog.show() ;
            //showLoading();
        };
        protected Bitmap doInBackground(String... params) {  //三个点，代表可变参数
            Bitmap bitmap = null ;
            Book product = getBookFromUI();
            try {
                if (params[0]=="AddBook" && isAdd){
                    AddToServer(product);
                }else{
                    updateToServer(product);
                }
            }  catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bitmap;
        }

        //主要是更新UI
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            //imageView.setImageBitmap  (result) ;//更新UI
            // dialog.dismiss();
            //   hideLoading();
        }
    }

    private void AddToServer(final Book product) {
        //添加数据到云端服务器
        if (imagePath == null) {//如果没有图像，就不需要上传图片，用save
            product.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Toast.makeText(getApplication(), "add successfully", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplication(), "add failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // hideLoading();
                }
            });
        } else {
            final BmobFile icon = new BmobFile(new File(imagePath));
            icon.uploadblock(new UploadFileListener() {//
                @Override
                public void done(BmobException e) {
                    //hideLoading();
                    if (e == null) {
                        product.setImage(icon);
                        product.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getApplication(), "update successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplication(), "upload failed :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Log.e(TAG, "upload failed :" + e.getErrorCode());
                    }
                }
            });
        }
    }
    private void updateToServer(final Book product) {
        Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception()) + objectID);
        if (imagePath == null) {
            product.update(objectID, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Toast.makeText(getApplication(), "update successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "update failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            final BmobFile icon = new BmobFile(new File(imagePath));
            icon.uploadblock(new UploadFileListener() {//
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        product.setImage(icon);
                        product.update(objectID, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(getApplication(), "update successuflly", Toast.LENGTH_LONG).show();
                                    String path = Environment.getExternalStorageDirectory() + "/" + objectID + ".PNG";

                                    Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception()) + path);
                                    File file = new File(path);
                                    file.delete();//更新球员图片了，需要重新下载，删除本地缓存
                                } else {
                                    Toast.makeText(getApplication(), "creaet failed:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Log.e(TAG, "creaet failed:" + e.getErrorCode());
                    }
                }
            });
        }
    }

    //      收集界面输入的数据，并将封装成Meal对象
    private Book getBookFromUI() {
        String issnId=bookISBN.getText().toString();
        String name = bookTitle.getText().toString();
        // String phoneNumber = authorText.getText().toString();
        String desc = bookDesc.getText().toString();
        String author = bookAuthor.getText().toString();
        Book s = new Book(currentUser.getObjectId(), name,"Available", author,  desc, null);
        s.setId(Integer.valueOf(issnId));
        s.setFrom("none");
        return s;
    }

    private boolean checkUIInput() { // name, age, sex
        String ISSNID=bookISBN.getText().toString();
        String name = bookTitle.getText().toString();
        String desc = bookDesc.getText().toString();
        String from=bookAuthor.getText().toString();

        String message = null;
        View invadView = null;
        if (ISSNID.trim().length() == 0) {
            message = "ISSN code is empty！";
            invadView = bookISBN;
        }else if (name.trim().length() == 0) {
            message = "bookname is empty！";
            invadView = bookTitle;
        }else if (desc.trim().length() == 0) {
            message = "description is empty！";
            invadView = bookDesc;
        }else if (from.trim().length() == 0) {
            message = "author is empty！";
            invadView = bookAuthor;
        }
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if (invadView != null)
                invadView.requestFocus();
            return false;
        }
        return true;
    }

}
