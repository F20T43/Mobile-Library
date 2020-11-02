package com.example.demo.login.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.example.demo.login.R;
import com.example.demo.login.bean.Book;
import com.example.demo.login.utils.Debug;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;


public class MealListAdapter extends BaseAdapter {

        private List<Book> list;
        private ListView listview;
        private LruCache<String, BitmapDrawable> mImageCache;

        public MealListAdapter(List<Book> list) {
            super();
            this.list = list;


        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (listview == null) {
                listview = (ListView) parent;
            }
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.book_list_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.image_view);
                holder.item_tv_id = (TextView) convertView.findViewById(R.id.item_tv_id);
                holder.item_tv_userid = (TextView) convertView.findViewById(R.id.item_tv_userid);
                holder.item_tv_title = (TextView) convertView.findViewById(R.id.BookListItemTitle);
                holder.item_tv_type = (TextView) convertView.findViewById(R.id.BookListItemAuthor);
                holder.item_tv_desc = (TextView) convertView.findViewById(R.id.BookListItemStatus);
                holder.item_tv_author = (TextView) convertView.findViewById(R.id.ownerUserName);
                holder.item_tv_vin= (TextView) convertView.findViewById(R.id.tv_issbn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Book book = list.get(position);
            holder.item_tv_id.setText(book.getObjectId());
            holder.item_tv_userid.setText(book.getUserid());
            holder.item_tv_title.setText(book.getMealname());
            holder.item_tv_type.setText(book.getDesc()+"/"+book.getType()+"/current");
            holder.item_tv_author.setText(book.getAuthor());
            holder.item_tv_desc .setText(book.getDesc());
            holder.item_tv_vin.setText(book.getId()+"");
            Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+ book.getImage());

            if (book.getImage()!=null) {

                holder.iv.setTag( book.getImage().getFileUrl() );
                String path = Environment.getExternalStorageDirectory()+"/listviewImage/";
                //File image = new File(path);
                File fileDir;
                /**
                 * 文件目录如果不存在，则创建
                 */
                fileDir = new File(path);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }

                /**
                 * 创建图片文件
                 */
                File imageFile = new File(fileDir, book.getObjectId()+".PNG");
               String filePath = Environment.getExternalStorageDirectory().getPath()+"/"+ book.getObjectId()+".PNG";
                Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+filePath);
                File file=new File(filePath);
                // 如果本地已有缓存，就从本地读取，否则从网络请求数据

                if (file.exists()) {
                    Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+"local save");
                   // holder.iv.setImageDrawable( mImageCache.get( player.getIcon().getFileUrl() ) );
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
                    holder.iv.setImageBitmap(bitmap);

                } else {

                    Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+"from network");
                    Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+path);
                    downloadImage( book.getImage(), filePath );


                }
            } else {
                holder.iv.setImageResource(R.drawable.ic_library);
            }


            return convertView;
        }


    class ViewHolder {
            ImageView iv;
            TextView item_tv_id,item_tv_title,item_tv_type,item_tv_desc, item_tv_userid, item_tv_author,item_tv_date;
            TextView item_tv_vin;
        }


    /**
     * 根据url从网络上下载图片
     *
     * @return
     */
    private void downloadImage(final BmobFile bmobFile, final String path) {
            bmobFile.download(new File(path),new DownloadFileListener() {

                @Override
                public void done(String s, BmobException e) {
                    Log.e( Debug.filename( new Exception() ), Debug.funAndLine( new Exception() ) + "done() called with: " + "s = [" + s + "]" );
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    Bitmap bm = BitmapFactory.decodeFile(path, options);

                    ImageView iv = (ImageView) listview.findViewWithTag( bmobFile.getFileUrl() );
                    iv.setImageBitmap(bm);
                }
            @Override
            public void onProgress(Integer integer, long l) {

               // Log.e(Debug.filename(new Exception()), Debug.funAndLine(new Exception())+ "onProgress() called with: " + "integer = [" + integer + "], l = [" + l + "]");
            }
        });
    }

}
