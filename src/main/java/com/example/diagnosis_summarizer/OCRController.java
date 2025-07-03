package com.example.diagnosis_summarizer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "OCR API", description = "Endpoints for OCR text extraction and summarization")
public class OCRController {

    @Autowired
    private OCRService ocrService;
    @Autowired
    private GeminiService geminiService;

    @Operation(
            summary = "Upload an image file and get a diagnosis summary",
            description = "Uploads an image file (PNG, JPG, etc.) to extract text and optionally summarize it."
    )
    @PostMapping(
            value = "/summarize",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public OCRResponse summarize(
            @Parameter(
                    description = "Image file to be processed",
                    required = true
            )
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        String text;
        try {
            text = ocrService.extractText(tempFile);
        } catch (TesseractException e) {
            e.printStackTrace();
            text = "Failed to extract text.";
        }

        // Placeholder for gemini summarization
        String summarizedText = geminiService.summarizeText(text);
        return new OCRResponse(summarizedText);
    }


}
