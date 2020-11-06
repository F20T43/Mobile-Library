package com.stl.mobilelibrary;

import android.view.View;
import android.widget.ImageView;

import com.stl.mobilelibrary.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * The view holder for the list of book images
 */
public class BookImageViewHolder extends RecyclerView.ViewHolder {
    public ImageView bookImage;
    public CircleImageView deleteImageButton;

    public BookImageViewHolder(@NonNull View itemView) {
        super(itemView);
        bookImage = itemView.findViewById(R.id.bookImage);
        deleteImageButton = itemView.findViewById(R.id.deleteBookImageButton);
        deleteImageButton.setVisibility(View.GONE);
    }

}
