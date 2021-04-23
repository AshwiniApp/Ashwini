package com.example.android.asklepius;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private boolean unregistered = true;
    private static final String TAG = "UserValidationUpload";
    File imageFile;
    String imageURL;
    Uri imageUri;
    StorageReference storage;
    private static final int REQUEST_IMAGE_CAPTURE = 887;
    public static User user;
    private boolean uploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_validation_upload);

        storage = FirebaseStorage.getInstance().getReference();
    }

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
                ((Button) findViewById(R.id.button_submit_validation_info)).setEnabled(true);
                imageFile.delete();

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
                Log.d(TAG, "onFailure: Image failed to upload. The following exception occurred: " );
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Snackbar.make(((Button) findViewById(R.id.button_submit_validation_info)), "Uploading Image", Snackbar.LENGTH_SHORT).show();
            uploadImage();
        }
    }

    @Override
    public void onBackPressed() {
        if (uploaded) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "Please upload your validation details first!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSubmitValidationInfoButtonClicked(View view) {
        TextInputLayout icmrIDTextField = findViewById(R.id.textField_ICMR_ID);
        String icmrId = icmrIDTextField.getEditText().getText().toString().trim();
        if (icmrId.isEmpty()) {
            icmrIDTextField.setError("Please input your ICMR ID!");
            return;
        }

        if (user != null) {
            unregistered = false;
        }

        if (unregistered) {
            User user = new User(imageURL, FirebaseAuth.getInstance().getUid(), icmrId, Values.userVerificationState.Pending.toString(), null,
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            DatabaseReference userDB = Values.userDB;
            userDB.push().setValue(user);
            uploaded = true;
        } else {
            DatabaseReference userDB = Values.userDB;
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot childrenSnapshot : snapshot.getChildren()) {
                        if (childrenSnapshot.getValue(User.class).equals(user)) {
                            User newUser = new User(imageURL, FirebaseAuth.getInstance().getUid(), icmrId, Values.userVerificationState.Pending.toString(), null,
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            String key = childrenSnapshot.getKey();
                            userDB.child(key).setValue(newUser);
                            uploaded = true;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: " + error.toException());
                }
            };
            userDB.addListenerForSingleValueEvent(listener);

            if (uploaded == true) {
                userDB.removeEventListener(listener);
            }
        }
    }
}