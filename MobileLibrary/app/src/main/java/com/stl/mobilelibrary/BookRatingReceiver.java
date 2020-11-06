package com.stl.mobilelibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Get the rating of the book
 */
public class BookRatingReceiver extends AsyncTask<String, Void, String> {
    private final String TAG = BookRatingReceiver.class.getSimpleName();
    private Rating rating;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String uuid;

    public BookRatingReceiver(Context context, Rating rating, String uuid) {
        setRating(rating);
        setContext(context);
        setUuid(uuid);
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    protected String doInBackground(String... strings) {
        return GoodReadsAPIHelper.getBookInfo(context,strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s == null) {
            Toast.makeText(getContext(),"Not a valid ISBN",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            JSONObject jsonObject = new JSONObject(s);
            Log.d(TAG, jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("books");
            Log.d(TAG, jsonArray.toString());
            double averageRating = jsonArray.getJSONObject(0).getDouble("average_rating");
            int ratingsCount = jsonArray.getJSONObject(0).getInt("ratings_count");
            getRating().setAverageRating(averageRating);
            getRating().setCount(ratingsCount);
            Log.d(TAG, "onPostExecute: "+rating);
            Log.d(TAG, "onPostExecute: "+getUuid()+" "+getRating());

            databaseHelper.updateRating(getUuid(),getRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
