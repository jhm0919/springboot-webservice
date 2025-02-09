package com.jhm.springbootwebservice.service.post;

import com.jhm.springbootwebservice.domain.post.PostType;
import com.jhm.springbootwebservice.web.dto.request.UserSearchDto;
import com.jhm.springbootwebservice.web.dto.response.PostListResponseDto;
import com.jhm.springbootwebservice.web.dto.response.PostResponseDto;
import com.jhm.springbootwebservice.web.dto.request.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.request.PostUpdateRequestDto;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface PostService {

    Long save(Long userId, PostsSaveRequestDto requestDto) throws Exception;

    Long update(Long postId, PostUpdateRequestDto requestDto);

    void updateView(Long id);

    PostResponseDto findById(Long id);

    Page<PostListResponseDto> findAll(PostType postType, UserSearchDto userSearchDto, int page, int myPost, Long userId);

    void delete(Long id) throws IOException;
}
