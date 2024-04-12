package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.domain.posts.PostType;
import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostsUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.List;

public interface PostsService {

    Long save(Long userId, PostsSaveRequestDto requestDto) throws Exception;

    Long update(Long postId, PostsUpdateRequestDto requestDto);

    void updateView(Long id);

    PostsResponseDto findById(Long id);

    Page<PostsListResponseDto> findAll(PostType postType, UserSearchDto userSearchDto, Pageable pageable);

    void delete(Long id);
}
