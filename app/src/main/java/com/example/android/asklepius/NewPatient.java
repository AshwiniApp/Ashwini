package com.example.android.asklepius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

public class NewPatient extends AppCompatActivity {

	private static final String TAG = "NewPatient";
	EditText otherSexInput;
	Patient patient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);
		getSupportActionBar().setTitle("Add New Patient");

		otherSexInput = findViewById(R.id.editText_patient_sex_other);
		patient = new Patient();

		Slider slider = findViewById(R.id.slider_symptom_severity);
		slider.setLabelFormatter(value -> {

			Log.d(TAG, "getFormattedValue: " + value);

			if (value == 0.00) {
				return "Very Mild";
			} else if (value == 20.0) {
				return "Mild";
			} else if (value == 40.0) {
				return "Average";
			} else if (value == 60.0) {
				return "Severe";
			} else if (value == 80.0) {
				return "Very Severe";
			} else {
				return "";
			}
		});
	}

	public void onSexSelectRadioButtonClicked(View view) {
		switch(view.getId()) {
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
}