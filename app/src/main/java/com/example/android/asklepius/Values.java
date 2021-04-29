package com.example.android.asklepius;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Values {

	public static Map<Patient, String> patients = new HashMap<>();
	public static List<User> users = new ArrayList<>();
	public static DatabaseReference patientDB;
	public static DatabaseReference userDB;

	public enum userVerificationState {
		Verified, Rejected, Pending
	}

	public static final String[] symptoms = {
			"Fever",
			"Fatigue",
			"Dry Cough",
			"Aches and Pains",
			"Sore Throat",
			"Nasal Congestion",
			"Runny Nose",
			"Diarrhoea",
			"Anosmia",
			"Rash",
			"Conjunctivitis",
			"Headache",
			"Asymptomatic"
	};

	/**
	 * @param query Severity or Condition
	 * @return range of neighbouring conditions for matching
	 */
	public static String[] getSeverityAndConditionSearchRange(String query) {
		String[] range;
		switch (query) {
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

		return range;
	}
}
