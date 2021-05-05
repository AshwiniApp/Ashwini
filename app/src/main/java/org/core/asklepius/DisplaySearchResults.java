package org.core.asklepius;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DisplaySearchResults extends AppCompatActivity {

    private static final String TAG = "DisplaySearchResults";
    static List<Patient> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Results");

        if (filteredList == null) {
            Log.d(TAG, "onCreate: null filteredList");
            Toast.makeText(this, "Sorry, there was an error in loading the " +
                    "list! Please try later", Toast.LENGTH_SHORT).show();
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView_search_results);

        if (filteredList.isEmpty()) {
            Snackbar.make(recyclerView, "Looks like there aren't any great matches for your search right now!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Okay", v -> {
                        // Do nothing
                    })
                    .show();
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            PatientListAdapter adapter = new PatientListAdapter(filteredList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}