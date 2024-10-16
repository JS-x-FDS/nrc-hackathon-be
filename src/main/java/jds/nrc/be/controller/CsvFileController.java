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
    public ResponseEntity<?> send(@RequestBody MultipartFile file, @RequestParam("url") String url) {
        try {
            ResponseEntity<String> response = csvFileService.sendFile(url, file);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
