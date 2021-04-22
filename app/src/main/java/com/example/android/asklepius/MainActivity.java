package com.example.android.asklepius;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";

	private static final int LOGIN_ACTIVITY_REQUEST_CODE = 262;
	List<Patient> patients = Values.patients;
	DatabaseReference patientDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NewPatient.class)));

		initializePatientDB();
	}

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
				for (DataSnapshot childSnapshot: snapshot.getChildren()) {
					patient = childSnapshot.getValue(Patient.class);
					Log.d(TAG, "onDataChange: " + patient);
					patients.add(patient);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.d(TAG, "onCancelled: " + error.toException());
			}
		};
		patientDB.addValueEventListener(patientChangeListener);
		Values.patientDB = patientDB;
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