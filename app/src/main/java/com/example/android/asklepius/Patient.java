package com.example.android.asklepius;

import java.util.HashMap;
import java.util.Map;

public class Patient {
	public String name;
	public String sex;
	public int age;
	public Map<String, Boolean> symptomList; // CheckBox
	public String comorbidities; // Yes/No
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

	// Default constructor to initialize some fields of the patient data
	// We'll go with Java defaults for the fields that are not initialized here
	Patient() {
		symptomList = new HashMap<String, Boolean>(13);
		symptomList.put("Fever", false);
		symptomList.put("Fatigue", false);
		symptomList.put("Dry Cough", false);
		symptomList.put("Aches and Pains", false);
		symptomList.put("Sore Throat", false);
		symptomList.put("Nasal Congestion", false);
		symptomList.put("Runny Nose", false);
		symptomList.put("Diarrhoea", false);
		symptomList.put("Anosmia", false);
		symptomList.put("Rash", false);
		symptomList.put("Conjunctivitis", false);
		symptomList.put("Headache", false);
		symptomList.put("Asymptomatic", false);

		symptomsSeverity = "Very Mild";
		condition = "Very Mild";
	}


	public String getName() {
		return name;
	}

	public String getSex() {
		return sex;
	}

	public int getAge() {
		return age;
	}

	public Map<String, Boolean> getSymptomList() {
		return symptomList;
	}

	public String getComorbidities() {
		return comorbidities;
	}

	public String getSymptomsSeverity() {
		return symptomsSeverity;
	}

	public String getCondition() {
		return condition;
	}

	public String getTreatmentPlan() {
		return treatmentPlan;
	}

	public String getPeriodOfTreatment() {
		return periodOfTreatment;
	}

	public String getMethodOfTreatmentAdministration() {
		return methodOfTreatmentAdministration;
	}

	public int getFrequencyOfAdministrationPerDay() {
		return frequencyOfAdministrationPerDay;
	}

	public int getFrequencyOfAdministrationPerWeek() {
		return frequencyOfAdministrationPerWeek;
	}

	public String getResult() {
		return result;
	}

	public String getSideEffects() {
		return sideEffects;
	}

	public String getSourceOfInfection() {
		return sourceOfInfection;
	}

	public String getPatientInfectivity() {
		return patientInfectivity;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setSymptomList(Map<String, Boolean> symptomList) {
		this.symptomList = symptomList;
	}

	public void setComorbidities(String comorbidities) {
		this.comorbidities = comorbidities;
	}

	public void setSymptomsSeverity(String symptomsSeverity) {
		this.symptomsSeverity = symptomsSeverity;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setTreatmentPlan(String treatmentPlan) {
		this.treatmentPlan = treatmentPlan;
	}

	public void setPeriodOfTreatment(String periodOfTreatment) {
		this.periodOfTreatment = periodOfTreatment;
	}

	public void setMethodOfTreatmentAdministration(String methodOfTreatmentAdministration) {
		this.methodOfTreatmentAdministration = methodOfTreatmentAdministration;
	}

	public void setFrequencyOfAdministrationPerDay(int frequencyOfAdministrationPerDay) {
		this.frequencyOfAdministrationPerDay = frequencyOfAdministrationPerDay;
	}

	public void setFrequencyOfAdministrationPerWeek(int frequencyOfAdministrationPerWeek) {
		this.frequencyOfAdministrationPerWeek = frequencyOfAdministrationPerWeek;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setSideEffects(String sideEffects) {
		this.sideEffects = sideEffects;
	}

	public void setSourceOfInfection(String sourceOfInfection) {
		this.sourceOfInfection = sourceOfInfection;
	}

	public void setPatientInfectivity(String patientInfectivity) {
		this.patientInfectivity = patientInfectivity;
	}

	@Override
	public String toString() {
		return "Patient{" +
				"name='" + name + '\'' +
				", sex='" + sex + '\'' +
				", age=" + age +
				", symptomList=" + symptomList +
				", comorbidities='" + comorbidities + '\'' +
				", symptomsSeverity='" + symptomsSeverity + '\'' +
				", condition='" + condition + '\'' +
				", treatmentPlan='" + treatmentPlan + '\'' +
				", periodOfTreatment='" + periodOfTreatment + '\'' +
				", methodOfTreatmentAdministration='" + methodOfTreatmentAdministration + '\'' +
				", frequencyOfAdministrationPerDay=" + frequencyOfAdministrationPerDay +
				", frequencyOfAdministrationPerWeek=" + frequencyOfAdministrationPerWeek +
				", result='" + result + '\'' +
				", sideEffects='" + sideEffects + '\'' +
				", sourceOfInfection='" + sourceOfInfection + '\'' +
				", patientInfectivity='" + patientInfectivity + '\'' +
				'}';
	}
}
