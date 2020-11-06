package com.stl.mobilelibrary;

import java.util.regex.Pattern;

/**
 * Determines if the user registration info is correct format
 */
public class SignUpValidator implements Validatable {
    private String username;
    private String password;
    private String emailAddress;
    private String phoneNumber;
    private String errorMessage;


    public SignUpValidator() {
        this(null,null,null,null);
    }

    /**
     * Full constructor
     * @param username: the user's username
     * @param password: the user's password
     * @param emailAddress: the user's email address
     * @param phoneNumber: the user's phone number
     */
    public SignUpValidator(String username, String password, String emailAddress, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.errorMessage = "No errors detected";
    }


    /**
     * Gets the user's username
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username
     * @param username: the user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's password
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * @param password: the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's email address
     * @return the user's email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the user's email address
     * @param emailAddress: the user's email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the user's phone number
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number
     * @param phoneNumber: the user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the error mesage
     * @param errorMessage: the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Determines if the user name is valid
     * @return true if valid, false otherwise
     */
    public boolean isUserNameValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]+").matcher(username).matches();

        if (!valid){
            errorMessage = "Invalid username format";
        }

        return valid;
    }

    /**
     * Determines if the password is valid
     * @return true if valid, false otherwise
     */
    public boolean isPasswordValid(){
        Boolean valid = Pattern.compile("[0-9a-zA-Z_]{6,}").matcher(password).matches();

        if (!valid){
            errorMessage = "Invalid password format";
        }

        return valid;
    }

    /**
     * Determines if the email is valid
     * @return true if valid, false otherwise
     */
    public boolean isEmailNameValid(){
        Boolean valid = Pattern.compile("[a-zA-Z_0-9.]+@[a-zA-Z]+\\.[a-zA-Z]+").matcher(emailAddress).matches();

        if (!valid){
            errorMessage = "Invalid email address format";
        }

        return valid;
    }

    /**
     * Determines if the phone number is valid
     * @return true if valid, false otherwise
     */
    public boolean isPhoneNumberValid(){
        Boolean valid = Pattern.compile("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}").matcher(phoneNumber).matches();

        if (!valid){
            errorMessage = "Invalid phone number format";
        }

        return valid;
    }


    /**
     * Determines if a user's registration details are of proper format
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return isUserNameValid() && isPasswordValid() && isEmailNameValid() && isPhoneNumberValid();
    }

    /**
     * Gets the error message
     * @return the error message
     */
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
