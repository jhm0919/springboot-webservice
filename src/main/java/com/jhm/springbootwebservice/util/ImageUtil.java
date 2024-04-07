package com.jhm.springbootwebservice.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageUtil {

    private final String PREV_IMAGE_URL = "C:/springboot-webservice/src/main/resources/static/files/";

    public String ckUpload(MultipartRequest request) {

        MultipartFile uploadFile = request.getFile("upload");

        String fileName = getFileName(uploadFile);

//        String realPath = getPath(request);

//        String savePath = realPath + fileName;

        String uploadPath = PREV_IMAGE_URL + fileName;

        uploadFile(uploadPath, uploadFile);

        return uploadPath;
    }

    private void uploadFile(String savePath, MultipartFile uploadFile) {
        File file = new File(savePath);
        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload the file", e);
        }
    }

    private String getFileName(MultipartFile uploadFile) {
        String originalFileName = uploadFile.getOriginalFilename();
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }

//    private String getPath(MultipartRequest request) {
//        // 실제 파일 저장 경로
//        String realPath = request.getServletContext().getRealPath("/upload/");
//        Path directoryPath = Paths.get(realPath);
//        if (!Files.exists(directoryPath)) {
//            try {
//                Files.createDirectories(directoryPath);
//            } catch (IOException e) {
//                throw new RuntimeException("Could not create upload directory", e);
//            }
//        }
//        return realPath;
//    }
}
