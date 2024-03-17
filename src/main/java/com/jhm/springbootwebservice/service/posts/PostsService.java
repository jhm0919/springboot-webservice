package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostsService {

    Long save(Long id, PostsSaveRequestDto requestDto, List<MultipartFile> multipartFiles);

    Long update(Long id, PostsUpdateRequestDto requestDto, List<MultipartFile> multipartFiles);

    int updateView(Long id);

    PostsResponseDto findById(Long id);

    Page<PostsListResponseDto> findAll(Pageable pageable, String postType, String searchType, String searchKeyword);

    void delete(Long id);

    Long deleteImage(Long postsId, Long id);
}
