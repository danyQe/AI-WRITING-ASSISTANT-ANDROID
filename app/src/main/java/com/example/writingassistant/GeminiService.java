package com.example.writingassistant;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeminiService {
    private final GenerativeModel model;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public interface GenerateCallback {
        void onSuccess(String text);
        void onError(String error);
    }

    public GeminiService(String apiKey) {
        model = new GenerativeModel("gemini-pro", apiKey);
    }

    public void generateText(String prompt, GenerateCallback callback) {
        executor.execute(() -> {
            try {
                GenerativeModelFutures modelFutures = model.generateContentFutures(prompt);
                GenerateContentResponse response = modelFutures.get();
                String generatedText = response.getText();
                callback.onSuccess(generatedText);
            } catch (Exception e) {
                callback.onError("Error generating text: " + e.getMessage());
            }
        });
    }
}