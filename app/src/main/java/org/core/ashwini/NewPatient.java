package org.core.ashwini;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.core.ashwini.databinding.ActivityNewPatientBinding;

public class NewPatient extends AppCompatActivity {

    private static final String TAG = "NewPatient";
    boolean isSexSelected = false;
    boolean isComorbiditySelected = false;
    boolean hasError;
    ProgressBar progressBar;
    ScrollView sv;
    Patient patient;
    Patient oldPatientData;
    private DatabaseReference patientDB;
    private ActivityNewPatientBinding binding;
    private boolean editMode = false;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Add New Patient");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sv = binding.scrollView;
        progressBar = binding.progressBar;
        progressBar.setVisibility(View.INVISIBLE);
        patientDB = Values.patientDB;

        patient = new Patient();
        patient.setInitial();
        setSliderTags();

        // Check for editMode
        oldPatientData = (Patient) getIntent().getSerializableExtra("edit");
        key = getIntent().getStringExtra("editKey");
        if (oldPatientData != null) {
            editMode = true;
            setEditModePresets();
        }


        inflateSymptomChips();
        binding.textFieldPatientName.requestFocus();
    }

    private void setEditModePresets() {
        binding.textFieldPatientName.getEditText().setText(oldPatientData.name);

        if (oldPatientData.sex.equals("Male")) {
            binding.radioButtonSexMale.performClick();
        } else if (oldPatientData.sex.equals("Female")) {
            binding.radioButtonSexFemale.performClick();
        } else {
            binding.radioButtonSexOther.performClick();
            binding.editTextPatientSexOther.setText(oldPatientData.sex);
        }

        binding.textFieldPatientAge.getEditText().setText(String.valueOf(oldPatientData.age));

        // Symptoms wil be handled by the inflater

        if (oldPatientData.comorbidities.equals("No")) {
            binding.radioButtonComorbidityNo.performClick();
        } else {
            binding.radioButtonComorbidityYes.performClick();
            binding.editTextComorbidties.setText(oldPatientData.comorbidities);
        }

        binding.sliderSymptomSeverity.setValue(Values.getKeyByValue(Values.intensity, oldPatientData.symptomsSeverity));
        patient.setSymptomsSeverity(oldPatientData.symptomsSeverity);
        binding.sliderPatientCondition.setValue(Values.getKeyByValue(Values.intensity, oldPatientData.condition));
        patient.setCondition(oldPatientData.condition);

        binding.textFieldTreatmentPlan.getEditText().setText(oldPatientData.treatmentPlan);
        binding.textFieldPeriodOfTreatment.getEditText().setText(oldPatientData.periodOfTreatment);
        binding.textFieldMethodOfTreatmentAdministration.getEditText().setText(oldPatientData.methodOfTreatmentAdministration);
        binding.textFieldFrequencyOfAdministrationPerDay.getEditText().setText(String.valueOf(oldPatientData.frequencyOfAdministrationPerDay));
        binding.textFieldFrequencyOfAdministrationPerWeek.getEditText().setText(String.valueOf(oldPatientData.frequencyOfAdministrationPerWeek));
        binding.textFieldResult.getEditText().setText(oldPatientData.result);
        binding.textFieldSideEffects.getEditText().setText(oldPatientData.sideEffects);
        binding.textFieldPatientInfectivity.getEditText().setText(oldPatientData.patientInfectivity);
        binding.textFieldSourceOfInfection.getEditText().setText(oldPatientData.sourceOfInfection);
    }

    /**
     * Inflates filter chips that allow symptoms to be selected
     */
    private void inflateSymptomChips() {
        ChipGroup symptomChipGroup = binding.chipGroupSymptoms;
        for (String symptom : Values.symptoms) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.symptom_chip_single, symptomChipGroup, false);
            symptomChipGroup.addView(chip);
            chip.setText(symptom);

            if (editMode) {
                chip.setChecked(oldPatientData.symptomList.get(symptom));
            }
        }
    }

    /**
     * Sets slider labels for patient's symptom severity and condition, along with providing
     * callback listeners for setting the value upon selection.
     */
    public void setSliderTags() {
        Slider symptomSeveritySlider = binding.sliderSymptomSeverity;
        symptomSeveritySlider.setLabelFormatter(value -> {

            Log.d(TAG, "setSliderTags: " + value);

            patient.setSymptomsSeverity(Values.intensity.get(value));
//
//			if (value == 0.00) {
//				patient.setSymptomsSeverity("Very Mild");
//			} else if (value == 20.0) {
//				patient.setSymptomsSeverity("Mild");
//			} else if (value == 40.0) {
//				patient.setSymptomsSeverity("Moderate");
//			} else if (value == 60.0) {
//				patient.setSymptomsSeverity("Severe");
//			} else if (value == 80.0) {
//				patient.setSymptomsSeverity("Very Severe");
//			} else {
//				return "";
//			}

            return patient.getSymptomsSeverity();
        });

        Slider conditionSlider = binding.sliderPatientCondition;
        conditionSlider.setLabelFormatter(value -> {
            Log.d(TAG, "setSliderTags: " + value);
            patient.setCondition(Values.intensity.get(value));
            //
//			if (value == 0.00) {
//				patient.setCondition("Very Mild");
//			} else if (value == 20.0) {
//				patient.setCondition("Mild");
//			} else if (value == 40.0) {
//				patient.setCondition("Moderate");
//			} else if (value == 60.0) {
//				patient.setCondition("Severe");
//			} else if (value == 80.0) {
//				patient.setCondition("Very Severe");
//			} else {
//				return "";
//			}
            return patient.getCondition();
        });
    }

    /**
     * Invoked when the user selects the patient's sex. Sets the patient sex if either Male
     * or Female is chosen, or waits for setting in onSubmitButtonClicked() in case of
     * specification of sex.
     */
    @SuppressLint("NonConstantResourceId")
    public void onSexSelectRadioButtonClicked(View view) {
        EditText otherSexInput = binding.editTextPatientSexOther;
        isSexSelected = true;
        switch (view.getId()) {
            case R.id.radioButton_sex_male:
                patient.setSex(getString(R.string.male));
                otherSexInput.setVisibility(View.GONE);
                break;
            case R.id.radioButton_sex_female:
                patient.setSex(getString(R.string.female));
                otherSexInput.setVisibility(View.GONE);
                break;
            case R.id.radioButton_sex_other:
                otherSexInput.setVisibility(View.VISIBLE);
                otherSexInput.setEnabled(true);
                if (!editMode) {
                    otherSexInput.requestFocus();
                }
                break;
        }
    }

    /**
     * Helper method for onSubmitButtonClicked()
     * Checks and sets symptoms map upon submission.
     *
     * @return whether at least one checkbox has been checked
     */
    private boolean symptomListChips() {
        boolean checked = false;
        ChipGroup symptomsChipGroup = binding.chipGroupSymptoms;
        for (int i = 0; i < symptomsChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) symptomsChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                checked = true;
                patient.symptomList.put(chip.getText().toString(), true);
            }
        }

        return checked;
    }

    /**
     * This method is invoked when the submit button at the bottom of the form is pressed.
     * After being called, the method checks for any faults in the input, for example a mandatory
     * field like comorbidities being missing. It also checks for complicated scenarios like if
     * sex is selected as other, the other sex input field is filled or not.
     * <p>
     * Note: The checking order is reversed to have the most recent checks being made near the end,
     * with a bottom to top checking flow followed.
     */
    public void onSubmitButtonClicked(View view) {
        hasError = false;

        patient.setPatientInfectivity(binding.textFieldPatientInfectivity.getEditText().getText().toString());
        patient.setSourceOfInfection(binding.textFieldSourceOfInfection.getEditText().getText().toString());

        checkTextInputField(R.id.textField_side_effects, "effects", "Please specify any side effects that the patient had from the treatment!");
        checkTextInputField(R.id.textField_result, "result", "Please specify the results obtained on treatment administration!");
        checkTextInputField(R.id.textField_frequency_of_administration_per_week, "week", "Please specify the frequency of the treatment administration per week!");
        checkTextInputField(R.id.textField_frequency_of_administration_per_day, "day", "Please specify the frequency of the treatment administered per day!");
        checkTextInputField(R.id.textField_method_of_treatment_administration, "administration", "Please specify the method of treatment administration!");
        checkTextInputField(R.id.textField_period_of_treatment, "period", "Please specify the period of treatment!");
        checkTextInputField(R.id.textField_treatment_plan, "plan", "Please specify the treatment plan!");

        // Checks for empty response in comorbidity specification
        // The code section was small enough to not require a refactor
        EditText comorbidityDescription = binding.editTextComorbidties;
        if (!isComorbiditySelected) {
            errorScrollToView("Please specify whether the patient had any comorbidities!", R.id.textView_comorbidity);
        } else if (comorbidityDescription.isEnabled()) {
            String description = comorbidityDescription.getText().toString();
            if (description.trim().isEmpty()) {
                errorScrollToView("Please detail the patient's comorbidity!", R.id.textView_comorbidity);
            } else {
                patient.setComorbidities(description);
            }
        }

        // Checks if minimum one of the checkboxes in the symptom list has been selected
        if (!symptomListChips()) {
            errorScrollToView("Please specify at least one symptom!", R.id.textView_patient_symptoms);
        }

        // Checks for empty response in sex specification
        // The code section was small enough to not require a refactor
        EditText otherSexInputEditText = binding.editTextPatientSexOther;
        if (!isSexSelected) {
            errorScrollToView("Please specify patient's sex!", R.id.textView_patient_sex);
            hasError = true;
        } else if (otherSexInputEditText.isEnabled()) {
            String otherSexInput = otherSexInputEditText.getText().toString();
            if (otherSexInput.trim().isEmpty()) {
                hasError = true;
                errorScrollToView("Please type in the patient's sex!", R.id.textView_patient_sex);
            } else {
                patient.setSex(otherSexInput);
            }
        }

        checkTextInputField(R.id.textField_patient_age, "age", "Please specify the patient's age!");

        checkTextInputField(R.id.textField_patient_name, "name", "Please specify the patient's name!");

        if (!hasError) {
            progressBar.setVisibility(View.VISIBLE);
            uploadData();
        }
    }

    /**
     * Called when there are no erroneous inputs in the form. Uploads data to the firebase RTDB with
     * a unique push ID auto-generated from the server time. Callback listeners notify users whether
     * data has been successfully uploaded or not.
     */
    public void uploadData() {
        Log.d(TAG, "uploadData: " + patient);

        if (!isOnline()) {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        patient.setDoctorID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if (editMode) {
            patientDB.child(key).setValue(patient).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "uploadData onSuccess Edit: Data edit uploaded to firebase");
                Toast.makeText(NewPatient.this, "Patient Data Successfully Edited", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                setResult(RESULT_OK);
                finish();
            }).addOnFailureListener(e -> {
                Log.d(TAG, "uploadData onFailure: Data Edit Upload Failed!");
                Toast.makeText(NewPatient.this, "Patient Data Edit Failed! Please check your network!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                setResult(RESULT_CANCELED);
                finish();
            });
        } else {
            patientDB.push().setValue(patient).addOnSuccessListener(aVoid -> {
                Log.d(TAG, "uploadData onSuccess: Data successfully uploaded to firebase");
                Toast.makeText(NewPatient.this, "Patient Data Successfully Added", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            }).addOnFailureListener(e -> {
                Log.d(TAG, "uploadData onFailure: Data Upload Failed!");
                Toast.makeText(NewPatient.this, "Patient Data Addition Failed! Please check your network!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            });
        }


    }

    /**
     * Invoked when the user selects a comorbidity state.
     */
    @SuppressLint("NonConstantResourceId")
    public void onComorbiditySelectRadioButtonClicked(View view) {
        isComorbiditySelected = true;
        EditText comorbidityDescription = binding.editTextComorbidties;
        switch (view.getId()) {
            case R.id.radioButton_comorbidity_no:
                patient.setComorbidities("No");
                comorbidityDescription.setEnabled(false);
                break;
            case R.id.radioButton_comorbidity_yes:
                comorbidityDescription.setEnabled(true);
                if (!editMode) {
                    comorbidityDescription.requestFocus();
                }
                break;
        }
    }

    /**
     * Helper method for onSubmitButtonClicked()
     * In the case of an non-text based input field like a radiobutton or checkbox, scrolls to the
     * header text view.
     *
     * @param errorString the error to be shown to the user
     * @param viewId      the header text view resource ID
     */
    private void errorScrollToView(String errorString, @IdRes int viewId) {
        hasError = true;
        Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        View targetView = findViewById(viewId);
        sv.smoothScrollTo(0, (int) targetView.getY());
    }

    /**
     * Helper method for onSubmitButtonClicked()
     * In the case of a text input field being empty, it displays an error on the associated view.
     * Otherwise, it sets the corresponding member in the patient object
     *
     * @param textFieldId Resource ID of the text field that is to be checked
     * @param parameter   specifies which data member of the patient object is to be set
     * @param error       to be shown to the user in case of empty input
     */
    private void checkTextInputField(@IdRes int textFieldId, String parameter, String error) {
        TextInputLayout textInputLayout = findViewById(textFieldId);
        String text = textInputLayout.getEditText().getText().toString();

        if (text.trim().isEmpty()) {
            textInputLayout.setError(error);
            hasError = true;
        } else {
            switch (parameter) {
                case "name":
                    patient.setName(text);
                    break;
                case "age":
                    patient.setAge(Integer.parseInt(text));
                    break;
                case "plan":
                    patient.setTreatmentPlan(text);
                    break;
                case "period":
                    patient.setPeriodOfTreatment(text);
                    break;
                case "administration":
                    patient.setMethodOfTreatmentAdministration(text);
                    break;
                case "day":
                    patient.setFrequencyOfAdministrationPerDay(Integer.parseInt(text));
                    break;
                case "week":
                    patient.setFrequencyOfAdministrationPerWeek(Integer.parseInt(text));
                    break;
                case "result":
                    patient.setResult(text);
                    break;
                case "effects":
                    patient.setSideEffects(text);
                    break;
            }
        }
    }

    /**
     * @return true if internet connection is available
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}