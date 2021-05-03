package com.example.android.asklepius;

import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> {
    private static final String TAG = "PatientListAdapter";
    private static final int VIEW_PATIENT = 133;
    private static final int EDIT_PATIENT = 132;
    private static final int DELETE_PATIENT = 527;
    static ViewUploadedData activityContext;
    static PatientListAdapter currentContext;
    private static List<Patient> patientsDataset;
    private static boolean searchMode;

    public PatientListAdapter(List<Patient> patients) {
        patientsDataset = patients;
        searchMode = true;
        currentContext = this;
    }


    public PatientListAdapter(List<Patient> patients, ViewUploadedData activityContextParam) {
        patientsDataset = patients;
        activityContext = activityContextParam;
        currentContext = this;
    }

    private static void viewPatient(int position) {
        Intent intent = new Intent(activityContext, DisplayPatientData.class);
        intent.putExtra("display", patientsDataset.get(position));
        Log.d(TAG, "onClick: Patient DATA: " + patientsDataset.get(position).toString());
        activityContext.startActivity(intent);
    }

    private static void editPatient(int position) {
        Patient patient = patientsDataset.get(position);
        String key = Values.patients.get(patient);
        Intent intent = new Intent(activityContext, NewPatient.class);
        intent.putExtra("edit", patient);
        intent.putExtra("editKey", key);
        Log.d(TAG, "onClick: Patient DATA: " + patient);
        Log.d(TAG, "onClick: Patient KEY: " + key);
        activityContext.startActivityForResult(intent, Values.EDIT_PATIENT_DATA);
    }

    private static void deletePatient(int position) {
        Patient patient = patientsDataset.get(position);
        String key = Values.patients.get(patient);
        Log.d(TAG, "onClick: Patient DATA: " + patient);
        Log.d(TAG, "onClick: Patient KEY: " + key);
        Values.patientDB.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(activityContext.binding.recylerViewUploadedPatientData, "Patient Deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Values.patientDB.child(key).setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        patientsDataset.add(position, patient);
                                        currentContext.notifyItemInserted(position);
                                        currentContext.notifyItemRangeChanged(position, patientsDataset.size());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activityContext, "Could Not Restore The Deleted Patient!", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "onFailure: Could not restore: " + e.toString());
                                    }
                                });

                            }
                        }).show();
            }
        });

        patientsDataset.remove(position);
        currentContext.notifyItemRemoved(position);
        currentContext.notifyItemRangeChanged(position, patientsDataset.size());


    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Patient patient = patientsDataset.get(position);
        holder.patientNameTextView.setText("Patient's Name: " + patient.getName());
        holder.patientAgeTextView.setText("Age: " + patient.getAge());
        holder.patientSexTextView.setText("Sex: " + patient.getSex());
        holder.patientSymptomsTextView.setText("Symptoms: " + formatSymptoms(patient));
        holder.patientComorbiditiesTextView.setText("Comorbidities: " + patient.getComorbidities());
        holder.patientSymptomSeverityTextView.setText("Symptom Severity: " + patient.getSymptomsSeverity());
        holder.patientConditionTextView.setText("Condition: " + patient.getCondition());

        // If patient name is confidential
        if (searchMode) {
            holder.patientNameTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return patientsDataset.size();
    }

    private String formatSymptoms(Patient patient) {
        List<String> symptomList = new ArrayList<>();
        Map<String, Boolean> symptoms = patient.getSymptomList();
        for (String key : Values.symptoms) {
            if (symptoms.get(key)) {
                symptomList.add(key);
            }
        }

        StringBuilder string = new StringBuilder();
        string.append(symptomList.get(0));
        for (int i = 1; i < symptomList.size(); i++) {
            string.append(", ").append(symptomList.get(i));
        }

        return string.toString();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView patientNameTextView;
        TextView patientAgeTextView;
        TextView patientSexTextView;
        TextView patientSymptomsTextView;
        TextView patientComorbiditiesTextView;
        TextView patientSymptomSeverityTextView;
        TextView patientConditionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientNameTextView = itemView.findViewById(R.id.textView_patient_name_display);
            patientAgeTextView = itemView.findViewById(R.id.textView_patient_age_display);
            patientSexTextView = itemView.findViewById(R.id.textView_patient_sex_display);
            patientSymptomsTextView = itemView.findViewById(R.id.textView_patient_symptoms_display);
            patientComorbiditiesTextView = itemView.findViewById(R.id.textView_comorbidities_display);
            patientSymptomSeverityTextView = itemView.findViewById(R.id.textView_symptom_severity_display);
            patientConditionTextView = itemView.findViewById(R.id.textView_patient_condition_display);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Trigger Complete Detail Display Activity
                    viewPatient(getAdapterPosition());
                }
            });

            if (!searchMode) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                            @Override
                            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                                MenuItem view = menu.add(Menu.NONE, VIEW_PATIENT, 1, "View");
                                MenuItem edit = menu.add(Menu.NONE, EDIT_PATIENT, 2, "Edit");
                                MenuItem delete = menu.add(Menu.NONE, DELETE_PATIENT, 3, "Delete");
                                view.setOnMenuItemClickListener(item -> {
                                    viewPatient(getAdapterPosition());
                                    return false;
                                });
                                edit.setOnMenuItemClickListener(item -> {
                                    editPatient(getAdapterPosition());
                                    return false;
                                });
                                delete.setOnMenuItemClickListener(item -> {
                                    deletePatient(getAdapterPosition());
                                    return false;
                                });
                            }
                        });

                        return false;
                    }
                });
            }
        }
    }

}
