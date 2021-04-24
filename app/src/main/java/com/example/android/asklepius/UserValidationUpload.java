package com.example.android.asklepius;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class UserValidationUpload extends AppCompatActivity {

    private static final String TAG = "UserValidationUpload";
    private File imageFile;
    private String imageURL;
    private Uri imageUri;
    private StorageReference storage;
    private static final int REQUEST_IMAGE_CAPTURE = 887;
    public static User user;
    private boolean uploaded = false;
    private static final int PICK_IMAGE = 313;
    private Snackbar uploadingSnackBar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_validation_upload);

        // If user is not null, that means the user already exists in the database, leading to the
        // only possibility that is rejection.
        if (user != null) {
            ((TextView) findViewById(R.id.textView_validation_status)).setText("Current Status: " + Values.userVerificationState.Rejected.toString());
            TextView rejectionStatusTextView = findViewById(R.id.textView_validation_rejection_reason);
            rejectionStatusTextView.setText("Rejection Reason: " + user.rejectionReason);
            rejectionStatusTextView.setVisibility(View.VISIBLE);

            ((TextInputLayout) findViewById(R.id.textField_ICMR_ID)).getEditText().setText(user.regID);
        }

        storage = FirebaseStorage.getInstance().getReference();
    }

    /**
     * If the user chooses to take the image through the camera, this method generates an
     * empty file in the external app directory, and loads the resultant image in that file
     * which gives us a URI which can be used to upload that image to Firebase
     */
    public void onTakeIDImageButtonClicked(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            imageFile = createImageFile();
        }

        if (imageFile != null) {
            imageUri = FileProvider.getUriForFile(this, "com.example.android.filecontentprovider", imageFile);
        }

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * If the user chooses to select an image from the gallery, instantiate and fire an intent
     * to do so.
     */
    public void onSelectIDImageButtonClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    /**
     * @return a new File object created for storing the returned data from the Camera API
     *         after an image capture.
     */
    private File createImageFile() {
        String imageFileName = "JPEG_" + FirebaseAuth.getInstance().getCurrentUser().getUid();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * Creates a reference to a Firebase Storage location based on the current user ID to prevent
     * duplicate uploads of the same ID in the future thus saving space. After a reference is created,
     * the imageURI (obtained either from the generated file after image capture from Camera) or a file
     * selected from the gallery is used to upload the file to the Firebase Storage. Until the file is
     * successfully uploaded, a SnackBar and ProgressBar help the user ascertain the upload progress.
     * When the file is uploaded, a download URL is obtained and the submit button is enabled.
     * A error is displayed if the upload fails for some reason
     *
     * Note: The uploaded file is completely uncompressed to account for improper angles.
     */
    private void uploadImage() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference imageReference = storage.child("images/" + userID);
        LinearProgressIndicator progressBar = findViewById(R.id.progressBar_upload_status);
        progressBar.setVisibility(View.VISIBLE);

        imageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserValidationUpload.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuccess: Image uploaded");
                uploadingSnackBar.dismiss();
                ((Button) findViewById(R.id.button_submit_validation_info)).setEnabled(true);
                try {
                    imageFile.delete();
                } catch (NullPointerException e) {
                    // DO nothing, the file's been selected from the gallery
                }

                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURL = uri.toString();
                    }

                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserValidationUpload.this, "The image failed to upload!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: Image failed to upload. The following exception occurred: " );
                e.printStackTrace();
            }
        });

    }

    /**
     * The first condition handles an image capture by the camera, in which case imageURI is already
     * set. It's simply a matter of displaying an informative SnackBar and triggering the upload.
     * The second condition handles the case in which the image is selected from the gallery,
     * in which case we just obtain the URI from the returned intent and trigger upload.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            uploadingSnackBar = Snackbar.make(((Button) findViewById(R.id.button_submit_validation_info)), "Uploading Image", Snackbar.LENGTH_INDEFINITE);
            uploadingSnackBar.show();
            uploadImage();
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            uploadingSnackBar = Snackbar.make(((Button) findViewById(R.id.button_submit_validation_info)), "Uploading Image", Snackbar.LENGTH_INDEFINITE);
            uploadingSnackBar.show();
            uploadImage();
        }
    }

    /**
     * Back button is disabled until the details are submitted
     */
    @Override
    public void onBackPressed() {
        if (uploaded) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Please upload your validation details first!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This button is enabled when the image upload is successfully completed. If the user is new,
     * that is there is no presence in the database, and a new child is created and the user object is pushed.
     *
     * If the user already exists in the database, the complete list of users is fetched from the database once,
     * and the existing used object at the matching key is replace by a new one. The image is simply replaced at
     * the same ID, so there is no duplication.
     */
    public void onSubmitValidationInfoButtonClicked(View view) {
        TextInputLayout icmrIDTextField = findViewById(R.id.textField_ICMR_ID);
        String icmrId = icmrIDTextField.getEditText().getText().toString().trim();
        if (icmrId.isEmpty()) {
            icmrIDTextField.setError("Please input your ICMR ID!");
            return;
        }

        if (user == null) {
            User user = new User(imageURL, FirebaseAuth.getInstance().getUid(), icmrId, Values.userVerificationState.Pending.toString(), "",
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            DatabaseReference userDB = Values.userDB;
            userDB.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    uploaded = true;
                    Toast.makeText(UserValidationUpload.this, "Validation Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        } else {
            DatabaseReference userDB = Values.userDB;
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childrenSnapshot : snapshot.getChildren()) {
                        if (childrenSnapshot.getValue(User.class).equals(user)) {
                            User newUser = new User(imageURL, FirebaseAuth.getInstance().getUid(), icmrId, Values.userVerificationState.Pending.toString(), "",
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            String key = childrenSnapshot.getKey();
                            userDB.child(key).setValue(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    uploaded = true;
                                    Toast.makeText(UserValidationUpload.this, "Validation Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error.toException());
                }
            };
            userDB.addListenerForSingleValueEvent(listener);

            if (uploaded) {
                userDB.removeEventListener(listener);
            }
        }
    }
}