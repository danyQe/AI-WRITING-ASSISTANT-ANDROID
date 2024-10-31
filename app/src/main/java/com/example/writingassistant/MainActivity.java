package com.example.writingassistant;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class MainActivity extends AppCompatActivity {
    private EditText promptInput;
    private TextView generatedText;
    private MaterialButton generateButton;
    private MaterialButton copyButton;
    private CircularProgressIndicator progressIndicator;
    private FloatingActionButton homeButton;
    private View promptDialog;
    private GeminiService geminiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
        
        geminiService = new GeminiService("YOUR_GEMINI_API_KEY");
    }

    private void initializeViews() {
        promptInput = findViewById(R.id.promptInput);
        generatedText = findViewById(R.id.generatedText);
        generateButton = findViewById(R.id.generateButton);
        copyButton = findViewById(R.id.copyButton);
        progressIndicator = findViewById(R.id.progressIndicator);
        homeButton = findViewById(R.id.homeButton);
        promptDialog = findViewById(R.id.promptDialog);
    }

    private void setupListeners() {
        homeButton.setOnClickListener(v -> togglePromptDialog());
        
        generateButton.setOnClickListener(v -> {
            String prompt = promptInput.getText().toString().trim();
            if (!prompt.isEmpty()) {
                generateText(prompt);
            }
        });

        copyButton.setOnClickListener(v -> copyToClipboard());
    }

    private void togglePromptDialog() {
        if (promptDialog.getVisibility() == View.VISIBLE) {
            promptDialog.setVisibility(View.GONE);
        } else {
            promptDialog.setVisibility(View.VISIBLE);
        }
    }

    private void generateText(String prompt) {
        setLoading(true);
        
        geminiService.generateText(prompt, new GeminiService.GenerateCallback() {
            @Override
            public void onSuccess(String text) {
                runOnUiThread(() -> {
                    generatedText.setText(text);
                    generatedText.setVisibility(View.VISIBLE);
                    copyButton.setVisibility(View.VISIBLE);
                    setLoading(false);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    setLoading(false);
                });
            }
        });
    }

    private void setLoading(boolean isLoading) {
        progressIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        generateButton.setEnabled(!isLoading);
    }

    private void copyToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Generated Text", generatedText.getText().toString());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}