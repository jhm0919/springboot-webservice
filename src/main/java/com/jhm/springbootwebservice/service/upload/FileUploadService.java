package com.jhm.springbootwebservice.service.upload;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

//    private String localLocation = "C:\\Users\\jhm09\\Desktop\\images\\";
    private String tempLocation = "C:\\springboot-webservice\\src\\main\\resources\\static\\temp\\";
    private String localLocation = "C:\\springboot-webservice\\src\\main\\resources\\static\\files\\";

    public String upload(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile("upload");
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName;

        String tempUrl = "/temp/" + uuidFileName;
        String url = "/files/" + uuidFileName;

        File localFile = new File(localPath);
        file.transferTo(localFile);
        return url;
    }
}
