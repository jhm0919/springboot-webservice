package com.jhm.springbootwebservice.service.posts;

import com.jhm.springbootwebservice.web.dto.PostsListResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsResponseDto;
import com.jhm.springbootwebservice.web.dto.PostsSaveRequestDto;
import com.jhm.springbootwebservice.web.dto.PostsUpdateRequestDto;

import java.util.List;

public interface PostsService {

    Long save(PostsSaveRequestDto requestDto);

    Long update(Long id, PostsUpdateRequestDto requestDto);

    PostsResponseDto findById(Long id);

    List<PostsListResponseDto> findAllDesc();

    void delete(Long id);

}
