package com.jhm.springbootwebservice.service.upload;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    private String tempLocation = "C:\\springboot-webservice\\src\\main\\resources\\static\\temp\\";

    public String upload(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile("upload");
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = tempLocation + uuidFileName;

        String url = "/temp/" + uuidFileName;

        File localFile = new File(localPath);
        file.transferTo(localFile);
        return url;
    }
}
