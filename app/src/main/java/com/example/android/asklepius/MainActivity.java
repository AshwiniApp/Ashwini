package com.example.android.asklepius;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";
	DatabaseReference patientDB;
	static List<Patient> patients = new ArrayList<>();

	private static final int LOGIN_ACTIVITY_REQUEST_CODE = 262;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent(this, LoginActivity.class);
		startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NewPatient.class)));

		patientDB = FirebaseDatabase.getInstance().getReference().child("patients");
		ValueEventListener patientChangeListener = new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				Patient patient;
				patients.clear();
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

	public void onViewPatientDataButtonClicked(View view) {
		// Launch view patient activity
		Intent intent = new Intent(this, ViewUploadedData.class);
		startActivity(intent);
	}
}