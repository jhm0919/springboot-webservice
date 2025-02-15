package com.jhm.springbootwebservice.web.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostImageRequestDto {

    private List<MultipartFile> files;
}
