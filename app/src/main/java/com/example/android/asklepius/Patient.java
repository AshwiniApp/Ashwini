package com.example.android.asklepius;

import java.util.Map;

public class Patient {
	public String name;
	public char sex;
	public int age;
	public Map<String, Boolean> symptomList; // CheckBox
	public boolean comorbidities; // Yes/No
	public String symptomsSeverity; // Very mild/Mild/Average/Severe/Very Severe
	public String condition; // Very mild/Mild/Average/Severe/Very Severe
	public String treatmentPlan; // TODO optional document upload, successive period upload, if possible
	public String periodOfTreatment;
	public String methodOfTreatmentAdministration;
	public int frequencyOfAdministrationPerDay;
	public int frequencyOfAdministrationPerWeek;
	public String result; // TODO optional document upload
	public String sideEffects;
	public String sourceOfInfection; // Optional
	public String patientInfectivity; // Optional

	public Patient(String name, char sex, int age, Map<String, Boolean> symptomList,
	               boolean comorbidities, String symptomsSeverity, String condition,
	               String treatmentPlan, String periodOfTreatment, String methodOfTreatmentAdministration,
	               int frequencyOfAdministrationPerDay, int frequencyOfAdministrationPerWeek, String result,
	               String sideEffects, String sourceOfInfection, String patientInfectivity) {
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.symptomList = symptomList;
		this.comorbidities = comorbidities;
		this.symptomsSeverity = symptomsSeverity;
		this.condition = condition;
		this.treatmentPlan = treatmentPlan;
		this.periodOfTreatment = periodOfTreatment;
		this.methodOfTreatmentAdministration = methodOfTreatmentAdministration;
		this.frequencyOfAdministrationPerDay = frequencyOfAdministrationPerDay;
		this.frequencyOfAdministrationPerWeek = frequencyOfAdministrationPerWeek;
		this.result = result;
		this.sideEffects = sideEffects;
		this.sourceOfInfection = sourceOfInfection;
		this.patientInfectivity = patientInfectivity;
	}
}
