package org.core.ashwini;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.annotations.NotNull;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@IgnoreExtraProperties
public class Patient implements Serializable {
    public String name;
    public String sex;
    public int age;
    public Map<String, Boolean> symptomList; // CheckBox
    public String comorbidities; // Yes/No
    public String symptomsSeverity; // Very mild/Mild/Moderate/Severe/Very Severe
    public String condition; // Very mild/Mild/Moderate/Severe/Very Severe
    public String treatmentPlan; // TODO optional document upload, successive period upload, if possible
    public String periodOfTreatment;
    public String methodOfTreatmentAdministration;
    public int frequencyOfAdministrationPerDay;
    public int frequencyOfAdministrationPerWeek;
    public String result; // TODO optional document upload
    public String sideEffects;
    public String sourceOfInfection; // Optional
    public String patientInfectivity; // Optional
    public String doctorID;

    Patient() {

    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    // Default constructor to initialize some fields of the patient data
    // We'll go with Java defaults for the fields that are not initialized here
    public void setInitial() {
        symptomList = new LinkedHashMap<>(13);
        for (String symptom : Values.symptoms) {
            symptomList.put(symptom, false);
        }

        symptomsSeverity = "Very Mild";
        condition = "Very Mild";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Map<String, Boolean> getSymptomList() {
        return symptomList;
    }

    public void setSymptomList(Map<String, Boolean> symptomList) {
        this.symptomList = symptomList;
    }

    public String getComorbidities() {
        return comorbidities;
    }

    public void setComorbidities(String comorbidities) {
        this.comorbidities = comorbidities;
    }

    public String getSymptomsSeverity() {
        return symptomsSeverity;
    }

    public void setSymptomsSeverity(String symptomsSeverity) {
        this.symptomsSeverity = symptomsSeverity;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public String getPeriodOfTreatment() {
        return periodOfTreatment;
    }

    public void setPeriodOfTreatment(String periodOfTreatment) {
        this.periodOfTreatment = periodOfTreatment;
    }

    public String getMethodOfTreatmentAdministration() {
        return methodOfTreatmentAdministration;
    }

    public void setMethodOfTreatmentAdministration(String methodOfTreatmentAdministration) {
        this.methodOfTreatmentAdministration = methodOfTreatmentAdministration;
    }

    public int getFrequencyOfAdministrationPerDay() {
        return frequencyOfAdministrationPerDay;
    }

    public void setFrequencyOfAdministrationPerDay(int frequencyOfAdministrationPerDay) {
        this.frequencyOfAdministrationPerDay = frequencyOfAdministrationPerDay;
    }

    public int getFrequencyOfAdministrationPerWeek() {
        return frequencyOfAdministrationPerWeek;
    }

    public void setFrequencyOfAdministrationPerWeek(int frequencyOfAdministrationPerWeek) {
        this.frequencyOfAdministrationPerWeek = frequencyOfAdministrationPerWeek;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getSourceOfInfection() {
        return sourceOfInfection;
    }

    public void setSourceOfInfection(String sourceOfInfection) {
        this.sourceOfInfection = sourceOfInfection;
    }

    public String getPatientInfectivity() {
        return patientInfectivity;
    }

    public void setPatientInfectivity(String patientInfectivity) {
        this.patientInfectivity = patientInfectivity;
    }

    @NotNull
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
                ", doctorID='" + doctorID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return age == patient.age &&
                frequencyOfAdministrationPerDay == patient.frequencyOfAdministrationPerDay &&
                frequencyOfAdministrationPerWeek == patient.frequencyOfAdministrationPerWeek &&
                Objects.equals(name, patient.name) &&
                Objects.equals(sex, patient.sex) &&
                Objects.equals(symptomList, patient.symptomList) &&
                Objects.equals(comorbidities, patient.comorbidities) &&
                Objects.equals(symptomsSeverity, patient.symptomsSeverity) &&
                Objects.equals(condition, patient.condition) &&
                Objects.equals(treatmentPlan, patient.treatmentPlan) &&
                Objects.equals(periodOfTreatment, patient.periodOfTreatment) &&
                Objects.equals(methodOfTreatmentAdministration, patient.methodOfTreatmentAdministration) &&
                Objects.equals(result, patient.result) &&
                Objects.equals(sideEffects, patient.sideEffects) &&
                Objects.equals(sourceOfInfection, patient.sourceOfInfection) &&
                Objects.equals(patientInfectivity, patient.patientInfectivity) &&
                Objects.equals(doctorID, patient.doctorID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sex, age, symptomList, comorbidities, symptomsSeverity, condition, treatmentPlan, periodOfTreatment, methodOfTreatmentAdministration, frequencyOfAdministrationPerDay, frequencyOfAdministrationPerWeek, result, sideEffects, sourceOfInfection, patientInfectivity, doctorID);
    }
}
