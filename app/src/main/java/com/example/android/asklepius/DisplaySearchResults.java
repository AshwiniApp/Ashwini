package com.example.android.asklepius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;

import java.util.List;

public class DisplaySearchResults extends AppCompatActivity {

	static List<Patient> filteredList;
	private static final String TAG = "DisplaySearchResults";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_search_results);

		if (filteredList == null) {
			Log.d(TAG, "onCreate: null filteredList");
			Toast.makeText(this, "Sorry, there was an error in loading the " +
					"list! Please try later", Toast.LENGTH_SHORT).show();
		}

		RecyclerView recyclerView = findViewById(R.id.recyclerView_search_results);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		PatientListAdapter adapter = new PatientListAdapter(filteredList, true);
		recyclerView.setAdapter(adapter);
	}
}