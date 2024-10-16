package jds.nrc.be.service;

import jds.nrc.be.data.AISendPromptResponse;
import jds.nrc.be.data.AIUploadFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CsvFileService {

    @Value("${app.ai.url}")
    private String AI_URL;

    public ResponseEntity<?> sendFile(MultipartFile csvFile) {
        if (!isCsvFile(csvFile)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type.");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource fileResource = new ByteArrayResource(csvFile.getBytes()) {
                @Override
                public String getFilename() {
                    return csvFile.getOriginalFilename();  // Return original file name
                }
            };
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<AIUploadFileResponse> response = restTemplate.postForEntity(AI_URL + "/upload", requestEntity, AIUploadFileResponse.class);

            System.out.println("File uploaded successfully.");
            return response;
        } catch (Exception e) {
            e.printStackTrace();  // For debugging
            return ResponseEntity.internalServerError().body("Error during file upload: " + e.getMessage());
        }
    }

    public ResponseEntity<?> sendPrompt(String message) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForEntity(AI_URL + "/chat?query=" + message, "", AISendPromptResponse.class);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("There have been some errors during the execution of the prompt.");
        }
    }

    private boolean isCsvFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.endsWith(".csv")) {
            return false;
        }

        String mimeType = file.getContentType();
        return mimeType == null || mimeType.equals("text/csv") || mimeType.equals("application/vnd.ms-excel");
    }
}
