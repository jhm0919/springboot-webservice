package com.jhm.springbootwebservice.web;

import com.jhm.springbootwebservice.service.upload.FileUploadService;
import com.jhm.springbootwebservice.web.dto.response.FileResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/image")
@RestController
public class CKController {

    private final FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUploadFromCkEditor(MultipartRequest request) throws IOException {
        String url = fileUploadService.upload(request);
        return new ResponseEntity<>(FileResponse.builder()
                .uploaded(true)
                .url(url)
                .build(), HttpStatus.OK);
    }
}
