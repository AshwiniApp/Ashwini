package com.example.android.asklepius;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;

public class DisplayPatientData extends AppCompatActivity {

	Patient patient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_patient_data);

		patient = (Patient) getIntent().getSerializableExtra("display");
		setData();
	}

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

	private void setSymptoms() {
		Map<String, Boolean> symptoms = patient.getSymptomList();
		setSymptomChips(R.id.chip_fever, symptoms.get("Fever"));
		setSymptomChips(R.id.chip_fatigue, symptoms.get("Fatigue"));
		setSymptomChips(R.id.chip_dry_cough, symptoms.get("Dry Cough"));
		setSymptomChips(R.id.chip_aches_and_pains, symptoms.get("Aches and Pains"));
		setSymptomChips(R.id.chip_sore_throat, symptoms.get("Sore Throat"));
		setSymptomChips(R.id.chip_nasal_congestion, symptoms.get("Nasal Congestion"));
		setSymptomChips(R.id.chip_runny_nose, symptoms.get("Runny Nose"));
		setSymptomChips(R.id.chip_diarrhoea, symptoms.get("Diarrhoea"));
		setSymptomChips(R.id.chip_anosmia, symptoms.get("Anosmia"));
		setSymptomChips(R.id.chip_rash, symptoms.get("Rash"));
		setSymptomChips(R.id.chip_conjunctivitis, symptoms.get("Conjunctivitis"));
		setSymptomChips(R.id.chip_headache, symptoms.get("Headache"));
		setSymptomChips(R.id.chip_asymptomatic, symptoms.get("Asymptomatic"));
	}

	private void setSymptomChips(@IdRes int resourceID, boolean value) {
		Chip chip = findViewById(resourceID);
		if (value == false) {
			chip.setVisibility(View.GONE);
		}
	}

	private void setTextFields(@IdRes int resourceID, String setString) {
		TextInputLayout textInputLayout;
		EditText editText;

		textInputLayout = findViewById(resourceID);
		editText = textInputLayout.getEditText();
		editText.setText(setString);
	}
}