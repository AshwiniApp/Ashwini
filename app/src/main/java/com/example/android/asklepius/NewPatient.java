package com.example.android.asklepius;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

public class NewPatient extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_patient);
		getSupportActionBar().setTitle("Add New Patient");

		Slider slider = findViewById(R.id.slider);
		slider.setLabelFormatter(new LabelFormatter() {
			@NonNull
			@Override
			public String getFormattedValue(float value) {
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
			}
		});
	}
}