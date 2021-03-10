package com.example.android.asklepius;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPatient extends AppCompatActivity {

	private static final String TAG = "NewPatient";
	boolean isSexSelected = false;
	boolean isComorbiditySelected = false;
	ScrollView sv;
	Patient patient;
	private DatabaseReference databaseReference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);
		getSupportActionBar().setTitle("Add New Patient");
		sv = findViewById(R.id.scrollView);
		databaseReference = FirebaseDatabase.getInstance().getReference().child("patients");

		patient = new Patient();
		patient.setInitial();
		setSliderTags();
	}

	public void setSliderTags() {
		Slider symptomSlider = findViewById(R.id.slider_symptom_severity);
		symptomSlider.setLabelFormatter(value -> {

			Log.d(TAG, "setSliderTags: " + value);

			if (value == 0.00) {
				patient.setSymptomsSeverity("Very Mild");
			} else if (value == 20.0) {
				patient.setSymptomsSeverity("Mild");
			} else if (value == 40.0) {
				patient.setSymptomsSeverity("Moderate");
			} else if (value == 60.0) {
				patient.setSymptomsSeverity("Severe");
			} else if (value == 80.0) {
				patient.setSymptomsSeverity("Very Severe");
			} else {
				return "";
			}

			return patient.getSymptomsSeverity();
		});

		Slider conditionSlider = findViewById(R.id.slider_patient_condition);
		conditionSlider.setLabelFormatter(value -> {
			Log.d(TAG, "setSliderTags: " + value);
			if (value == 0.00) {
				patient.setCondition("Very Mild");
			} else if (value == 20.0) {
				patient.setCondition("Mild");
			} else if (value == 40.0) {
				patient.setCondition("Moderate");
			} else if (value == 60.0) {
				patient.setCondition("Severe");
			} else if (value == 80.0) {
				patient.setCondition("Very Severe");
			} else {
				return "";
			}
			return patient.getCondition();
		});
	}

	public void onSexSelectRadioButtonClicked(View view) {
		EditText otherSexInput = findViewById(R.id.editText_patient_sex_other);
		isSexSelected = true;
		switch (view.getId()) {
			case R.id.radioButton_sex_male:
				patient.setSex(getString(R.string.male));
				otherSexInput.setEnabled(false);
				break;
			case R.id.radioButton_sex_female:
				patient.setSex(getString(R.string.female));
				otherSexInput.setEnabled(false);
				break;
			case R.id.radioButton_sex_other:
				otherSexInput.setEnabled(true);
				otherSexInput.requestFocus();
		}
	}


	public boolean symptomListCheckboxes() {
		boolean checked = false;
		CheckBox checkBox = findViewById(R.id.checkBox_fever);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Fever", true);
		}

		checkBox = findViewById(R.id.checkBox_fatigue);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Fatigue", true);
		}

		checkBox = findViewById(R.id.checkBox_dry_cough);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Dry Cough", true);
		}

		checkBox = findViewById(R.id.checkBox_aches_and_pains);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Aches and Pains", true);
		}

		checkBox = findViewById(R.id.checkBox_sore_throat);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Sore Throat", true);
		}

		checkBox = findViewById(R.id.checkBox_nasal_congestion);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Nasal Congestion", true);
		}

		checkBox = findViewById(R.id.checkBox_runny_nose);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Runny Nose", true);
		}

		checkBox = findViewById(R.id.checkBox_diarrhoea);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Diarrhoea", true);
		}

		checkBox = findViewById(R.id.checkBox_anosmia);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Anosmia", true);
		}

		checkBox = findViewById(R.id.checkBox_nasal_congestion);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Nasal Congestion", true);
		}

		checkBox = findViewById(R.id.checkBox_rash);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Rash", true);
		}

		checkBox = findViewById(R.id.checkBox_conjunctivitis);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Conjunctivitis", true);
		}

		checkBox = findViewById(R.id.checkBox_headache);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Headache", true);
		}

		checkBox = findViewById(R.id.checkBox_asymptomatic);
		if (checkBox.isChecked()) {
			checked = true;
			patient.symptomList.put("Asymptomatic", true);
		}

		return checked;

	}

	public void onSubmitButtonClicked(View view) {
		// Set patient name or raise error if empty
		TextInputLayout patientNameTextField = findViewById(R.id.textField_patient_name);
		String patientName = patientNameTextField.getEditText().getText().toString();
		if (patientName.trim().isEmpty()) {
			patientNameTextField.setError("Please Input Patient's Name!");
		} else {
			patient.setName(patientName);
		}

		// If doctor didn't specify the sex, or selected other but didn't input the other sex
		EditText otherSexInputEditText = findViewById(R.id.editText_patient_sex_other);
		if (isSexSelected == false) {
			errorScrollToView("Please specify patient's sex", R.id.textView_patient_sex);
		} else if (otherSexInputEditText.isEnabled()) {
			String otherSexInput = otherSexInputEditText.getText().toString();
			if (otherSexInput.trim().isEmpty()) {
				errorScrollToView("Please type in the patient's sex", R.id.textView_patient_sex);
			} else {
				patient.setSex(otherSexInput);
			}
		}


		// Set Patient Age or raise error if empty
		TextInputLayout patientAgeTextField = findViewById(R.id.textField_patient_age);
		String patientAge = patientAgeTextField.getEditText().getText().toString();
		if (patientAge.trim().isEmpty()) {
			patientAgeTextField.setError("Please Input Patient's Age!");
		} else {
			patient.setAge(Integer.parseInt(patientAge));
		}

		// Checks if minimum one of the checkboxes in the symptom list has been selected
		if (symptomListCheckboxes() == false) {
			Toast.makeText(this, "Please specify Patient's Symptoms", Toast.LENGTH_SHORT).show();
			View targetView = findViewById(R.id.textView_patient_symptoms);
			sv.smoothScrollTo(0, (int) targetView.getY());
		}

		// Check for null values in comorbidity
		EditText comorbidityDescription = findViewById(R.id.editText_comorbidties);
		if (isComorbiditySelected == false) {
			errorScrollToView("Please specify whether the patient had any comorbidities", R.id.textView_comorbidity);
		} else if (comorbidityDescription.isEnabled()) {
			String description = comorbidityDescription.getText().toString();
			if (description.trim().isEmpty()) {
				errorScrollToView("Please specify Patient's comorbidity", R.id.textView_comorbidity);
			} else {
				patient.setComorbidities(description);
			}
		}

		// Sliders are defaulted, and set in another method through event listeners

		TextInputLayout treatmentPlanTextField = findViewById(R.id.textField_treatment_plan);
		String treatmentPlan = treatmentPlanTextField.getEditText().getText().toString();
		if (treatmentPlan.isEmpty()) {
			treatmentPlanTextField.setError("Please specify the treatment plan");
		} else {
			patient.setTreatmentPlan(treatmentPlan);
		}

		TextInputLayout treatmentPeriodTextField = findViewById(R.id.textField_period_of_treatment);
		String treatmentPeriod = treatmentPeriodTextField.getEditText().getText().toString();
		if (treatmentPeriod.isEmpty()) {
			treatmentPeriodTextField.setError("Please specify the treatment period");
		} else {
			patient.setPeriodOfTreatment(treatmentPeriod);
		}

		TextInputLayout treatmentAdministrationMethodTextField = findViewById(R.id.textField_method_of_treatment_administration);
		String treatmentAdministrationMethod = treatmentAdministrationMethodTextField.getEditText().getText().toString();
		if (treatmentAdministrationMethod.isEmpty()) {
			treatmentAdministrationMethodTextField.setError("Please specify the treatment period");
		} else {
			patient.setMethodOfTreatmentAdministration(treatmentAdministrationMethod);
		}

		TextInputLayout frequencyPerDayTextField = findViewById(R.id.textField_frequency_of_administration_per_day);
		String frequencyAdministrationPerDay = frequencyPerDayTextField.getEditText().getText().toString();
		if (frequencyAdministrationPerDay.isEmpty()) {
			frequencyPerDayTextField.setError("Please specify the treatment period");
		} else {
			patient.setFrequencyOfAdministrationPerDay(Integer.parseInt(frequencyAdministrationPerDay));
		}

		TextInputLayout frequencyPerWeekTextField = findViewById(R.id.textField_frequency_of_administration_per_week);
		String frequencyPerWeek = frequencyPerWeekTextField.getEditText().getText().toString();
		if (frequencyPerWeek.isEmpty()) {
			frequencyPerWeekTextField.setError("Please specify the treatment period");
		} else {
			patient.setFrequencyOfAdministrationPerWeek(Integer.parseInt(frequencyPerWeek));
		}

		TextInputLayout resultTextField = findViewById(R.id.textField_result);
		String result = resultTextField.getEditText().getText().toString();
		if (result.isEmpty()) {
			resultTextField.setError("Please specify the treatment period");
		} else {
			patient.setResult(result);
		}

		TextInputLayout sideEffectsTextField = findViewById(R.id.textField_side_effects);
		String sideEffects = sideEffectsTextField.getEditText().getText().toString();
		if (sideEffects.isEmpty()) {
			sideEffectsTextField.setError("Please specify the treatment period");
		} else {
			patient.setSideEffects(sideEffects);
		}

		patient.setSourceOfInfection(((TextInputLayout) findViewById(R.id.textField_source_of_infection)).getEditText().getText().toString());
		patient.setPatientInfectivity(((TextInputLayout) findViewById(R.id.textField_patient_infectivity)).getEditText().getText().toString());

		uploadData();
	}

	public void uploadData() {
		Log.d(TAG, "uploadData: " + patient);

		databaseReference.push().setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Log.d(TAG, "uploadData onSuccess: Data successfully uploaded to firebase");
				Toast.makeText(NewPatient.this, "Patient Data Successfully Added", Toast.LENGTH_SHORT).show();
			}
		})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.d(TAG, "uploadData onFailure: Data Upload Failed!");
						Toast.makeText(NewPatient.this, "Patient Data Addition Failed! Please check your network!", Toast.LENGTH_SHORT).show();
					}
				});
		finish();
	}

	public void onComorbiditySelectRadioButtonClicked(View view) {
		isComorbiditySelected = true;
		EditText comorbidityDescription = findViewById(R.id.editText_comorbidties);
		switch (view.getId()) {
			case R.id.radioButton_comorbidity_no:
				patient.setComorbidities("No");
				comorbidityDescription.setEnabled(false);
				break;
			case R.id.radioButton_comorbidity_yes:
				comorbidityDescription.setEnabled(true);
				comorbidityDescription.requestFocus();
				break;
		}
	}

	private void errorScrollToView(String errorString, @IdRes int viewId) {
		Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
		View targetView = findViewById(viewId);
		sv.smoothScrollTo(0, (int) targetView.getY());
	}
}