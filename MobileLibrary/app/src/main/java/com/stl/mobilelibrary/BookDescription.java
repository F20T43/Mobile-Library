package com.stl.mobilelibrary;

import android.annotation.SuppressLint;

import java.util.Objects;

/**
 * Show description of a book.
 */
public class BookDescription {
    private String title;
    private String synopsis;
    private String author;

    public BookDescription(){}

    /**
     * @param title: the book's title
     * @param synopsis: the book's synopsis
     * @param author: the book's author
     */
    public BookDescription(String title, String synopsis, String author) {
        this.title = title;
        this.synopsis = synopsis;
        this.author = author;
    }

    /**
     * gets the book's title
     * @return the book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the book's title
     * @param title: the book's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the book's synopsis
     * @return the book's synopsis
     */
    public String getSynopsis() {
        return synopsis;
    }

    /**
     * sets the book's synopsis
     * @param synopsis: the book's synopsis
     */
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * get the book's author
     * @return the book's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * sets the book's author
     * @param author: the book's author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    @SuppressLint("NewApi")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDescription)) return false;
        BookDescription that = (BookDescription) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getSynopsis(), that.getSynopsis()) &&
                Objects.equals(getAuthor(), that.getAuthor());
    }

    @SuppressLint("NewApi")
    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getSynopsis(), getAuthor());
    }
}
