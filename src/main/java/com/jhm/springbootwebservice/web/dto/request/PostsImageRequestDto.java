package com.jhm.springbootwebservice.web.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
public class PostsImageRequestDto {

    private List<MultipartFile> files;
}
