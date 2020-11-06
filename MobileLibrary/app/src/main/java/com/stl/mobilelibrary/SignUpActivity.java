package com.stl.mobilelibrary;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

/**
 * new user sign up
 */
public class SignUpActivity extends AppCompatActivity {
    private EditText usernameText;
    private EditText passwordText;
    private EditText emailAddressText;
    private EditText phoneNumberText;
    private CircleImageView profilePhotoImageView;
    private ViewableImage profileImage;
    public static final int PICK_PROFILE_IMAGE = 0;


    /**
     * Bind UI elements and setup listeners
     * @param savedInstanceState: the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        usernameText = findViewById(R.id.SignUpUsername);
        passwordText = findViewById(R.id.SignUpPassword);
        emailAddressText = findViewById(R.id.SignUpEmail);
        phoneNumberText = findViewById(R.id.SignUpPhoneNumber);
        MaterialButton profilePhotoButton = findViewById(R.id.addProfileImage);
        profilePhotoImageView = findViewById(R.id.newProfileImage);

        profileImage = new ViewableImage(BitmapFactory.decodeResource(getResources(), R.drawable.user));

        usernameText.addTextChangedListener(new TextValidator(usernameText) {
            /**
             * Validate the username
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setUsername(text);
                if (!(signInValidator.isUserNameValid())){
                    usernameText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        passwordText.addTextChangedListener(new TextValidator(passwordText) {
            /**
             * Validate the password
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setPassword(text);
                if (!(signInValidator.isPasswordValid())){
                    passwordText.setError(signInValidator.getErrorMessage());
                }
            }
        });



        emailAddressText.addTextChangedListener(new TextValidator(emailAddressText) {
            /**
             * Validate the email address
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setEmailAddress(text);
                if (!(signInValidator.isEmailNameValid())){
                    emailAddressText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        phoneNumberText.addTextChangedListener(new TextValidator(phoneNumberText) {
            /**
             * Validate the phonenumber
             * @param textView: the textview to validate
             * @param text: the text to validate
             */
            @Override
            public void validate(TextView textView, String text) {
                SignUpValidator signInValidator = new SignUpValidator();
                signInValidator.setPhoneNumber(text);
                if (!(signInValidator.isPhoneNumberValid())){
                    phoneNumberText.setError(signInValidator.getErrorMessage());
                }
            }
        });

        profilePhotoButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Set the user's profile photo
             * @param v: the profile photo button
             */
              @SuppressLint("IntentReset")
              @Override
              public void onClick(View v) {
                  Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                  getIntent.setType("image/*");

                  Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  pickIntent.setType("image/*");

                  Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                  chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                  startActivityForResult(chooserIntent, PICK_PROFILE_IMAGE);
              }
          }
        );
    }


    /**
     * Register the user if all of thier info is valid and thier username is unique.
     * @param view: the register button
     */
    public void RegisterOnClick(View view) {
        String userName = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String emailAddress = emailAddressText.getText().toString();
        String phoneNumber = phoneNumberText.getText().toString();
        SignUpValidator signUpValidator = new SignUpValidator(userName, password, emailAddress, phoneNumber);
        if (signUpValidator.isValid()){
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.createAccountIfValid(userName, password, emailAddress, phoneNumber, profileImage);
        }
        else{
            Toast.makeText(SignUpActivity.this, "Please fix the above errors", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Retrieve the selected profile image
     * @param requestCode: the request code
     * @param resultCode: the result of the call
     * @param data: the data retrieved
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PROFILE_IMAGE) {
            if (resultCode == RESULT_OK) {
                // The user picked a photo.
                Uri imageUri = data.getData();
                profilePhotoImageView.setImageURI(imageUri);
                try {
                    profileImage = new ViewableImage(ViewableImage.getBitmapFromUri(imageUri, this));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
