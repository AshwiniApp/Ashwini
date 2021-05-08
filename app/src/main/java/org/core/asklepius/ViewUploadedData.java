package org.core.asklepius;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.core.asklepius.databinding.ActivityViewUploadedDataBinding;

import java.util.ArrayList;
import java.util.List;

public class ViewUploadedData extends AppCompatActivity {

    ActivityViewUploadedDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewUploadedDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Uploaded Patient Data");

        List<Patient> filteredData = getFilteredPatientData();
        RecyclerView recyclerView = binding.recylerViewUploadedPatientData;
        if (filteredData.isEmpty()) {
            Snackbar.make(recyclerView, "Looks like you haven't done any uploads yet!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Okay", v -> {
                        // Do nothing, let the snackbar dismiss
                    })
                    .show();
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            PatientListAdapter adapter = new PatientListAdapter(filteredData, ViewUploadedData.this, false);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Values.EDIT_PATIENT_DATA && resultCode == RESULT_OK) {
            finish();
            startActivity(getIntent());
        }
    }
}