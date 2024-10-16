package jds.nrc.be.controller;

import jds.nrc.be.service.CsvFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CsvFileController {

    private final CsvFileService csvFileService;

    @GetMapping("/system/system-time")
    public ResponseEntity<Object> getTime() {
        return ResponseEntity.ok(System.currentTimeMillis());
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> send(@RequestBody MultipartFile file) {
        try {
            ResponseEntity<String> response = csvFileService.sendFile(file);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
