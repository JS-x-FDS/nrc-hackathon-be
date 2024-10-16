package jds.nrc.be.controller;

import jds.nrc.be.service.CsvFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
public class CsvFileController {

    @Autowired
    private CsvFileService csvFileService;

    // Endpoint to receive file from App A and forward it to App B
    @PostMapping("/upload")
    public ResponseEntity<?> send(@RequestParam("file") MultipartFile file, @RequestParam("url") String url) {
        try {
            if (!isCsvFile(file)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type.");
            }
            
            ResponseEntity<String> response = csvFileService.sendFile(url, file);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // Validate if the uploaded file is a CSV file
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
