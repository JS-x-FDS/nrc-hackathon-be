package com.jds.transferFile.service;

import com.jds.transferFile.model.FeedbackFile;
import com.jds.transferFile.repository.FeedbackFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackFileRepository feedbackFileRepository;

    public FeedbackFile storeFile(MultipartFile file) throws Exception {
        String content = new BufferedReader(new InputStreamReader(file.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        FeedbackFile feedbackFile = new FeedbackFile();
        feedbackFile.setFileName(file.getOriginalFilename());
        feedbackFile.setFileContent(content);

        return feedbackFileRepository.save(feedbackFile); // Lưu file vào DB (nếu cần)
    }

    public String sendFileToSystemB(String content) throws Exception {
        // Logic gửi file CSV tới hệ thống B qua API
        URL url = new URL("https://link-to-system-b/api/receive");  // URL hệ thống B
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write(content.getBytes());

        // Nhận phản hồi từ B
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = in.lines().collect(Collectors.joining("\n"));
        in.close();

        return response; // Trả về kết quả từ hệ thống B
    }
}