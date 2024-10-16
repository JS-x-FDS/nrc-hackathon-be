package com.jds.transferFile.controller;

import com.jds.transferFile.model.FeedbackFile;
import com.jds.transferFile.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/submit")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Lưu file vào hệ thống
            FeedbackFile feedbackFile = feedbackService.storeFile(file);

            // Gửi file đến hệ thống B và nhận kết quả
            String responseFromSystemB = feedbackService.sendFileToSystemB(feedbackFile.getFileContent());

            // Trả lại kết quả cho hệ thống A
            return ResponseEntity.ok(responseFromSystemB);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing the file");
        }
    }
