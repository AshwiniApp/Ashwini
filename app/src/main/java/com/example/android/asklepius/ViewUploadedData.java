package com.example.android.asklepius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ViewUploadedData extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_uploaded_data);

		RecyclerView recyclerView = findViewById(R.id.recylerView_uploaded_patient_data);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		PatientListAdapter adapter = new PatientListAdapter(getFilteredPatientData(), false);
		recyclerView.setAdapter(adapter);
	}

	private List<Patient> getFilteredPatientData() {
		String doctorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
		List<Patient> patients = new ArrayList<>();
		for (Patient patient : MainActivity.patients) {
			if (patient.getDoctorID().equals(doctorID)) {
				patients.add(patient);
			}
		}

		return patients;
	}
}