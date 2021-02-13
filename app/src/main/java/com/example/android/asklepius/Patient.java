package com.example.android.asklepius;

import java.util.Map;

public class Patient {
	public String name;
	public String sex;
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

	public boolean isComorbidities() {
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

	public void setComorbidities(boolean comorbidities) {
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
}
