package com.example.diagnosis_summarizer;

public class OCRResponse {
    private String summary;

    public OCRResponse() {
    }

    public OCRResponse(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
