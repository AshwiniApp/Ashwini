package com.example.android.asklepius;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 262;
    Map<Patient, String> patients = Values.patients;
    List<User> users = Values.users;
    DatabaseReference patientDB;
    DatabaseReference userDB;
    private ValueEventListener userChangeListener;
    private static final int RC_VALIDATION = 743;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sign_out: {
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainActivity.this, "You've been successfully signed out!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                break;
            }

            case R.id.action_delete_user: {
                String currentUserID = FirebaseAuth.getInstance().getUid();
                FirebaseStorage.getInstance().getReference().child("images/" + currentUserID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final String[] key = {""};
                        userDB.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                for (DataSnapshot childSnapshot : task.getResult().getChildren()) {
                                    if (childSnapshot.getValue(User.class).userID.equals(currentUserID)) {
                                        key[0] = childSnapshot.getKey();
                                        break;
                                    }
                                }

                                userDB.removeEventListener(userChangeListener);
                                userDB.child(key[0]).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        AuthUI.getInstance().delete(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(MainActivity.this, "User successfully deleted!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads the MainActivity UI
     */
    private void setContentToMain() {
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NewPatient.class)));
    }

	/**
	 * Initializes the patient database reference and attaches listeners
	 */
	private void initializePatientDB() {
        patientDB = FirebaseDatabase.getInstance().getReference().child("patients");

        // Attaching a ValueEventListener is better, since upon any changes, the entire list is
        // updated. This prevents writing methods for adding, updating and deleting data.
        ValueEventListener patientChangeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Patient patient;
                patients.clear();

                // The parameter snapshot basically contains the entire list. We iterate on the list
                // by calling the getChildren method on the param snapshot
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    patient = childSnapshot.getValue(Patient.class);
                    Log.d(TAG, "onDataChange patientDB: " + patient);
                    patients.put(patient, childSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled patientDB: " + error.toException());
            }
        };
        patientDB.addValueEventListener(patientChangeListener);
        Values.patientDB = patientDB;
    }

    /**
     * @return user object if the user is in the database, null otherwise.
     */
    private User check() {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for (User user : Values.users) {
            if (user.userID.equals(currentUserID)) {
                return user;
            }
        }

        return null;
    }

    /**
     * Initialized the user database reference and attaches listeners
     */
    private void initializeUserDB() {
        userDB = FirebaseDatabase.getInstance().getReference().child("users");

        userChangeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user;
                users.clear();

                for (DataSnapshot childrenSnapshot : snapshot.getChildren()) {
                    user = childrenSnapshot.getValue(User.class);
                    Log.d(TAG, "onDataChange userDB: " + user);
                    users.add(user);
                }

                if (!users.isEmpty()) {
                    user = check();
                    if (user == null || user.status.equals(Values.userVerificationState.Rejected.toString())) {
                        // user will be null if no match is found, indicating first time user
                        // the UserValidationActivity will handle the null value
                        Intent intent = new Intent(MainActivity.this, UserValidationUpload.class);
                        UserValidationUpload.user = user;
                        startActivityForResult(intent, RC_VALIDATION);
                    } else if (user.status.equals(Values.userVerificationState.Pending.toString())) {
                        // Snippet is run if the validation is pending, in which case all actions are disabled.
                        setContentToMain();
                        findViewById(R.id.fab).setVisibility(View.GONE);
                        findViewById(R.id.button_view_uploaded_patient_data).setVisibility(View.GONE);
                        findViewById(R.id.button_search_patient_data).setVisibility(View.GONE);
                        ((TextView) findViewById(R.id.textView_welcome)).setText("ICMR Validation in process!");
                    } else {
                        // User is verified.
                        setContentToMain();
                        ((TextView) findViewById(R.id.textView_welcome)).setText("Hello, " + user.username.split(" ")[0]);
                    }
                } else {
                    Log.d(TAG, "onDataChange userDB: no user data obtained from rtdb");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled userDB: " + error.toException());
            }
        };
        userDB.addValueEventListener(userChangeListener);
        Values.userDB = userDB;
    }

    /**
     * Exit the app if the login failed
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            } else if (resultCode == RESULT_OK) {
                initializePatientDB();
                initializeUserDB();
            }
        }

        if (requestCode == RC_VALIDATION) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    /**
     * Launch the view uploaded patient data activity
     */
    public void onViewPatientDataButtonClicked(View view) {
        // Launch view patient activity
        Intent intent = new Intent(this, ViewUploadedData.class);
        startActivity(intent);
    }

    /**
     * Launch the search patient activity
     */
    public void onSearchPatientDataButtonClicked(View view) {
        Intent intent = new Intent(this, SearchParameters.class);
        startActivity(intent);
    }
}