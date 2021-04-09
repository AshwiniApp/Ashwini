package com.example.android.asklepius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SearchParameters extends AppCompatActivity {

	private static final String TAG = "SearchParameters";
	String sex = "Male";
	String severity = "Very Mild";
	String condition = "Very Mild";
	String comorbidity = "No";
	List<String> symptoms = new ArrayList<>();
	int age;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_parameters);
		progressBar = findViewById(R.id.progressBar_search_patient);
		progressBar.setVisibility(View.GONE);
		setSliderTags();
		inflateChips();
	}

	public void inflateChips() {
		ChipGroup chipGroup = findViewById(R.id.chipGroup_symptoms);
		for (String symptom : Values.symptoms) {
			Chip chip = (Chip) getLayoutInflater().inflate(R.layout.symptom_chip_single, chipGroup, false);
			chipGroup.addView(chip);
			chip.setText(symptom);
		}
	}

	public void onSexSelectRadioButtonClicked(View view) {
		switch (view.getId()) {
			case R.id.radioButton_sex_male: sex = "Male";
			break;
			case R.id.radioButton_sex_female: sex = "Female";
			break;
			case R.id.radioButton_sex_other: sex = "Other";
			break;
		}
	}

	public void setSliderTags() {
		Slider symptomSeveritySlider = findViewById(R.id.slider_symptom_severity);
		symptomSeveritySlider.setLabelFormatter(value -> {

			Log.d(TAG, "setSliderTags: " + value);

			if (value == 0.00) {
				severity = "Very Mild";
			} else if (value == 20.0) {
				severity = "Mild";
			} else if (value == 40.0) {
				severity = "Moderate";
			} else if (value == 60.0) {
				severity = "Severe";
			} else if (value == 80.0) {
				severity = "Very Severe";
			} else {
				return "";
			}

			return severity;
		});

		Slider conditionSlider = findViewById(R.id.slider_patient_condition);
		conditionSlider.setLabelFormatter(value -> {
			Log.d(TAG, "setSliderTags: " + value);
			if (value == 0.00) {
				condition = "Very Mild";
			} else if (value == 20.0) {
				condition = "Mild";
			} else if (value == 40.0) {
				condition = "Moderate";
			} else if (value == 60.0) {
				condition = "Severe";
			} else if (value == 80.0) {
				condition = "Very Severe";
			} else {
				return "";
			}
			return condition;
		});
	}

	public void onComorbiditySelectRadioButtonClicked(View view) {
		switch (view.getId()) {
			case R.id.radioButton_comorbidity_no: comorbidity = "No";
			break;
			case R.id.radioButton_comorbidity_yes: comorbidity = "Yes";
			break;
		}
	}

	public void onSearchButtonClicked(View view) {
		// Get Symptoms
		boolean checked = false;
		ChipGroup symptomsChipGroup = findViewById(R.id.chipGroup_symptoms);
		for (int i = 0; i < symptomsChipGroup.getChildCount(); i++) {
			Chip chip = (Chip) symptomsChipGroup.getChildAt(i);
			if (chip.isChecked()) {
				symptoms.add(chip.getText().toString());
				checked = true;
			}
		}
		
		if (checked == false) {
			ScrollView sv = findViewById(R.id.scrollView);
			sv.smoothScrollTo(0, (int) ((TextView) findViewById(R.id.textView_patient_symptoms)).getY());
			Toast.makeText(this, "Please select at least one symptom to generate the results!", Toast.LENGTH_SHORT).show();
			return;
		}

		// Get Patient Age
		TextInputLayout ageInputLayout = findViewById(R.id.textField_patient_age);
		EditText ageText = ageInputLayout.getEditText();
		if (ageText.getText().toString().trim().isEmpty()) {
			ageInputLayout.setError("Please input patient's age to get matches!");
			return;
		} else {
			age = Integer.parseInt(ageText.getText().toString().trim());
		}

		Log.d(TAG, "onSearchButtonClicked: " + sex + "\n" + age + "\n" + symptoms.toString() +
				comorbidity + "\n" + severity + "\n" + condition);

		generateSearchResults();
	}

	private void generateSearchResults() {

		progressBar.setVisibility(View.VISIBLE);

		List<Patient> currentFilteredPatients = new ArrayList<>();
		List<Patient> previouslyFilteredPatients;

		// Filter ages with a relaxation key of 4
		for (Patient patient : MainActivity.patients) {
			if (patient.age >= age - 4 && patient.age <= age + 4) {
				currentFilteredPatients.add(patient);
			}
		}
		previouslyFilteredPatients = currentFilteredPatients;
		currentFilteredPatients = new ArrayList<>();

		// Filter male and female sex
		if (sex.equals("Male") || sex.equals("Female")) {
			for (Patient patient : previouslyFilteredPatients) {
				if (patient.sex.equals(sex)) {
					currentFilteredPatients.add(patient);
				}
			}
		} else {
			// For sex specified as other
			for (Patient patient : previouslyFilteredPatients) {
				if (!patient.sex.equals("Male") && !patient.sex.equals("Female")) {
					currentFilteredPatients.add(patient);
				}
			}
		}
		previouslyFilteredPatients = currentFilteredPatients;
		currentFilteredPatients = new ArrayList<>();

		// Filter by Symptoms
		for (String symptom : symptoms) {
			for (Patient patient : previouslyFilteredPatients) {
				if (patient.getSymptomList().get(symptom)) {
					currentFilteredPatients.add(patient);
				}
			}

			previouslyFilteredPatients = currentFilteredPatients;
			currentFilteredPatients = new ArrayList<>();
		}

		// Filter by Comorbidity
		if (comorbidity.equals("No")) {
			for (Patient patient : previouslyFilteredPatients) {
				if (patient.getComorbidities().equals("No")) {
					currentFilteredPatients.add(patient);
				}
			}
		} else {
			for (Patient patient : previouslyFilteredPatients) {
				if (! patient.getComorbidities().equals("No")) {
					currentFilteredPatients.add(patient);
				}
			}
		}

		previouslyFilteredPatients = currentFilteredPatients;
		currentFilteredPatients = new ArrayList<>();

		// Filter by Severity
		String[] range;
		switch (severity) {
			case "Very Mild":
				range = new String[]{"Very Mild", "Very Mild", "Mild"};
				break;
			case "Mild":
				range = new String[]{"Very Mild", "Mild", "Moderate"};
				break;
			case "Moderate":
				range = new String[]{"Mild", "Moderate", "Severe"};
				break;
			case "Severe":
				range = new String[]{"Moderate", "Severe", "Very Severe"};
				break;
			default:
				range = new String[]{"Severe", "Very Severe", "Very Severe"};
				break;
		}

		for (Patient patient : previouslyFilteredPatients) {
			if (patient.getSymptomsSeverity().equals(range[0]) ||
					patient.getSymptomsSeverity().equals(range[1]) ||
					patient.getSymptomsSeverity().equals(range[2])) {
				currentFilteredPatients.add(patient);
			}
		}

		previouslyFilteredPatients = currentFilteredPatients;
		currentFilteredPatients = new ArrayList<>();

		// Filter by condition
		switch (condition) {
			case "Very Mild":
				range = new String[]{"Very Mild", "Very Mild", "Mild"};
				break;
			case "Mild":
				range = new String[]{"Very Mild", "Mild", "Moderate"};
				break;
			case "Moderate":
				range = new String[]{"Mild", "Moderate", "Severe"};
				break;
			case "Severe":
				range = new String[]{"Moderate", "Severe", "Very Severe"};
				break;
			default:
				range = new String[]{"Severe", "Very Severe", "Very Severe"};
				break;
		}

		for (Patient patient : previouslyFilteredPatients) {
			if (patient.getCondition().equals(range[0]) ||
					patient.getCondition().equals(range[1]) ||
					patient.getCondition().equals(range[2])) {
				currentFilteredPatients.add(patient);
			}
		}

		progressBar.setVisibility(View.GONE);

		DisplaySearchResults.filteredList = currentFilteredPatients;
		Intent intent = new Intent(this, DisplaySearchResults.class);
		startActivity(intent);
	}
}