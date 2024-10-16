package jds.nrc.be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    public ResponseEntity<String> sendFile(MultipartFile csvFile) {
        if (!isCsvFile(csvFile)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type.");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", csvFile);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForEntity(AI_URL + "/upload", requestEntity, String.class);
        } catch (Exception e) {
            return ResponseEntity.ok("There have been some errors during the execution of the file.");
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
