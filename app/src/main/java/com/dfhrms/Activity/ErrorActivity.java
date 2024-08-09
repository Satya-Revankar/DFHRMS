package com.dfhrms.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dfhrms.R;

public class ErrorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        // Retrieve the error message from the intent
        String errorMessage = getIntent().getStringExtra("error_message");

        // Display the error message
        TextView errorMessageTextView = findViewById(R.id.error_message);
        errorMessageTextView.setText(errorMessage != null ? errorMessage : "An unexpected error occurred.");

        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the app
                finishAffinity(); // Closes all activities
                System.exit(0);
            }
        });
    }
}
