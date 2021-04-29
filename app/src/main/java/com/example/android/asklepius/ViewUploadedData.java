package com.example.android.asklepius;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ViewUploadedData extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_uploaded_data);

		List<Patient> filteredData = getFilteredPatientData();
		RecyclerView recyclerView = findViewById(R.id.recylerView_uploaded_patient_data);
		if (filteredData.isEmpty()) {
			Snackbar.make(recyclerView, "Looks like you haven't done any uploads yet!", Snackbar.LENGTH_INDEFINITE)
					.setAction("Okay", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// Do nothing, let the snackbar dismiss
						}
					})
					.show();
		} else {
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			PatientListAdapter adapter = new PatientListAdapter(filteredData, false, this);
			recyclerView.setAdapter(adapter);
		}
	}

	private List<Patient> getFilteredPatientData() {
		String doctorID = FirebaseAuth.getInstance().getCurrentUser().getUid();
		List<Patient> patients = new ArrayList<>();
		for (Patient patient : Values.patients.keySet()) {
			if (patient.getDoctorID().equals(doctorID)) {
				patients.add(patient);
			}
		}

		return patients;
	}
}