package com.example.android.asklepius;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textservice.TextInfo;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchParameters extends AppCompatActivity {

	private static final String TAG = "SearchParameters";
	String sex = "Male";
	String severity = "Very Mild";
	String condition = "Very Mild";
	String comorbidity = "No";
	List<String> symptoms = new ArrayList<>();
	int age;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_parameters);
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
			case R.id.editText_patient_sex_other: sex = "Other";
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
		ChipGroup symptomsChipGroup = findViewById(R.id.chipGroup_symptoms);
		for (int i = 0; i < symptomsChipGroup.getChildCount(); i++) {
			Chip chip = (Chip) symptomsChipGroup.getChildAt(i);
			if (chip.isChecked()) {
				symptoms.add(chip.getText().toString());
			}
		}

		// Get Patient Age
		TextInputLayout ageInputLayout = findViewById(R.id.textField_patient_age);
		EditText ageText = ageInputLayout.getEditText();
		if (ageText.getText().toString().trim().isEmpty()) {
			ageInputLayout.setError("Please input patient's age to get matches!");
		} else {
			age = Integer.parseInt(ageText.getText().toString().trim());
		}

		Log.d(TAG, "onSearchButtonClicked: " + sex + "\n" + age + "\n" + symptoms.toString() +
				comorbidity + "\n" + severity + "\n" + condition);
	}
}