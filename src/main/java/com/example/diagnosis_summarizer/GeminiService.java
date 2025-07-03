package com.example.diagnosis_summarizer;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String API_KEY = "AIzaSyCskhMvLUSrLwwKRVroaPeWdXm3QvynJLM";

    private final WebClient webClient = WebClient.builder()
            .baseUrl(API_URL)
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build();

    public String summarizeText(String extractedText) {

        var payload = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", "Summarize this medical text in simple layman's terms:\n\n" + extractedText)
                                )
                        )
                )
        );

        Map response = webClient.post()
                .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("key", API_KEY)
                                .build()
                )
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response != null) {
            List candidates = (List) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map firstCandidate = (Map) candidates.get(0);
                Map content = (Map) firstCandidate.get("content");
                List parts = (List) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    Map firstPart = (Map) parts.get(0);
                    return firstPart.get("text").toString();
                }
            }
        }

        return "Gemini summarization failed.";
    }
}
