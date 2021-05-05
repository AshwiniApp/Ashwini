package org.core.asklepius;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 605;
    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Collections.singletonList(
            new AuthUI.IdpConfig.GoogleBuilder().build());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initiates the FirebaseUI for auth
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                setResult(resultCode);
                Log.d(TAG, "onActivityResult: Sign In Successful!");
                Toast.makeText(this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
            } else {
                setResult(RESULT_CANCELED);
                Log.d(TAG, "onActivityResult: Sign In Failed!");
                Toast.makeText(this, "Sign In Failed!", Toast.LENGTH_SHORT).show();
            }

            finish();
        }
    }
}