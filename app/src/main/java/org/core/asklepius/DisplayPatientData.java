package org.core.asklepius;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

public class DisplayPatientData extends AppCompatActivity {

    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_patient_data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Patient Data");

        patient = (Patient) getIntent().getSerializableExtra("display");
        setData();
    }

    /**
     * Set data fields in the activity
     */
    private void setData() {

        setSymptoms();

        setTextFields(R.id.textField_display_patient_age, String.valueOf(patient.getAge()));
        setTextFields(R.id.textField_display_patient_sex, patient.getSex());
        setTextFields(R.id.textField_display_patient_comorbidities, patient.getComorbidities());
        setTextFields(R.id.textField_display_patient_symptom_severity, patient.getSymptomsSeverity());
        setTextFields(R.id.textField_display_patient_condition, patient.getCondition());
        setTextFields(R.id.textField_display_patient_treatment_plan, patient.getTreatmentPlan());
        setTextFields(R.id.textField_display_patient_tratment_period, patient.getPeriodOfTreatment());
        setTextFields(R.id.textField_display_patient_treatment_administration_method, patient.getMethodOfTreatmentAdministration());
        setTextFields(R.id.textField_display_patient_frequency_per_day, String.valueOf(patient.getFrequencyOfAdministrationPerDay()));
        setTextFields(R.id.textField_display_patient_frequency_per_week, String.valueOf(patient.getFrequencyOfAdministrationPerWeek()));
        setTextFields(R.id.textField_display_patient_result, patient.getResult());
        setTextFields(R.id.textField_display_patient_side_effects, patient.getSideEffects());
        setTextFields(R.id.textField_display_patient_source_of_infection, patient.getSourceOfInfection());
        setTextFields(R.id.textField_display_patient_infectivity, patient.getPatientInfectivity());
    }

    /**
     * Inflate and set symptom chips in the activity
     */
    private void setSymptoms() {
        Map<String, Boolean> symptoms = patient.getSymptomList();
        ChipGroup symptomsChipGroups = findViewById(R.id.chipGroup_symptoms);
        for (String symptom : Values.symptoms) {
            if (symptoms.get(symptom)) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.symptom_chip_single, symptomsChipGroups, false);
                symptomsChipGroups.addView(chip);
                chip.setText(symptom);
                chip.setEnabled(false);
            }
        }
    }

    private void setTextFields(@IdRes int resourceID, String setString) {
        TextInputLayout textInputLayout;
        EditText editText;

        textInputLayout = findViewById(resourceID);
        editText = textInputLayout.getEditText();
        editText.setText(setString);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}