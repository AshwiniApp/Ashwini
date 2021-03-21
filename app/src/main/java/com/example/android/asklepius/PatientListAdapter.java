package com.example.android.asklepius;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.ViewHolder> implements Serializable {
	private static List<Patient> patientsDataset;

	private static final String TAG = "PatientListAdapter";
	public PatientListAdapter(List<Patient> patients) {
		patientsDataset = patients;
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
		holder.patientNameTextView.setText(patient.getName());
		holder.patientAgeTextView.setText(String.valueOf(patient.getAge()));
		holder.patientSexTextView.setText(patient.getSex());
		holder.patientSymptomsTextView.setText(formatComorbidities(patient));
		holder.patientComorbiditiesTextView.setText(patient.getComorbidities());
		holder.patientSymptomSeverityTextView.setText(patient.getSymptomsSeverity());
		holder.patientConditionTextView.setText(patient.getCondition());
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

	private String formatComorbidities(Patient patient) {
		StringBuilder symptomString = new StringBuilder();
		Map<String, Boolean> symptoms = patient.getSymptomList();
		symptomString.append("Fever: ").append(symptoms.get("Fever")).append("\n");
		symptomString.append("Fatigue: ").append(symptoms.get("Fatigue")).append("\n");
		symptomString.append("Dry Cough: ").append(symptoms.get("Dry Cough")).append("\n");
		symptomString.append("Aches and Pains: ").append(symptoms.get("Aches and Pains")).append("\n");
		symptomString.append("Sore Throat: ").append(symptoms.get("Sore Throat")).append("\n");
		symptomString.append("Nasal Congestion: ").append(symptoms.get("Nasal Congestion")).append("\n");
		symptomString.append("Runny Nose: ").append(symptoms.get("Runny Nose")).append("\n");
		symptomString.append("Diarrhoea: ").append(symptoms.get("Diarrhoea")).append("\n");
		symptomString.append("Anosmia: ").append(symptoms.get("Anosmia")).append("\n");
		symptomString.append("Rash: ").append(symptoms.get("Rash")).append("\n");
		symptomString.append("Conjunctivitis: ").append(symptoms.get("Conjunctivitis")).append("\n");
		symptomString.append("Headache: ").append(symptoms.get("Headache")).append("\n");
		symptomString.append("Asymptomatic: ").append(symptoms.get("Asymptomatic"));
		return symptomString.toString();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements Serializable {

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
					int position = getAdapterPosition();

					Patient patient = patientsDataset.get(position);

					Log.d(TAG, "onClick: Patient DATA: " + patient.toString());
					
//					Intent intent = new Intent(this, DisplayPatientData.class);
//					intent.putExtra("display", patient);
//					itemView.getContext().startActivity(intent);
				}
			});
		}
	}

}
