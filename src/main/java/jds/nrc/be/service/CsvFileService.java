package jds.nrc.be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CsvFileService {

    @Autowired
    private RestTemplate restTemplate;

    // This method is used to send the file to App B
    public ResponseEntity<String> sendFile(String url, MultipartFile csvFile) {
        try {
            if (!isCsvFile(csvFile)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type.");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create InputStreamResource from the MultipartFile
            InputStreamResource inputStreamResource = new InputStreamResource(csvFile.getInputStream());

            // Prepare the request entity with the InputStreamResource
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", inputStreamResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Send POST request to App B and get the response
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return response;

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending file to App B: " + e.getMessage());
        }
    }

    private boolean isCsvFile(MultipartFile file) {
        // Check file extension
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.endsWith(".csv")) {
            return false;
        }

        String mimeType = file.getContentType();
        if (mimeType != null && !mimeType.equals("text/csv") && !mimeType.equals("application/vnd.ms-excel")) {
            return false;
        }

        return true;
    }
}
