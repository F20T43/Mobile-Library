package com.stl.mobilelibrary;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Contain the state of a book.
 */
public class Book{
    final public static String TAG = "Book";

    private String uuid;
    private String ISBN;
    private BookDescription description;
    private String ownerUserName;
    private RequestHandler requests;
    private ArrayList<ViewableImage> images;
    private Rating rating;

    public Book() {
        this.uuid = UUID.randomUUID().toString();
    }

    /**
     * @param description: the description of the book
     * @param ISBN: the ISBN of the book
     * @param ownerUserName: the owner's username of the book
     * @param images: an array list of images of the book
     */
    public Book(BookDescription description, String ISBN, String ownerUserName, ArrayList<ViewableImage> images) {
        this(ISBN, description, ownerUserName, new RequestHandler(), images, new Rating());
    }

    /**
     * @param ISBN: the ISBN of the book
     * @param description: the description of the book
     * @param ownerUserName: the owner's username of the book
     * @param requests: the request handler of the book
     * @param images: an array list of images of the book
     * @param rating: the rating of the book
     */
    public Book(String ISBN, BookDescription description, String ownerUserName, RequestHandler requests, ArrayList<ViewableImage> images, Rating rating) {
        this.uuid = UUID.randomUUID().toString();
        this.ISBN = ISBN;
        this.description = description;
        this.ownerUserName = ownerUserName;
        this.requests = requests;
        this.images = images;
        this.rating = rating;
    }

    /**
     * @param userName: the owner's username
     * @return true if the user is interested, false otherwise.
     */
    public boolean userIsInterested(String userName){
        if (userName.equals("")){
            return false;
        }
        if (requests == null){
            return false;
        }
        if (requests.getRequestors() != null && requests.getRequestors().contains(userName)){
            return true;
        }
        if (requests.getAcceptedRequestor() != null && requests.getAcceptedRequestor().equals(userName)){
            return true;
        }
        return false;
    }

    /**
     * @return ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * @param ISBN: the book's ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return the book's Unique ID
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid: the book's Unique ID
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the book's request handler
     */
    public RequestHandler getRequests() {
        return requests;
    }

    /**
     * @param requests: the book's request handler
     */
    public void setRequests(RequestHandler requests) {
        this.requests = requests;
    }

    /**
     * @return the book's rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * @param rating: the book's rating
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     * @return an array list of images the book has
     */
    public ArrayList<ViewableImage> getImages() {
        if (images == null){
            return new ArrayList<>();
        }
        return images;
    }

    /**
     * @param images: an array list of images of the book.
     */
    public void setImages(ArrayList<ViewableImage> images) {
        this.images = images;
    }

    /**
     * @return the book's description
     */
    public BookDescription getDescription() {
        return description;
    }

    /**
     * @param description: the book's description
     */
    public void setDescription(BookDescription description) {
        this.description = description;
    }


    /**
     * @return the book's owner's username
     */
    public String getOwnerUserName() {
        return ownerUserName;
    }

    /**
     * @param ownerUserName:
     */
    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(getUuid(), book.getUuid()) &&
                Objects.equals(getISBN(), book.getISBN()) &&
                Objects.equals(getDescription(), book.getDescription()) &&
                Objects.equals(getOwnerUserName(), book.getOwnerUserName()) &&
                Objects.equals(getRequests(), book.getRequests()) &&
                Objects.equals(getImages(), book.getImages()) &&
                Objects.equals(getRating(), book.getRating());
    }

    /**
     * @return the book's hashcode
     */
    @SuppressLint("NewApi")
    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getISBN(), getDescription(), getOwnerUserName(), getRequests(), getImages(), getRating());
    }
}
