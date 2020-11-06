package com.stl.mobilelibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The recycler adapter for books which display book list
 */
public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder>{
    final public static String BOOK_ID = "bookUUID";
    final private static String TAG = "RecyclerViewAdapter";
    private Context context;
    private ArrayList<Book> books;
    private Book mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public BookRecyclerAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public ArrayList<Book> getBooks() {
        return books;
    }


    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent,false);
        return new BookRecyclerAdapter.ViewHolder(view);
    }


    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String title = books.get(position).getDescription().getTitle();
        String author = books.get(position).getDescription().getAuthor();
        String status = books.get(position).getRequests().getState().getBookStatus().name();
        final String userName = books.get(position).getOwnerUserName();
        holder.title.setText(title);
        holder.author.setText(author);

        SpannableString userNameUnderLined = new SpannableString("@" + userName);
        userNameUnderLined.setSpan(new UnderlineSpan(), 0, userNameUnderLined.length(), 0);
        holder.userName.setText( userNameUnderLined);

        if (context.getClass() == MyBooksActivity.class && status.equals(BookStatus.BORROWED.name())){
            holder.status.setText(context.getString(R.string.lent).toUpperCase());
        }
        else{
            holder.status.setText(status);
        }
        if (status.equals(BookStatus.ACCEPTED.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.ACCEPTED));
        }
        else if (status.equals(BookStatus.AVAILABLE.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.AVAILABLE));
        }
        else if (status.equals(BookStatus.REQUESTED.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.REQUESTED));
        }
        else if (status.equals(BookStatus.BORROWED.name())){
            holder.status.setBackgroundTintList(context.getColorStateList(R.color.BORROWED));
        }


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked on book arrow");
                Intent intent = new Intent(context, ViewBookActivity.class);
                intent.putExtra(BOOK_ID, books.get(position).getUuid());
                context.startActivity(intent);
            }
        });

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_NAME,
                        userName);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout parentLayout;
        ImageView rightArrow;
        TextView title;
        TextView author;
        TextView status;
        ImageView bookArrow;
        TextView userName;

        @SuppressLint("CutPasteId")
        public ViewHolder(View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.book_list_parent_view);
            rightArrow = itemView.findViewById(R.id.BookListItemRightArrow);
            title = itemView.findViewById(R.id.BookListItemTitle);
            author = itemView.findViewById(R.id.BookListItemAuthor);
            status = itemView.findViewById(R.id.BookListItemStatus);
            bookArrow = itemView.findViewById(R.id.BookListItemRightArrow);
            userName = itemView.findViewById(R.id.ownerUserName);
        }
    }

    /**
     * Delete an item on a position from the array list
     */
    public void deleteItem(int position) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        mRecentlyDeletedItem = books.get(position);
        mRecentlyDeletedItemPosition = position;
        books.remove(position);
        notifyItemRemoved(position);
        databaseHelper.deleteBook(mRecentlyDeletedItem);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = null;
        if (context.getClass() == MyBooksActivity.class) {
            view = ((Activity) context).findViewById(R.id.ownerBooksRecyclerView);
        } else {
            return;
        }

        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDelete();
            }
        });
        snackbar.show();
    }

    private void undoDelete() {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        books.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        databaseHelper.addBookIfValid(mRecentlyDeletedItem, false);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}